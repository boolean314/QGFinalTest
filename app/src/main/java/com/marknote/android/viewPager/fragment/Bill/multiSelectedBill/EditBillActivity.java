package com.marknote.android.viewPager.fragment.Bill.multiSelectedBill;

import android.content.Intent;
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

import com.marknote.android.R;
import com.marknote.android.SQLiteDatabase.SQLiteUtil;

public class EditBillActivity extends AppCompatActivity {
    private EditText editTime, editMoney, editType, editComment;
    private Button button;
    private String time, money, type, comment;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTime = findViewById(R.id.edit_time);
        editMoney = findViewById(R.id.edit_money);
        editType = findViewById(R.id.edit_type);
        editComment = findViewById(R.id.edit_comment);
        button = findViewById(R.id.finish_edit_bill);
        Intent intent = getIntent();
        time = intent.getStringExtra("time");
        money = intent.getStringExtra("money");
        type = intent.getStringExtra("type");
        comment = intent.getStringExtra("comment");
        editTime.setText(time);
        editMoney.setText(money);
        editType.setText(type);
        editComment.setText(comment);
        initListener();
        id = SQLiteUtil.checkBillId("Bill", time, money, type, comment);
    }

    private void initListener() {
        button.setOnClickListener(this::onClickButton);
    }

    private void onClickButton(View view) {
        time = editTime.getText().toString();
        money = editMoney.getText().toString();
        type = editType.getText().toString();
        comment = editComment.getText().toString();
        SQLiteUtil.updateBill("Bill", id, time, money, type, comment);
        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        finish();
    }

}