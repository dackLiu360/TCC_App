package com.br.tcc.assistants;

/**
 * Created by Victor on 3/25/2018.
 */

public class TimeBlockModel {

    String id_time_block;
    String id_time;
    String time_start;
    String time_end;
    String part;
    String availability;
    public TimeBlockModel(String id_time_block, String id_time, String time_start, String time_end, String part, String availability) {
        this.id_time_block = id_time_block;
        this.id_time = id_time;
        this.time_start = time_start;
        this.time_end = time_end;
        this.part = part;
        this.availability = availability;
    }

    public String getId_time_block() {
        return id_time_block;
    }

    public void setId_time_block(String id_time_block) {
        this.id_time_block = id_time_block;
    }

    public String getId_time() {
        return id_time;
    }

    public void setId_time(String id_time) {
        this.id_time = id_time;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    @Override
    public String toString(){
        return time_start+"-"+time_end;
    }
}
