package com.marknote.android.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.marknote.android.SQLiteDatabase.MyDatabaseHelper;
import com.marknote.android.R;
import com.marknote.android.SQLiteDatabase.SQLiteUtil;

public class RegisterActivity extends AppCompatActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button finishRegister;
    private MyDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        accountEdit = findViewById(R.id.set_account);
        passwordEdit = findViewById(R.id.set_password);
        finishRegister = findViewById(R.id.finish_register);

        initListener();


    }

    private void initListener() {
        finishRegister.setOnClickListener(this::onClickFinishRegister);
    }

    private void onClickFinishRegister(View view) {

        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (!account.isEmpty() && !password.isEmpty()) {
            if (SQLiteUtil.isAccountExist("ACCOUNTANDPASSWORD",account)) {
                Toast.makeText(this, "该账号已被注册", Toast.LENGTH_SHORT).show();
            }else{
                SQLiteUtil.insertData("AccountAndPassword", account, password);
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }

        } else {
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}