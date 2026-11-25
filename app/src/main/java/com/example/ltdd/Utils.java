package com.example.ltdd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

// Các hàm dùng chung cho cả app
public class Utils {

    // Chừa chỗ cho thanh trạng thái, thanh điều hướng, bàn phím
    public static void applyInsets(View root) {
        View appbar = root.findViewById(R.id.appbar);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            if (appbar != null) {
                appbar.setPadding(0, b.top, 0, 0);       // thanh trên cùng tô màu luôn phần status bar
                v.setPadding(b.left, 0, b.right, b.bottom);
            } else {
                v.setPadding(b.left, b.top, b.right, b.bottom);
            }
            return insets;
        });
    }

    // Đặt tiêu đề + nút Back góc trái
    public static MaterialToolbar setupToolbar(Activity a, String title) {
        MaterialToolbar tb = a.findViewById(R.id.toolbar);
        tb.setTitle(title);
        tb.setNavigationOnClickListener(v -> a.finish());
        return tb;
    }

    public static String formatPrice(long price) {
        return String.format(Locale.US, "%,d", price).replace(',', '.') + "đ";
    }

    public static String getText(TextInputLayout til) {
        return til.getEditText().getText().toString().trim();
    }

    // ===== SESSION (phiên đăng nhập) lưu trong SharedPreferences "session" =====
    public static void saveSession(Context c, String user, boolean remember) {
        c.getSharedPreferences("session", Context.MODE_PRIVATE).edit()
                .putString("user", user)
                .putBoolean("remember", remember)
                .apply();
    }

    public static String getUser(Context c) {
        return c.getSharedPreferences("session", Context.MODE_PRIVATE).getString("user", "");
    }

    public static boolean isRemembered(Context c) {
        SharedPreferences sp = c.getSharedPreferences("session", Context.MODE_PRIVATE);
        return sp.getBoolean("remember", false) && !sp.getString("user", "").isEmpty();
    }

    public static void logout(Activity a) {
        a.getSharedPreferences("session", Context.MODE_PRIVATE).edit().clear().apply();
        Cart.clear();
        Intent i = new Intent(a, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        a.startActivity(i);
        a.finish();
    }

    // ===== YÊU THÍCH (mỗi tài khoản 1 danh sách riêng) =====
    public static boolean isFav(Context c, int productId) {
        SharedPreferences sp = c.getSharedPreferences("fav_" + getUser(c), Context.MODE_PRIVATE);
        return sp.getStringSet("ids", new HashSet<>()).contains(String.valueOf(productId));
    }

    public static void toggleFav(Context c, int productId) {
        SharedPreferences sp = c.getSharedPreferences("fav_" + getUser(c), Context.MODE_PRIVATE);
        Set<String> set = new HashSet<>(sp.getStringSet("ids", new HashSet<>()));
        String id = String.valueOf(productId);
        if (!set.remove(id)) set.add(id);
        sp.edit().putStringSet("ids", set).apply();
    }
}
