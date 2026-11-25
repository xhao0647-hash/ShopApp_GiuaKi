package com.example.ltdd;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Lịch sử đơn hàng (đọc từ SQLite)
public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Utils.applyInsets(findViewById(R.id.root));
        Utils.setupToolbar(this, "Lịch sử đơn hàng");

        RecyclerView rvOrders = findViewById(R.id.rvOrders);
        TextView tvEmpty = findViewById(R.id.tvEmpty);

        ArrayList<DatabaseHelper.Order> orders =
                new DatabaseHelper(this).getOrders(Utils.getUser(this));

        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(new OrderAdapter(this, orders));
        tvEmpty.setVisibility(orders.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
