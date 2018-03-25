package com.br.tcc.assistants;

/**
 * Created by Victor on 3/25/2018.
 */

public class TaskModel {
            String id_task;
            String id_user;
            String title;
            String subject;
            String description;
            String estimated_time;
            String deadline;
            String progress;

    public TaskModel(String id_task, String id_user, String title, String subject, String description, String estimated_time, String deadline, String progress) {
        this.id_task = id_task;
        this.id_user = id_user;
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.estimated_time = estimated_time;
        this.deadline = deadline;
        this.progress = progress;
    }

    public String getId_task() {
        return id_task;
    }

    public void setId_task(String id_task) {
        this.id_task = id_task;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstimated_time() {
        return estimated_time;
    }

    public void setEstimated_time(String estimated_time) {
        this.estimated_time = estimated_time;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id_task='" + id_task + '\'' +
                ", id_user='" + id_user + '\'' +
                ", title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", estimated_time='" + estimated_time + '\'' +
                ", deadline='" + deadline + '\'' +
                ", progress='" + progress + '\'' +
                '}';
    }
}
