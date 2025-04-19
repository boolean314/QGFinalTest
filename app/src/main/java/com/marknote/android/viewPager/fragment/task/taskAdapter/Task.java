package com.marknote.android.viewPager.fragment.task.taskAdapter;

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

    public String getEndTime() {
        return endTime;
    }

    public String getTaskContent() {
        return  taskContent;
    }
}
