package com.br.tcc.assistants;

import java.util.Date;

/**
 * Created by Victor on 3/31/2018.
 */

public class Recomendation {
    String taskTitle;
    String subject;
    long startTime, endTime, deadline;
    double progress;
    boolean group;

    public Recomendation() {
    }

    public Recomendation(String taskTitle, String subject, long startTime, long endTime, long deadline, double progress, boolean group) {
        this.taskTitle = taskTitle;
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
        this.progress = progress;
        this.group = group;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }
}
