package com.marknote.android.viewPager.fragment.task.taskAdapter;

import android.widget.CheckBox;

public class Task {
    private String startTime;
    private String endTime;
    private String taskContent;

    public Task(String startTime,String endTime,String taskContent){
        this.startTime=startTime;
        this.endTime=endTime;
        this.taskContent=taskContent;

    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTaskContent() {
        return  taskContent;
    }
}
