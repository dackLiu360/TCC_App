package com.br.tcc.assistants;

/**
 * Created by Victor on 3/26/2018.
 */

public class TimeModelInitial {
    String id_user;
    String initialTime;
    String finalTime;
    String date;

    public TimeModelInitial(String id_user, String year, String month, String day, String initialTime, String finalTime) {
        this.id_user = id_user;
        if(Integer.parseInt(month)>=1&&Integer.parseInt(month)<=9){
            month = "0"+month;
        }
        if(Integer.parseInt(day)>=1&&Integer.parseInt(day)<=9){
            day = "0"+day;
        }


        date=year+"-"+month+"-"+day;
        this.initialTime = initialTime+ ":00";
        this.finalTime = finalTime+ ":00";
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(String initialTime) {
        this.initialTime = initialTime;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return id_user+","+date+","+initialTime+","+finalTime;
    }
}
