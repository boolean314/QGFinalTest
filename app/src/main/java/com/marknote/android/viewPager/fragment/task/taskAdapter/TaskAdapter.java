package com.marknote.android.viewPager.fragment.task.taskAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.marknote.android.R;
import com.marknote.android.SQLiteDatabase.SQLiteUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> mTaskList;
    private boolean[] isSelected; // 跟踪每个项的选中状态
    private boolean isMultiSelectMode = false; // 是否处于多选模式

    public TaskAdapter(List<Task> taskList) {
        mTaskList = taskList;
        isSelected = new boolean[mTaskList.size()]; // 初始化选中状态数组
    }

    public void updateData(List<Task> newData) {
        this.mTaskList = newData != null ? newData : new ArrayList<>();
        isSelected = new boolean[mTaskList.size()]; // 更新选中状态数组
        notifyDataSetChanged();
    }

    public void enterMultiSelectMode() {
        isMultiSelectMode = true;
        notifyDataSetChanged();
    }

    public void exitMultiSelectMode() {
        isMultiSelectMode = false;
        notifyDataSetChanged();
    }

    public boolean isMultiSelectMode() {
        return isMultiSelectMode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mTaskList.get(position);
        holder.startTime.setText(task.getStartTime());
        holder.endTime.setText(task.getEndTime());
        holder.taskContent.setText(task.getTaskContent());
        holder.checkBox.setChecked(isSelected[position]);
        holder.checkBox.setVisibility(isMultiSelectMode ? View.VISIBLE : View.GONE); // 根据多选模式显示或隐藏CheckBox

        // 设置CheckBox的点击事件监听器
        holder.checkBox.setOnClickListener(v -> {
            isSelected[position] = holder.checkBox.isChecked();
            notifyDataSetChanged(); // 通知适配器数据集变化
        });
        // 设置整个Item的点击事件监听器
        holder.itemView.setOnClickListener(v -> {
            // 显示对话框进行编辑
            showEditDialog(holder.itemView.getContext(), position);
        });

    }


    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    public List<Task> getSelectedItems() {
        List<Task> selectedItems = new ArrayList<>();
        for (int i = 0; i < mTaskList.size(); i++) {
            if (isSelected[i]) {
                selectedItems.add(mTaskList.get(i));
            }
        }
        return selectedItems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBox;
        private final TextView startTime;
        private final TextView endTime;
        private final TextView taskContent;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.task_checkbox);
            startTime = view.findViewById(R.id.start_time);
            endTime = view.findViewById(R.id.end_time);
            taskContent = view.findViewById(R.id.task_content);
        }
    }
    private void showEditDialog(Context context, int position) {
        Task task = mTaskList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("编辑任务");

        // 使用LayoutInflater加载对话框布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_task, null);
        builder.setView(dialogView);

        final EditText taskContentEditText = dialogView.findViewById(R.id.edit_task_content);
        final EditText startTimeEditText = dialogView.findViewById(R.id.edit_start_time);
        final EditText endTimeEditText = dialogView.findViewById(R.id.edit_end_time);

        taskContentEditText.setText(task.getTaskContent());
        startTimeEditText.setText(task.getStartTime());
        endTimeEditText.setText(task.getEndTime());

        builder.setPositiveButton("保存", (dialog, which) -> {
            String newTaskContent = taskContentEditText.getText().toString();
            String newStartTime = startTimeEditText.getText().toString();
            String newEndTime = endTimeEditText.getText().toString();
            task.setTaskContent(newTaskContent);
            task.setStartTime(newStartTime);
            task.setEndTime(newEndTime);
            SQLiteUtil.updateTask("Task", task.getTaskContent(), newTaskContent, newStartTime, newEndTime);
            notifyDataSetChanged();
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

}

