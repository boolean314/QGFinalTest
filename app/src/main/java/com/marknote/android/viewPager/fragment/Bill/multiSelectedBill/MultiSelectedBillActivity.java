package com.marknote.android.viewPager.fragment.Bill.multiSelectedBill;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

public class MultiSelectedBillActivity extends AppCompatActivity {
    private List<Bill> billList = new ArrayList<>();
    private Button delete;
    private Button edit;
    private MultiSelectedBillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multi_selected_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        delete = findViewById(R.id.bill_delete);
        edit = findViewById(R.id.bill_edit);

        initBill();
        initListener();

    }

    private void initBill() {
        Cursor cursor = SQLiteUtil.queryBill("Bill");
        billList.clear(); // 清空旧数据
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
            Bill bill = new Bill(timeStr, money, type, comment);
            billList.add(bill);
        }
        cursor.close();
        RecyclerView recyclerView = findViewById(R.id.multi_bill_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MultiSelectedBillAdapter(billList);
        recyclerView.setAdapter(adapter);

    }

    private void initListener() {
        delete.setOnClickListener(this::onClickDelete);
        edit.setOnClickListener(this::onClickEdit);
    }

    private void onClickDelete(View view) {
        List<Bill> selectedBills = adapter.getSelectedBills();
        if (!selectedBills.isEmpty()) {
            for (Bill selectedBill : selectedBills) {
                String time = selectedBill.getTime();
                String type = selectedBill.getType();
                String money = selectedBill.getMoney();
                String comment=selectedBill.getComment();

                SQLiteUtil.deleteBill("Bill", "time=? and type=? and money=? and comment=?", new String[]{time, type, money,comment});
            }
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "请选择要删除的账单", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickEdit(View view) {
        List<Bill> selectedBills = adapter.getSelectedBills();
        if (selectedBills.size() == 1) {
            Bill selectedBill = selectedBills.get(0);
            String time = selectedBill.getTime();
            String type = selectedBill.getType();
            String money = selectedBill.getMoney();
            String comment = selectedBill.getComment();
            Intent intent = new Intent(this, EditBillActivity.class);
            intent.putExtra("time", time);
            intent.putExtra("type", type);
            intent.putExtra("money", money);
            intent.putExtra("comment", comment);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "请选择一个账单进行编辑", Toast.LENGTH_SHORT).show();
        }
    }

}