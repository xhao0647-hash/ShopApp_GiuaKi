package com.example.ltdd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Giỏ hàng: tăng giảm số lượng, xoá, mã giảm giá, thanh toán
public class CartActivity extends AppCompatActivity {

    RecyclerView rvCart;
    TextView tvEmpty, tvSubtotal, tvDiscount, tvTotal;
    TextInputLayout tilCoupon;
    Button btnApply, btnCheckout;
    CartAdapter adapter;
    int percent = 0;   // % giảm giá đang áp dụng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Utils.applyInsets(findViewById(R.id.root));
        Utils.setupToolbar(this, "Giỏ hàng");

        rvCart = findViewById(R.id.rvCart);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);
        tilCoupon = findViewById(R.id.tilCoupon);
        btnApply = findViewById(R.id.btnApply);
        btnCheckout = findViewById(R.id.btnCheckout);

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, this::updateTotals);
        rvCart.setAdapter(adapter);
        updateTotals();

        // ----- Mã giảm giá (thử mã GIAM10) -----
        btnApply.setOnClickListener(v -> {
            String code = Utils.getText(tilCoupon).toUpperCase();
            if (code.equals("GIAM10")) {
                percent = 10;
                tilCoupon.setError(null);
                Toast.makeText(this, "Đã áp dụng giảm 10%", Toast.LENGTH_SHORT).show();
            } else {
                percent = 0;
                tilCoupon.setError("Mã không hợp lệ");
            }
            updateTotals();
        });

        btnCheckout.setOnClickListener(v -> checkout());
    }

    // Tính lại tạm tính / giảm giá / tổng cộng
    private void updateTotals() {
        long subtotal = Cart.getTotal();
        long discount = subtotal * percent / 100;
        tvSubtotal.setText("Tạm tính: " + Utils.formatPrice(subtotal));
        tvDiscount.setText("Giảm giá (" + percent + "%): -" + Utils.formatPrice(discount));
        tvTotal.setText(Utils.formatPrice(subtotal - discount));

        boolean empty = Cart.items.isEmpty();
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        btnCheckout.setEnabled(!empty);
    }

        new MaterialAlertDialogBuilder(this)
                .setTitle("Xác nhận đặt hàng")
                .setMessage("Tổng thanh toán: " + Utils.formatPrice(total))
                .setNegativeButton("Huỷ", null)
                .setPositiveButton("Đặt hàng", (d, w) -> {
                    StringBuilder items = new StringBuilder();
                    for (Cart.Item it : Cart.items) {
                        if (items.length() > 0) items.append(", ");
                        items.append(it.product.name).append(" x").append(it.qty);
                    }
                    String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .format(new Date());

                    new DatabaseHelper(this).insertOrder(Utils.getUser(this), items.toString(), total, date);
                    Cart.clear();
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, OrdersActivity.class));
                    finish();
                })
                .show();
    }
}
