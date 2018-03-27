package com.br.tcc.assistants;

/**
 * Created by Victor on 3/26/2018.
 */

public class TimeModel {
    String id_time;
    String id_user;
    String day;

    public TimeModel(String id_time, String id_user, String day) {
        this.id_time = id_time;
        this.id_user = id_user;
        this.day = day;
    }

    public String getId_time() {
        return id_time;
    }

    public void setId_time(String id_time) {
        this.id_time = id_time;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
