package com.example.ltdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

// SQLite: bảng users (tài khoản) và orders (đơn hàng)
public class DatabaseHelper extends SQLiteOpenHelper {

    public static class Order {
        public int id;
        public String items, date;
        public long total;
    }

    public DatabaseHelper(Context context) {
        super(context, "shopapp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (username TEXT PRIMARY KEY, password TEXT, fullname TEXT)");
        db.execSQL("CREATE TABLE orders (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT, items TEXT, total INTEGER, date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS orders");
        onCreate(db);
    }

    // ===== TÀI KHOẢN =====
    public boolean userExists(String user) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT username FROM users WHERE username=?", new String[]{user});
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }

    public boolean register(String user, String pass, String fullName) {
        if (userExists(user)) return false;
        ContentValues v = new ContentValues();
        v.put("username", user);
        v.put("password", pass);
        v.put("fullname", fullName);
        return getWritableDatabase().insert("users", null, v) != -1;
    }

    public boolean login(String user, String pass) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT username FROM users WHERE username=? AND password=?",
                new String[]{user, pass});
        boolean ok = c.getCount() > 0;
        c.close();
        return ok;
    }

    public String getFullName(String user) {
        String name = user;
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT fullname FROM users WHERE username=?", new String[]{user});
        if (c.moveToFirst()) name = c.getString(0);
        c.close();
        return name;
    }

    public void updateName(String user, String fullName) {
        ContentValues v = new ContentValues();
        v.put("fullname", fullName);
        getWritableDatabase().update("users", v, "username=?", new String[]{user});
    }

    public boolean changePassword(String user, String oldPass, String newPass) {
        if (!login(user, oldPass)) return false;   // mật khẩu cũ sai
        ContentValues v = new ContentValues();
        v.put("password", newPass);
        getWritableDatabase().update("users", v, "username=?", new String[]{user});
        return true;
    }

    // ===== ĐƠN HÀNG =====
    public void insertOrder(String user, String items, long total, String date) {
        ContentValues v = new ContentValues();
        v.put("username", user);
        v.put("items", items);
        v.put("total", total);
        v.put("date", date);
        getWritableDatabase().insert("orders", null, v);
    }

    public ArrayList<Order> getOrders(String user) {
        ArrayList<Order> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, items, total, date FROM orders WHERE username=? ORDER BY id DESC",
                new String[]{user});
        while (c.moveToNext()) {
            Order o = new Order();
            o.id = c.getInt(0);
            o.items = c.getString(1);
            o.total = c.getLong(2);
            o.date = c.getString(3);
            list.add(o);
        }
        c.close();
        return list;
    }

    public int countOrders(String user) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id FROM orders WHERE username=?", new String[]{user});
        int n = c.getCount();
        c.close();
        return n;
    }
}
