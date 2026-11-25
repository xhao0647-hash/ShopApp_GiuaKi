package com.example.ltdd;

import java.util.ArrayList;

public class Product {
    public final int id;
    public final String category, name, emoji, description;
    public final long price;
    public final float rating;

    public Product(int id, String category, String name, String emoji,
                   long price, float rating, String description) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.emoji = emoji;
        this.price = price;
        this.rating = rating;
        this.description = description;
    }

    // Dữ liệu mẫu
    public static final ArrayList<Product> LIST = new ArrayList<>();

    static {
        LIST.add(new Product(1, "Điện thoại", "iPhone 15 Pro Max", "📱", 29500000, 4.8f,
                "Chip A17 Pro, khung titan, camera 48MP, màn hình Super Retina 6.7 inch."));
        LIST.add(new Product(2, "Điện thoại", "Samsung Galaxy S24 Ultra", "📱", 31000000, 4.7f,
                "Snapdragon 8 Gen 3, bút S Pen, camera 200MP, màn hình 6.8 inch."));
        LIST.add(new Product(3, "Điện thoại", "Xiaomi 14 Ultra", "📱", 22000000, 4.5f,
                "Camera Leica, Snapdragon 8 Gen 3, sạc nhanh 90W, pin 5000mAh."));
        LIST.add(new Product(4, "Điện thoại", "OPPO Find X7", "📱", 18000000, 4.4f,
                "Màn hình AMOLED 120Hz, camera zoom quang học, sạc nhanh 100W."));
        LIST.add(new Product(5, "Laptop", "Dell XPS 15", "💻", 45000000, 4.6f,
                "Core i7, RAM 16GB, SSD 512GB, màn hình OLED 15.6 inch."));
        LIST.add(new Product(6, "Laptop", "MacBook Air M2", "💻", 27000000, 4.9f,
                "Chip Apple M2, RAM 8GB, SSD 256GB, pin dùng cả ngày, mỏng nhẹ."));
        LIST.add(new Product(7, "Laptop", "Asus ROG Strix G16", "💻", 38000000, 4.5f,
                "Core i9, RTX 4060, RAM 16GB, màn hình 165Hz cho game thủ."));
        LIST.add(new Product(8, "Phụ kiện", "Tai nghe AirPods Pro 2", "🎧", 5500000, 4.8f,
                "Chống ồn chủ động, chip H2, pin 6 giờ, hộp sạc MagSafe."));
        LIST.add(new Product(9, "Phụ kiện", "Apple Watch Series 9", "⌚", 10000000, 4.7f,
                "Chip S9, đo nhịp tim, đo oxy trong máu, màn hình luôn sáng."));
        LIST.add(new Product(10, "Phụ kiện", "Chuột Logitech MX Master 3", "🖱️", 2400000, 4.6f,
                "Chuột không dây công thái học, cuộn siêu nhanh, kết nối 3 thiết bị."));
        LIST.add(new Product(11, "Phụ kiện", "Bàn phím cơ Keychron K2", "⌨️", 2200000, 4.5f,
                "Bàn phím cơ không dây, switch Gateron, đèn nền RGB, pin 4000mAh."));
        LIST.add(new Product(12, "Phụ kiện", "Sạc dự phòng Anker 20000mAh", "🔋", 1100000, 4.4f,
                "Dung lượng 20000mAh, sạc nhanh 65W, sạc được cả laptop."));
    }

    // Tìm sản phẩm theo id
    public static Product find(int id) {
        for (Product p : LIST) {
            if (p.id == id) return p;
        }
        return LIST.get(0);
    }
}
