package com.vimalsagarji.vimalsagarjiapp.model;

public class ThoughtItem {
    private String ID;
    private String Title;
    private String Description;
    private String Date;
    private String View;
    private String Photo;
    private String location;
    private String viewed_user;
    private String is_viewed;


    public ThoughtItem(String ID, String title, String description, String date, String view, String photo, String location, String viewed_user, String is_viewed) {
        this.ID = ID;
        Title = title;
        Description = description;
        Date = date;
        View = view;
        Photo = photo;
        this.location = location;
        this.viewed_user = viewed_user;
        this.is_viewed = is_viewed;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getView() {
        return View;
    }

    public void setView(String view) {
        View = view;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getViewed_user() {
        return viewed_user;
    }

    public void setViewed_user(String viewed_user) {
        this.viewed_user = viewed_user;
    }

    public String getIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(String is_viewed) {
        this.is_viewed = is_viewed;
    }
}
