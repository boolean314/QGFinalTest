package com.marknote.android.viewPager.fragment.Bill.clickmenu;

import android.database.Cursor;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marknote.android.R;
import com.marknote.android.SQLiteDatabase.SQLiteUtil;
import com.marknote.android.viewPager.fragment.Bill.billAdapter.Bill;
import com.marknote.android.viewPager.fragment.Bill.billAdapter.BillAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClickQueryBillActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private List<Bill> billList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_click_query_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editText = findViewById(R.id.query_time_edit);
        button = findViewById(R.id.query_time_btn);
        billList = new ArrayList<>();
        initListener();


    }

    private void initListener() {
        button.setOnClickListener(this::onClickButton);
    }

    private void onClickButton(View view) {
        String time = editText.getText().toString();
        if (time.isEmpty()) {Toast.makeText(this,"请输入要查询的时间（段）",Toast.LENGTH_SHORT).show();}

        else{
        Cursor cursor = checkTime(time);
        if (cursor != null&& cursor.getCount() > 0) {
            billList.clear(); // 清空之前的账单列表
            while (cursor.moveToNext()) {
                // 提取每一列的数据

                String timeStr = cursor.getString(cursor.getColumnIndex("time"));
                String money = cursor.getString(cursor.getColumnIndex("money"));
String type = cursor.getString(cursor.getColumnIndex("type"));

                int commentIndex = cursor.getColumnIndex("comment");
                String comment = "";
                if (commentIndex != -1) {
                    comment = cursor.getString(commentIndex);
                    if (comment == null) {
                        comment = ""; // 如果字段为空，设置默认值
                    }
                }

                // 将数据封装为 Bill 对象
                Bill bill = new Bill(timeStr, money,type,comment);
                billList.add(bill);
            }
            cursor.close(); // 关闭 Cursor

            // 设置 RecyclerView 的适配器
            RecyclerView recyclerView = findViewById(R.id.query_bill_recycler_view);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);

            BillAdapter adapter = new BillAdapter(billList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "该时间（段）无收支", Toast.LENGTH_SHORT).show();
        }}
    }


    private Cursor checkTime(String time) {
        if (time.contains(" ")) {
            // 分割字符串，获取开始时间和结束时间
            String[] times = time.split(" ");
            if (times.length != 4) {
               Toast.makeText(this,"请输入正确的时间段",Toast.LENGTH_SHORT).show();
                return null;
            }
else{
            // 构造开始时间和结束时间
            String startTime = times[0] + " " + times[1];
            String endTime = times[2] + " " + times[3];

            // 调用查询方法
            return SQLiteUtil.queryBill("Bill", startTime, endTime);}
        } else {
            // 如果没有时间段，直接查询单个时间点
            return SQLiteUtil.queryBill("Bill", time);
        }
    }
}