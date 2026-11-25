package com.example.ltdd;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

// Màn hình đăng ký
public class RegisterActivity extends AppCompatActivity {

    TextInputLayout tilName, tilUser, tilPass, tilConfirm;
    Button btnSubmit;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Utils.applyInsets(findViewById(R.id.root));
        Utils.setupToolbar(this, "Đăng ký");

        db = new DatabaseHelper(this);
        tilName = findViewById(R.id.tilName);
        tilUser = findViewById(R.id.tilUser);
        tilPass = findViewById(R.id.tilPass);
        tilConfirm = findViewById(R.id.tilConfirm);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            String name = Utils.getText(tilName);
            String user = Utils.getText(tilUser);
            String pass = Utils.getText(tilPass);
            String confirm = Utils.getText(tilConfirm);

            tilName.setError(null);
            tilUser.setError(null);
            tilPass.setError(null);
            tilConfirm.setError(null);

            if (name.isEmpty()) {
                tilName.setError("Vui lòng nhập họ tên");
            } else if (user.isEmpty()) {
                tilUser.setError("Vui lòng nhập tên đăng nhập");
            } else if (pass.length() < 3) {
                tilPass.setError("Mật khẩu tối thiểu 3 ký tự");
            } else if (!pass.equals(confirm)) {
                tilConfirm.setError("Mật khẩu nhập lại không khớp");
            } else if (!db.register(user, pass, name)) {
                tilUser.setError("Tên đăng nhập đã tồn tại");
            } else {
                Toast.makeText(this, "Đăng ký thành công! Hãy đăng nhập.", Toast.LENGTH_LONG).show();
                finish();   // quay lại trang đăng nhập
            }
        });
    }
}
