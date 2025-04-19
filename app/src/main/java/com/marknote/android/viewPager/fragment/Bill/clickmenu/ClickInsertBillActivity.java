package com.marknote.android.viewPager.fragment.Bill.clickmenu;

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

public class ClickInsertBillActivity extends AppCompatActivity {
private EditText time,money,type,comment;
private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_click_insert_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        time=findViewById(R.id.set_time);
        money=findViewById(R.id.set_money);
        type=findViewById(R.id.set_type);
        comment=findViewById(R.id.set_comment);
        button=findViewById(R.id.finish_insert_bill);
        initListener();
    }
    private void initListener(){
        button.setOnClickListener(this::onClickButton);
    }
    private void onClickButton(View view){
        if(!time.getText().toString().isEmpty()&&!money.getText().toString().isEmpty()&&!type.getText().toString().isEmpty()){
            if(time.getText().toString().contains(" ")){
        SQLiteUtil.insertBill("Bill",time.getText().toString(),money.getText().toString(),type.getText().toString(),comment.getText().toString());
        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
        finish();}else{
            Toast.makeText(this,"请输入正确的时间格式",Toast.LENGTH_SHORT).show();
            }}
        else{
            Toast.makeText(this,"请填写完整信息",Toast.LENGTH_SHORT).show();
        }
    }
}