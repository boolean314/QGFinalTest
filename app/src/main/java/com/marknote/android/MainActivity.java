package com.marknote.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.marknote.android.SQLiteDatabase.SQLiteUtil;
import com.marknote.android.viewPager.HomeActivity;
import com.marknote.android.register.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    private EditText accountEdit;
    private EditText passwordEdit;
    private CheckBox rememberPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SQLiteUtil.init(this);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rememberPassword = findViewById(R.id.remember_password);
        initListener();
        String account = SQLiteUtil.getRememberAccount("ACCOUNTANDPASSWORD");
        String password = SQLiteUtil.getRememberPassword("ACCOUNTANDPASSWORD");
        if (account != null && password != null) {
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPassword.setChecked(true);
        }


    }

    private void initListener() {
        login.setOnClickListener(this::onClickLogin);
        register.setOnClickListener(this::onClickRegister);
    }

    //点击登录
    private void onClickLogin(View view) {

        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (!account.isEmpty() && !password.isEmpty()) {
            if (SQLiteUtil.isMatch("ACCOUNTANDPASSWORD", account, password)) {
                boolean result = rememberPassword.isChecked();
                if (result) {
                    SQLiteUtil.setRemember("ACCOUNTANDPASSWORD", account);

                } else {
                    SQLiteUtil.setNotRemember("ACCOUNTANDPASSWORD", account);
                }
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();


            }else{
                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    //点击注册
    private void onClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }
}