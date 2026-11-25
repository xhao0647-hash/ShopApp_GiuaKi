package com.example.ltdd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;

// Màn hình đăng nhập
public class MainActivity extends AppCompatActivity {

    TextInputLayout tilUser, tilPass;
    MaterialCheckBox cbRemember;
    Button btnLogin, btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Đã chọn "Ghi nhớ đăng nhập" từ lần trước -> vào thẳng trang chủ
        if (Utils.isRemembered(this)) {
            goHome();
            return;
        }

        setContentView(R.layout.activity_main);
        Utils.applyInsets(findViewById(R.id.root));

        db = new DatabaseHelper(this);
        tilUser = findViewById(R.id.tilUser);
        tilPass = findViewById(R.id.tilPass);
        cbRemember = findViewById(R.id.cbRemember);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            String user = Utils.getText(tilUser);
            String pass = Utils.getText(tilPass);
            tilUser.setError(null);
            tilPass.setError(null);

            if (user.isEmpty()) {
                tilUser.setError("Vui lòng nhập tên đăng nhập");
                return;
            }
            if (pass.isEmpty()) {
                tilPass.setError("Vui lòng nhập mật khẩu");
                return;
            }

            if (db.login(user, pass)) {
                Utils.saveSession(this, user, cbRemember.isChecked());
                goHome();
            } else {
                tilPass.setError("Sai tên đăng nhập hoặc mật khẩu");
            }
        });

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void goHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
