package com.marknote.android.viewPager.fragment.Bill.clickmenu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.marknote.android.viewPager.fragment.task.clickmenu.ClickInsertTaskActivity;

import java.util.Calendar;
import java.util.Locale;

public class ClickInsertBillActivity extends AppCompatActivity {
    private EditText money, comment;
    private Button chooseTime, finishInsert;
    private Spinner type, category;
    private TextView time;

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
        chooseTime = findViewById(R.id.set_time);
        time = findViewById(R.id.bill_time);
        money = findViewById(R.id.set_money);
        type = findViewById(R.id.set_type);
        category = findViewById(R.id.set_category);
        comment = findViewById(R.id.set_comment);
        finishInsert = findViewById(R.id.finish_insert_bill);
        initListener();
    }

    private void initListener() {
        finishInsert.setOnClickListener(this::onClickFinish);
        chooseTime.setOnClickListener(this::onClickChooseTime);
    }

    private void onClickFinish(View view) {
        if (!time.getText().toString().isEmpty() && !money.getText().toString().isEmpty()) {

            SQLiteUtil.insertBill("Bill", time.getText().toString(), money.getText().toString(), type.getSelectedItem().toString(), comment.getText().toString(), category.getSelectedItem().toString());
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickChooseTime(View view) {
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
                //  设置 OnDateSetListener，当用户选择日期后，会触发 onDateSet 方法。
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 创建时间选择器对话框
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                ClickInsertBillActivity.this,
                                AlertDialog.THEME_HOLO_LIGHT, // 使用 Holo Light 主题
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // 更新 TextView 的文本
                                        String formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute);
                                        time.setText(formattedDate);
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