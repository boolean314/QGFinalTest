package com.marknote.android.viewPager.fragment.Bill.clickmenu;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.marknote.android.R;
import com.marknote.android.SQLiteDatabase.SQLiteUtil;
import com.marknote.android.viewPager.fragment.Bill.billAdapter.Bill;
import com.marknote.android.viewPager.fragment.Bill.billAdapter.BillAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClickPayBillActivity extends AppCompatActivity {

    private BarChart barChart;
    private PieChart pieChart;
    private RecyclerView recyclerView;
    private BillAdapter billAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_click_pay_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        barChart = findViewById(R.id.pay_barChart);
        pieChart = findViewById(R.id.pay_pieChart);
        recyclerView = findViewById(R.id.pay_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(billAdapter = new BillAdapter(SQLiteUtil.getPaysForToday()));

        RadioGroup radioGroup = findViewById(R.id.pay_radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.pay_radio_day) {
                updateChartsForDay();
                billAdapter.updateBills(SQLiteUtil.getPaysForToday());
            } else if (checkedId == R.id.pay_radio_week) {
                updateChartsForWeek();
                billAdapter.updateBills(SQLiteUtil.getPaysForThisWeek());
            } else if (checkedId == R.id.pay_radio_month) {
                updateChartsForMonth();
                billAdapter.updateBills(SQLiteUtil.getPaysForThisMonth());
            }
        });

        // 默认选择日视图
        radioGroup.check(R.id.pay_radio_day);
        updateChartsForDay();
        billAdapter.updateBills(SQLiteUtil.getPaysForToday());
    }

    private void updateChartsForDay() {
        List<Bill> bills = SQLiteUtil.getPaysForToday();
        List<BarEntry> barEntries = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();

        for (Bill bill : bills) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bill.getTime());
                double amount = Double.parseDouble(bill.getMoney());
                barEntries.add(new BarEntry((float) date.getTime(), (float) amount));
                pieEntries.add(new PieEntry((float) amount, bill.getCategory()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "支出");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "支出比例");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void updateChartsForWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        Date oneWeekAgo = calendar.getTime();

        List<BarEntry> barEntries = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();

        for (Bill bill : SQLiteUtil.getPaysForThisWeek()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bill.getTime());
                if (date.after(oneWeekAgo)) {
                    double amount = Double.parseDouble(bill.getMoney());
                    barEntries.add(new BarEntry((float) date.getTime(), (float) amount));
                    pieEntries.add(new PieEntry((float) amount, bill.getCategory()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "支出");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "支出比例");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void updateChartsForMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date oneMonthAgo = calendar.getTime();

        List<BarEntry> barEntries = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();

        for (Bill bill : SQLiteUtil.getPaysForThisMonth()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bill.getTime());
                if (date.after(oneMonthAgo)) {
                    double amount = Double.parseDouble(bill.getMoney());
                    barEntries.add(new BarEntry((float) date.getTime(), (float) amount));
                    pieEntries.add(new PieEntry((float) amount, bill.getCategory()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "支出");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "支出比例");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}