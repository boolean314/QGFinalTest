package com.marknote.android.viewPager.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marknote.android.R;
import com.marknote.android.SQLiteDatabase.SQLiteUtil;
import com.marknote.android.viewPager.fragment.Bill.multiSelectedBill.MultiSelectedBillActivity;
import com.marknote.android.viewPager.fragment.task.clickmenu.ClickInsertTaskActivity;
import com.marknote.android.viewPager.fragment.task.clickmenu.ClickQueryTaskActivity;
import com.marknote.android.viewPager.fragment.task.taskAdapter.Task;
import com.marknote.android.viewPager.fragment.task.taskAdapter.TaskAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskFragment extends Fragment {
    private View view;
    private Toolbar toolbar;
    private List<Task> waitedTaskList = new ArrayList<>();
    private List<Task> finishedTaskList = new ArrayList<>();
    private Spinner spinner;
    private TaskAdapter taskAdapter;
    private RecyclerView recyclerView;
    private int lastSpinnerPosition;

    @Override
    // 创建视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 使用布局文件fragment_weather.xml填充视图
        view = inflater.inflate(R.layout.fragment_task, container, false);

        toolbar = view.findViewById(R.id.task_toolbar);
        spinner = view.findViewById(R.id.selected_task_status);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        // 给spinner初始化适配器
        String[] data = {"未办", "已结束"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        //给recyclerView 初始化适配器


        return view;
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.task_insert) {
            Intent intent = new Intent(getActivity(), ClickInsertTaskActivity.class);
            startActivity(intent);
        } else if (id == R.id.task_query) {
            Intent intent = new Intent(getActivity(), ClickQueryTaskActivity.class);
            startActivity(intent);
        }
        return true;

    }

    @Override
    public void onResume() {
        super.onResume();
        initTask();
    }

    private void initTask() {
        toolbar = view.findViewById(R.id.task_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        Cursor cursor = SQLiteUtil.queryTask("Task");
        waitedTaskList.clear();
        finishedTaskList.clear();

        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String taskContent = cursor.getString(cursor.getColumnIndex("content"));
                String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                Task task = new Task(startTime, endTime, taskContent);
                //
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                try {
                    Date endTimeDate = dateFormat.parse(endTime);
                    if (currentDate.after(endTimeDate)) {
                        finishedTaskList.add(task);
                    } else {
                        waitedTaskList.add(task);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            cursor.close();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (lastSpinnerPosition == 0) {
            taskAdapter = new TaskAdapter(waitedTaskList);
        } else if (lastSpinnerPosition == 1) {
            taskAdapter.updateData(finishedTaskList); // 使用updateData而不是新建适配器
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 获取选中的项
                String selectedItem = parent.getItemAtPosition(position).toString();
                // 根据选中的项执行不同的操作
                switch (selectedItem) {
                    case "未办":
                        taskAdapter.updateData(waitedTaskList);
                        lastSpinnerPosition = 0;
                        break;
                    case "已结束":
                        taskAdapter.updateData(finishedTaskList);
                        lastSpinnerPosition = 1;
                        break;
                }
                recyclerView.setAdapter(taskAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 未选中任何项时的操作

            }
        });
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
                                Toast.makeText(getContext(), "长按成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                gestureDetector.onTouchEvent(e); // 将触摸事件交给 GestureDetector 处理
                return false; // 不拦截事件，保证 RecyclerView 仍可滚动
            }
        });
    }

}
