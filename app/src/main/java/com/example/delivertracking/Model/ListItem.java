package com.example.delivertracking.Model;

public class ListItem {
    int id;
    String code;
    String time;

    public ListItem(int id, String code, String time) {
        this.id = id;
        this.code = code;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTime() {
        return time;
    }
}
