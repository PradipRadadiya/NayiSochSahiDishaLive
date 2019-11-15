package com.vimalsagarji.vimalsagarjiapp.model;

public class InformationItem {


    private String ID,Title,Description,Address,Date,View,Photo,viewed_user,is_viewed;

    public InformationItem(String ID, String title, String description, String address, String date, String view, String photo, String viewed_user, String is_viewed) {
        this.ID = ID;
        this.Title = title;
        this.Description = description;
        this.Address = address;
        this.Date = date;
        this.View = view;
        this.Photo = photo;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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
