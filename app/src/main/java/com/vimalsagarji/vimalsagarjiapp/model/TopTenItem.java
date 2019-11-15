package com.vimalsagarji.vimalsagarjiapp.model;

public class TopTenItem {

    private String user_marks,total_mark,user_id,Name;

    public TopTenItem(String user_marks, String total_mark, String user_id, String name) {
        this.user_marks = user_marks;
        this.total_mark = total_mark;
        this.user_id = user_id;
        Name = name;
    }

    public String getUser_marks() {
        return user_marks;
    }

    public void setUser_marks(String user_marks) {
        this.user_marks = user_marks;
    }

    public String getTotal_mark() {
        return total_mark;
    }

    public void setTotal_mark(String total_mark) {
        this.total_mark = total_mark;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
