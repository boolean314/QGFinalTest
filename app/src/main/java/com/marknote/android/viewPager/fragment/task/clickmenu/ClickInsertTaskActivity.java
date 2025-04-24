package com.marknote.android.viewPager.fragment.task.clickmenu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.marknote.android.R;
import com.marknote.android.SQLiteDatabase.SQLiteUtil;

import java.util.Calendar;
import java.util.Locale;

public class ClickInsertTaskActivity extends AppCompatActivity {
    private Button chooseStartTime, chooseEndTime, finishInsert;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private TextView insertStartTime, insertEndTime;
    private EditText insertTaskContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_click_insert_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        chooseStartTime = findViewById(R.id.choose_start_time);
        chooseEndTime = findViewById(R.id.choose_end_time);
        finishInsert = findViewById(R.id.finish_insert_task);
        insertStartTime = findViewById(R.id.insert_start_time);
        insertEndTime = findViewById(R.id.insert_end_time);
        insertTaskContent = findViewById(R.id.insert_task_content);
        initListener();
    }

    private void initListener() {
        chooseStartTime.setOnClickListener(this::onClickChooseStartTime);
        chooseEndTime.setOnClickListener(this::onClickChooseEndTime);
        finishInsert.setOnClickListener(this::onClickFinishInsert);
    }

    private void onClickChooseStartTime(View view) {
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
                                ClickInsertTaskActivity.this,
                                AlertDialog.THEME_HOLO_LIGHT, // 使用 Holo Light 主题
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // 更新 TextView 的文本
                                        String formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute);
                                        insertStartTime.setText(formattedDate);
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

    private void onClickChooseEndTime(View view) {
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
                                ClickInsertTaskActivity.this,
                                AlertDialog.THEME_HOLO_LIGHT, // 使用 Holo Light 主题
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // 更新 TextView 的文本
                                        String formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute);
                                        insertEndTime.setText(formattedDate);
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

    private void onClickFinishInsert(View view) {
        String startTime = insertStartTime.getText().toString();
        String endTime = insertEndTime.getText().toString();
        String taskContent = insertTaskContent.getText().toString();
        if (!startTime.isEmpty() && !endTime.isEmpty() && !taskContent.isEmpty()) {
            SQLiteUtil.insertTask("Task", startTime, endTime, taskContent);
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "请输入完整事项", Toast.LENGTH_SHORT).show();
        }
    }
}
