package com.example.ltdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    private final Context ctx;
    private final Runnable onChange;   // gọi lại để Activity tính lại tổng tiền

    public CartAdapter(Context ctx, Runnable onChange) {
        this.ctx = ctx;
        this.onChange = onChange;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName, tvPrice, tvQty;
        ImageView imgMinus, imgPlus, imgDelete;

        VH(View v) {
            super(v);
            tvEmoji = v.findViewById(R.id.tvEmoji);
            tvName = v.findViewById(R.id.tvName);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvQty = v.findViewById(R.id.tvQty);
            imgMinus = v.findViewById(R.id.imgMinus);
            imgPlus = v.findViewById(R.id.imgPlus);
            imgDelete = v.findViewById(R.id.imgDelete);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(ctx).inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Cart.Item it = Cart.items.get(position);
        h.tvEmoji.setText(it.product.emoji);
        h.tvName.setText(it.product.name);
        h.tvPrice.setText(Utils.formatPrice(it.product.price * it.qty));
        h.tvQty.setText(String.valueOf(it.qty));

        h.imgPlus.setOnClickListener(v -> {
            it.qty++;
            notifyDataSetChanged();
            onChange.run();
        });

        h.imgMinus.setOnClickListener(v -> {
            if (it.qty > 1) {
                it.qty--;
                notifyDataSetChanged();
                onChange.run();
            }
        });

        h.imgDelete.setOnClickListener(v -> {
            int pos = h.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            Cart.items.remove(pos);
            notifyItemRemoved(pos);
            onChange.run();
        });
    }

    @Override
    public int getItemCount() {
        return Cart.items.size();
    }
}
