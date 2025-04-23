package com.marknote.android.viewPager.fragment.task.clickmenu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.marknote.android.viewPager.fragment.Bill.billAdapter.BillAdapter;
import com.marknote.android.viewPager.fragment.task.taskAdapter.Task;
import com.marknote.android.viewPager.fragment.task.taskAdapter.TaskAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClickQueryTaskActivity extends AppCompatActivity {
    private Button chooseTime, queryDay, queryMonth;
    private TextView date;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_click_query_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        chooseTime = findViewById(R.id.choose_time_btn);
        queryDay = findViewById(R.id.query_day);
        queryMonth = findViewById(R.id.query_month);
        date = findViewById(R.id.choose_time_text);
        taskList = new ArrayList<>();

        initListener();
    }

    private void initListener() {
        chooseTime.setOnClickListener(this::onClickChooseTime);
        queryDay.setOnClickListener(this::onClickQueryDay);
        queryMonth.setOnClickListener(this::onClickQueryMonth);
    }

    private void onClickChooseTime(View view) {
        // 获取当前日期和时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        // 创建日期选择器对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                AlertDialog.THEME_HOLO_LIGHT, // 使用 Holo Light 主题(spinner)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 更新 TextView 的文本
                        String formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                        date.setText(formattedDate);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private void onClickQueryDay(View view) {
        String time = date.getText().toString();

        if (!time.isEmpty()) {
            taskList.clear();
            Cursor cursor = SQLiteUtil.queryDayTask("Task", time);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String taskContent = cursor.getString(cursor.getColumnIndex("content"));
                    String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                    String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                    Task task = new Task(startTime, endTime, taskContent);
                    taskList.add(task);

                }
                cursor.close();
                // 设置 RecyclerView 的适配器
                RecyclerView recyclerView = findViewById(R.id.query_task_recycler_view);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                TaskAdapter adapter = new TaskAdapter(taskList);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "当日无事项", Toast.LENGTH_SHORT).show();
                taskList.clear();
                RecyclerView recyclerView = findViewById(R.id.query_task_recycler_view);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                TaskAdapter adapter = new TaskAdapter(taskList);
                recyclerView.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, "请选择查询日期", Toast.LENGTH_SHORT).show();

        }
    }

    private void onClickQueryMonth(View view) {
        String time = date.getText().toString();

        if (!time.isEmpty()) {
            taskList.clear();
            Cursor cursor = SQLiteUtil.queryMonthTask("Task", time);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String taskContent = cursor.getString(cursor.getColumnIndex("content"));
                    String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                    String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                    Task task = new Task(startTime, endTime, taskContent);
                    taskList.add(task);

                }
                cursor.close();
                // 设置 RecyclerView 的适配器
                RecyclerView recyclerView = findViewById(R.id.query_task_recycler_view);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                TaskAdapter adapter = new TaskAdapter(taskList);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "当月无事项", Toast.LENGTH_SHORT).show();
                taskList.clear();
                RecyclerView recyclerView = findViewById(R.id.query_task_recycler_view);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                TaskAdapter adapter = new TaskAdapter(taskList);
                recyclerView.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, "请选择查询日期", Toast.LENGTH_SHORT).show();
        }

    }
}
