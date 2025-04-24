package com.marknote.android.viewPager.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marknote.android.R;
import com.marknote.android.SQLiteDatabase.SQLiteUtil;
import com.marknote.android.viewPager.fragment.Bill.billAdapter.Bill;
import com.marknote.android.viewPager.fragment.Bill.billAdapter.BillAdapter;
import com.marknote.android.viewPager.fragment.Bill.clickmenu.ClickIncomeBillActivity;
import com.marknote.android.viewPager.fragment.Bill.clickmenu.ClickInsertBillActivity;
import com.marknote.android.viewPager.fragment.Bill.clickmenu.ClickPayBillActivity;
import com.marknote.android.viewPager.fragment.Bill.clickmenu.ClickQueryBillActivity;
import com.marknote.android.viewPager.fragment.Bill.multiSelectedBill.MultiSelectedBillActivity;

import java.util.ArrayList;
import java.util.List;

public class BillFragment extends Fragment {
    private View view;
    private List<Bill> billList = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    // 创建视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 使用布局文件fragment_weather.xml填充视图

        view = inflater.inflate(R.layout.fragment_bill, container, false);
        toolbar = view.findViewById(R.id.bill_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        RecyclerView recyclerView = view.findViewById(R.id.bill_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BillAdapter billAdapter = new BillAdapter(billList);
        recyclerView.setAdapter(billAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bill_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pay) {
            Intent intent = new Intent(getContext(), ClickPayBillActivity.class);
            startActivity(intent);
        } else if (id == R.id.income) {
            Intent intent = new Intent(getContext(), ClickIncomeBillActivity.class);
            startActivity(intent);
        } else if (id == R.id.bill_query) {
            Intent intent = new Intent(getContext(), ClickQueryBillActivity.class);
            startActivity(intent);
        } else if (id == R.id.bill_insert) {
            Intent intent = new Intent(getContext(), ClickInsertBillActivity.class);
            startActivity(intent);
        }
        return true;

    }


    @Override
    public void onResume() {
        super.onResume();
        initBill();
    }

    private void initBill() {
        toolbar = view.findViewById(R.id.bill_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
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
        RecyclerView recyclerView = view.findViewById(R.id.bill_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        BillAdapter adapter = new BillAdapter(billList);
        recyclerView.setAdapter(adapter);

        // 添加ItemTouchListener长按触发
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(
                    getContext(),
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public void onLongPress(MotionEvent e) {
                            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (childView != null) {
                                // 长按跳转到 MultiSelectedBillActivity
                                // 跳转时添加 FLAG 避免重复创建
                                Intent intent = new Intent(getContext(), MultiSelectedBillActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // 关键标志
                                startActivity(intent);
                            }
                        }
                    }
            );

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                gestureDetector.onTouchEvent(e); // 将触摸事件交给 GestureDetector 处理
                return false; //返回 false 表示不拦截事件，RecyclerView 会继续处理触摸事件，例如滚动列表。
            }
        });
    }



}
