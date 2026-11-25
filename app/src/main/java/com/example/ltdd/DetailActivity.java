package com.example.ltdd;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

// Chi tiết sản phẩm: chọn số lượng + thêm vào giỏ + yêu thích
public class DetailActivity extends AppCompatActivity {

    TextView tvEmoji, tvName, tvRating, tvPrice, tvDesc, tvQty;
    Chip chipCategory;
    RatingBar ratingBar;
    ImageView imgMinus, imgPlus;
    Button btnAdd;
    MenuItem favItem;
    Product p;
    int qty = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Utils.applyInsets(findViewById(R.id.root));

        tvEmoji = findViewById(R.id.tvEmoji);
        tvName = findViewById(R.id.tvName);
        tvRating = findViewById(R.id.tvRating);
        tvPrice = findViewById(R.id.tvPrice);
        tvDesc = findViewById(R.id.tvDesc);
        tvQty = findViewById(R.id.tvQty);
        chipCategory = findViewById(R.id.chipCategory);
        ratingBar = findViewById(R.id.ratingBar);
        imgMinus = findViewById(R.id.imgMinus);
        imgPlus = findViewById(R.id.imgPlus);
        btnAdd = findViewById(R.id.btnAdd);

        // Nhận id sản phẩm từ trang chủ
        p = Product.find(getIntent().getIntExtra("ID", 1));

        // ----- Thanh trên: Back + nút tim yêu thích -----
        MaterialToolbar toolbar = Utils.setupToolbar(this, "Chi tiết sản phẩm");
        toolbar.inflateMenu(R.menu.menu_detail);
        favItem = toolbar.getMenu().findItem(R.id.action_fav);
        showFavIcon();
        toolbar.setOnMenuItemClickListener(item -> {
            Utils.toggleFav(this, p.id);
            showFavIcon();
            return true;
        });

        // ----- Hiển thị thông tin -----
        tvEmoji.setText(p.emoji);
        chipCategory.setText(p.category);
        tvName.setText(p.name);
        ratingBar.setRating(p.rating);
        tvRating.setText(String.valueOf(p.rating));
        tvPrice.setText(Utils.formatPrice(p.price));
        tvDesc.setText(p.description);
        updateQty();

        // ----- Tăng / giảm số lượng -----
        imgPlus.setOnClickListener(v -> {
            qty++;
            updateQty();
        });
        imgMinus.setOnClickListener(v -> {
            if (qty > 1) {
                qty--;
                updateQty();
            }
        });

        // ----- Thêm vào giỏ hàng -----
        btnAdd.setOnClickListener(v -> {
            Cart.add(p, qty);
            Snackbar.make(findViewById(R.id.root),
                            "Đã thêm " + qty + " × " + p.name + " vào giỏ hàng", Snackbar.LENGTH_LONG)
                    .setAnchorView(btnAdd)
                    .setAction("XEM GIỎ", view -> startActivity(new Intent(this, CartActivity.class)))
                    .show();
        });
    }

    private void updateQty() {
        tvQty.setText(String.valueOf(qty));
        btnAdd.setText("Thêm vào giỏ • " + Utils.formatPrice(p.price * qty));
    }

    private void showFavIcon() {
        favItem.setIcon(Utils.isFav(this, p.id)
                ? R.drawable.ic_fav_white : R.drawable.ic_fav_border_white);
    }
}
