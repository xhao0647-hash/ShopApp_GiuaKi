package com.example.ltdd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

// Tài khoản: sửa họ tên, đổi mật khẩu, xem thống kê, đăng xuất
public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvUser, tvOrderCount, tvCartCount;
    TextInputLayout tilName, tilOld, tilNew;
    Button btnSave, btnChangePass, btnOrders, btnLogout;
    DatabaseHelper db;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Utils.applyInsets(findViewById(R.id.root));
        Utils.setupToolbar(this, "Tài khoản");

        db = new DatabaseHelper(this);
        user = Utils.getUser(this);

        tvName = findViewById(R.id.tvName);
        tvUser = findViewById(R.id.tvUser);
        tvOrderCount = findViewById(R.id.tvOrderCount);
        tvCartCount = findViewById(R.id.tvCartCount);
        tilName = findViewById(R.id.tilName);
        tilOld = findViewById(R.id.tilOld);
        tilNew = findViewById(R.id.tilNew);
        btnSave = findViewById(R.id.btnSave);
        btnChangePass = findViewById(R.id.btnChangePass);
        btnOrders = findViewById(R.id.btnOrders);
        btnLogout = findViewById(R.id.btnLogout);

        String name = db.getFullName(user);
        tvName.setText(name);
        tvUser.setText("@" + user);
        tilName.getEditText().setText(name);

        // ----- Lưu họ tên -----
        btnSave.setOnClickListener(v -> {
            String newName = Utils.getText(tilName);
            if (newName.isEmpty()) {
                tilName.setError("Vui lòng nhập họ tên");
                return;
            }
            tilName.setError(null);
            db.updateName(user, newName);
            tvName.setText(newName);
            Snackbar.make(findViewById(R.id.root), "Đã lưu thông tin", Snackbar.LENGTH_SHORT).show();
        });

        // ----- Đổi mật khẩu -----
        btnChangePass.setOnClickListener(v -> {
            String oldPass = Utils.getText(tilOld);
            String newPass = Utils.getText(tilNew);
            tilOld.setError(null);
            tilNew.setError(null);

            if (newPass.length() < 3) {
                tilNew.setError("Mật khẩu mới tối thiểu 3 ký tự");
            } else if (!db.changePassword(user, oldPass, newPass)) {
                tilOld.setError("Mật khẩu cũ không đúng");
            } else {
                tilOld.getEditText().setText("");
                tilNew.getEditText().setText("");
                Snackbar.make(findViewById(R.id.root), "Đổi mật khẩu thành công", Snackbar.LENGTH_SHORT).show();
            }
        });

        btnOrders.setOnClickListener(v -> startActivity(new Intent(this, OrdersActivity.class)));
        btnLogout.setOnClickListener(v -> Utils.logout(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvOrderCount.setText(String.valueOf(db.countOrders(user)));
        tvCartCount.setText(String.valueOf(Cart.getCount()));
    }
}
