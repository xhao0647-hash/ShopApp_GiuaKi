package com.example.ltdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.VH> {

    private final Context ctx;
    private final List<DatabaseHelper.Order> list;

    public OrderAdapter(Context ctx, List<DatabaseHelper.Order> list) {
        this.ctx = ctx;
        this.list = list;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvItems, tvTotal;

        VH(View v) {
            super(v);
            tvOrderId = v.findViewById(R.id.tvOrderId);
            tvDate = v.findViewById(R.id.tvDate);
            tvItems = v.findViewById(R.id.tvItems);
            tvTotal = v.findViewById(R.id.tvTotal);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(ctx).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        DatabaseHelper.Order o = list.get(position);
        h.tvOrderId.setText("Đơn hàng #" + o.id);
        h.tvDate.setText(o.date);
        h.tvItems.setText(o.items);
        h.tvTotal.setText("Tổng: " + Utils.formatPrice(o.total));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
