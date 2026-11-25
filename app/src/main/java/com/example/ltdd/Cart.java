package com.example.ltdd;

import java.util.ArrayList;

// Giỏ hàng dùng chung cho mọi màn hình (biến static)
public class Cart {

    public static class Item {
        public Product product;
        public int qty;

        public Item(Product product, int qty) {
            this.product = product;
            this.qty = qty;
        }
    }

    public static final ArrayList<Item> items = new ArrayList<>();

    public static void add(Product p, int qty) {
        for (Item it : items) {
            if (it.product.id == p.id) {   // đã có thì cộng dồn số lượng
                it.qty += qty;
                return;
            }
        }
        items.add(new Item(p, qty));
    }

    public static long getTotal() {
        long total = 0;
        for (Item it : items) total += it.product.price * it.qty;
        return total;
    }

    public static int getCount() {
        int n = 0;
        for (Item it : items) n += it.qty;
        return n;
    }

    public static void clear() {
        items.clear();
    }
}
