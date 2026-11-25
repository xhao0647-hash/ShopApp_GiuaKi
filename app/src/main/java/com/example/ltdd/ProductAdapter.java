package com.example.ltdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

    public interface OnProductClick {
        void onClick(Product p);
    }

    private final Context ctx;
    private final List<Product> list;
    private final OnProductClick listener;

    public ProductAdapter(Context ctx, List<Product> list, OnProductClick listener) {
        this.ctx = ctx;
        this.list = list;
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName, tvRating, tvPrice;
        ImageView imgFav;

        VH(View v) {
            super(v);
            tvEmoji = v.findViewById(R.id.tvEmoji);
            tvName = v.findViewById(R.id.tvName);
            tvRating = v.findViewById(R.id.tvRating);
            tvPrice = v.findViewById(R.id.tvPrice);
            imgFav = v.findViewById(R.id.imgFav);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Product p = list.get(position);
        h.tvEmoji.setText(p.emoji);
        h.tvName.setText(p.name);
        h.tvRating.setText("★ " + p.rating);
        h.tvPrice.setText(Utils.formatPrice(p.price));
        h.imgFav.setImageResource(Utils.isFav(ctx, p.id)
                ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);

        // bấm vào thẻ -> xem chi tiết
        h.itemView.setOnClickListener(v -> listener.onClick(p));

        // bấm vào tim -> thích / bỏ thích
        h.imgFav.setOnClickListener(v -> {
            Utils.toggleFav(ctx, p.id);
            int pos = h.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) notifyItemChanged(pos);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
