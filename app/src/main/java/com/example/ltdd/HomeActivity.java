package com.example.ltdd;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

// Trang chủ: tìm kiếm + lọc danh mục + danh sách sản phẩm + menu trái + 3 nút dưới
public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navView;
    MaterialToolbar toolbar;
    TextInputLayout tilSearch;
    ChipGroup chipGroup;
    RecyclerView rvProducts;
    TextView tvHello, tvEmpty;
    BottomNavigationView bottomNav;

    ProductAdapter adapter;
    ArrayList<Product> shown = new ArrayList<>();   // danh sách đang hiển thị
    String category = "";       // "" = tất cả
    boolean onlyFav = false;    // chỉ hiện sản phẩm yêu thích

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Utils.applyInsets(findViewById(R.id.root));

        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        tilSearch = findViewById(R.id.tilSearch);
        chipGroup = findViewById(R.id.chipGroup);
        rvProducts = findViewById(R.id.rvProducts);
        tvHello = findViewById(R.id.tvHello);
        tvEmpty = findViewById(R.id.tvEmpty);
        bottomNav = findViewById(R.id.bottomNav);

        // ----- Thanh trên: Back (trái) + icon menu (phải) -----
        toolbar = Utils.setupToolbar(this, "Cửa hàng");
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        });

        // ----- Tên người dùng -----
        String user = Utils.getUser(this);
        String name = new DatabaseHelper(this).getFullName(user);
        tvHello.setText("Xin chào, " + name + " 👋");
        View header = navView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.tvNavName)).setText(name);
        ((TextView) header.findViewById(R.id.tvNavUser)).setText("@" + user);

        // ----- Danh sách sản phẩm (lưới 2 cột) -----
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, shown, p -> {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra("ID", p.id);
            startActivity(i);
        });
        rvProducts.setAdapter(adapter);

        // ----- Tìm kiếm: gõ chữ nào lọc luôn -----
        tilSearch.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter();
            }
        });

        // ----- Lọc theo danh mục (Chip) -----
        chipGroup.setOnCheckedStateChangeListener((group, ids) -> {
            int id = ids.isEmpty() ? R.id.chipAll : ids.get(0);
            if (id == R.id.chipPhone) category = "Điện thoại";
            else if (id == R.id.chipLaptop) category = "Laptop";
            else if (id == R.id.chipAccessory) category = "Phụ kiện";
            else category = "";
            filter();
        });

        // ----- Menu bên trái (Drawer) -----
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                onlyFav = false;
                filter();
            } else if (id == R.id.nav_fav) {
                onlyFav = true;
                filter();
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(this, OrdersActivity.class));
            } else if (id == R.id.nav_about) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Giới thiệu")
                        .setMessage("Shop Công Nghệ - ứng dụng bán hàng đơn giản.\nMôn: Lập trình thiết bị di động.")
                        .setPositiveButton("Đóng", null)
                        .show();
            } else if (id == R.id.nav_logout) {
                Utils.logout(this);
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // ----- 3 nút ở dưới: Trang chủ / Giỏ hàng / Tài khoản -----
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bnav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return false;   // false = không chuyển dấu chọn khỏi "Trang chủ"
            } else if (id == R.id.bnav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return false;
            }
            rvProducts.smoothScrollToPosition(0);
            return true;
        });

        // Bấm Back của điện thoại: nếu menu đang mở thì đóng menu trước
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });
    }

    // Quay lại trang này thì cập nhật lại tim yêu thích + số lượng giỏ hàng
    @Override
    protected void onResume() {
        super.onResume();
        filter();
        int n = Cart.getCount();
        if (n == 0) bottomNav.removeBadge(R.id.bnav_cart);
        else bottomNav.getOrCreateBadge(R.id.bnav_cart).setNumber(n);
    }

    // Lọc danh sách theo: danh mục + từ khoá tìm kiếm + yêu thích
    private void filter() {
        String keyword = tilSearch.getEditText().getText().toString().trim().toLowerCase();
        shown.clear();
        for (Product p : Product.LIST) {
            boolean okCategory = category.isEmpty() || p.category.equals(category);
            boolean okName = p.name.toLowerCase().contains(keyword);
            boolean okFav = !onlyFav || Utils.isFav(this, p.id);
            if (okCategory && okName && okFav) shown.add(p);
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
        toolbar.setTitle(onlyFav ? "Yêu thích" : "Cửa hàng");
    }
}
