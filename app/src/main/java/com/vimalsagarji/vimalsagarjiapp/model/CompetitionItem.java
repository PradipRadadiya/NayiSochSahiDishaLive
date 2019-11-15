package com.vimalsagarji.vimalsagarjiapp.model;

@SuppressWarnings("ALL")
public class CompetitionItem {
    private String id;
    private String title;
    private String rules;
    private String date;
    private String time;
    private String total_question;
    private String total_minute;
    private String is_open;
    private String participated_users;
    private boolean isSelected = false;
    private String status;
    private String description;


    public CompetitionItem(String id, String title, String rules, String date, String time, String total_question, String total_minute, String is_open, String participated_users, boolean isSelected, String status,String description) {
        this.id = id;
        this.title = title;
        this.rules = rules;
        this.date = date;
        this.time = time;
        this.total_question = total_question;
        this.total_minute = total_minute;
        this.is_open = is_open;
        this.participated_users = participated_users;
        this.isSelected = isSelected;
        this.status = status;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal_question() {
        return total_question;
    }

    public void setTotal_question(String total_question) {
        this.total_question = total_question;
    }

    public String getTotal_minute() {
        return total_minute;
    }

    public void setTotal_minute(String total_minute) {
        this.total_minute = total_minute;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getParticipated_users() {
        return participated_users;
    }

    public void setParticipated_users(String participated_users) {
        this.participated_users = participated_users;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
