package com.marknote.android.viewPager.fragment.task.taskAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.marknote.android.R;
import com.marknote.android.viewPager.fragment.Bill.billAdapter.BillAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> mTaskList;

    public TaskAdapter(List<Task> taskList) {
        mTaskList = taskList;
    }

    public void updateData(List<Task> newData) {
        this.mTaskList = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView startTime;
        TextView endTime;
        TextView taskContent;

        public ViewHolder(View view) {
            super(view);
            startTime = view.findViewById(R.id.start_time);
            endTime = view.findViewById(R.id.end_time);
            taskContent = view.findViewById(R.id.task_content);
        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_task_item,parent,false);
        TaskAdapter.ViewHolder holder=new TaskAdapter.ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mTaskList.get(position);
        holder.startTime.setText(task.getStartTime());
        holder.endTime.setText(task.getEndTime());
        holder.taskContent.setText(task.getTaskContent());
    }
    @Override
    public int getItemCount() {
        return mTaskList.size();
    }
}
