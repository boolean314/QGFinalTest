package com.marknote.android.viewPager.fragment.Bill.clickmenu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClickQueryBillActivity extends AppCompatActivity {
  private Button chooseStartTime,chooseEndTime,query,clearTime;
  private TextView startTime,endTime;
private EditText comment,category;
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
        chooseStartTime = findViewById(R.id.bill_choose_start_time);
        chooseEndTime= findViewById(R.id.bill_choose_end_time);
        query = findViewById(R.id.query_time_btn);
        clearTime = findViewById(R.id.clear_time_btn);
        startTime = findViewById(R.id.bill_start_time);
        endTime = findViewById(R.id.bill_end_time);
        comment = findViewById(R.id.query_comment);
        category = findViewById(R.id.query_category);

        billList = new ArrayList<>();
        initListener();


    }

    private void initListener() {
       chooseStartTime.setOnClickListener(this::onClickChooseStartTime);
       chooseEndTime.setOnClickListener(this::onClickChooseEndTime);
       query.setOnClickListener(this::onClickQuery);
       clearTime.setOnClickListener(this::onClickClearTime);
    }

private void onClickChooseStartTime(View view) {
    showPicker(startTime);
}

private void onClickChooseEndTime(View view) {
    showPicker(endTime);
}


    private void onClickQuery(View view) {
        String startTimeStr = startTime.getText().toString();
        String endTimeStr = endTime.getText().toString();
        String commentStr = comment.getText().toString();
        String categoryStr = category.getText().toString();

        // 检查是否至少输入了一个条件
        if (startTimeStr.isEmpty() && endTimeStr.isEmpty() && commentStr.isEmpty() && categoryStr.isEmpty()) {
            Toast.makeText(this, "请输入至少一个查询条件", Toast.LENGTH_SHORT).show();
            billList.clear();
            BillAdapter billAdapter = new BillAdapter(billList);
            RecyclerView recyclerView = findViewById(R.id.query_bill_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            return;
        }

        // 如果只输入了起始时间，则查询当天的所有收支记录
        if (!startTimeStr.isEmpty() && endTimeStr.isEmpty()) {
            // 将起始时间设置为当天的开始时间（00:00:00）
            startTimeStr = startTimeStr.substring(0, 10) + " 00:00:00";
            endTimeStr = startTimeStr.substring(0, 10) + " 23:59:59";
            Toast.makeText(this,"已为您查询当天账单",Toast.LENGTH_SHORT).show();
        }

        // 调用 SQLiteUtil 查询数据库
        Cursor cursor = SQLiteUtil.queryBill(startTimeStr, endTimeStr, commentStr, categoryStr);

        // 清空之前的列表数据
        billList.clear();
if(cursor!=null&&cursor.getCount()>0){
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String money = cursor.getString(cursor.getColumnIndex("money"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            String comment = cursor.getString(cursor.getColumnIndex("comment"));

            Bill bill = new Bill(time,money,type,comment);
            billList.add(bill);
        }

        // 更新 RecyclerView 的适配器
        BillAdapter billAdapter = new BillAdapter(billList);
        RecyclerView recyclerView = findViewById(R.id.query_bill_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(billAdapter);
}
else{
    Toast.makeText(this, "没有查询到相关记录", Toast.LENGTH_SHORT).show();
    billList.clear();
    BillAdapter billAdapter = new BillAdapter(billList);
    RecyclerView recyclerView = findViewById(R.id.query_bill_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(billAdapter);
}
    }

    private void onClickClearTime(View view) {
        startTime.setText("");
        endTime.setText("");
    }

    private void showPicker(TextView text){
        // 获取当前日期和时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        // 创建日期选择器对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                AlertDialog.THEME_HOLO_LIGHT, // 使用 Holo Light 主题(spinner)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 创建时间选择器对话框
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                ClickQueryBillActivity.this,
                                AlertDialog.THEME_HOLO_LIGHT, // 使用 Holo Light 主题
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // 更新 TextView 的文本
                                        String formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute);
                                        text.setText(formattedDate);
                                    }
                                },
                                hour,
                                minute,
                                true // 使用 24 小时制
                        );
                        timePickerDialog.show();
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

}