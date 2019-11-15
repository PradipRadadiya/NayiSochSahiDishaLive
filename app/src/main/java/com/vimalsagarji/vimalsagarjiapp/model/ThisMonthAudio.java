package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by GrapesA1-PC on 11/14/2016.
 */

@SuppressWarnings("ALL")
public class ThisMonthAudio {

    private String ID;
    private String AudioName;
    private String CategoryID;
    private String Audio;
    private String Photo;
    private String Duration;
    private String Date;
    private String view;
    private String flag;

    public ThisMonthAudio(String ID, String audioName, String categoryID, String audio, String photo, String duration, String date,String view,String flag) {
        this.ID = ID;
        AudioName = audioName;
        CategoryID = categoryID;
        Audio = audio;
        Photo = photo;
        Duration = duration;
        Date = date;
        this.view=view;
        this.flag=flag;
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAudioName() {
        return AudioName;
    }

    public void setAudioName(String audioName) {
        AudioName = audioName;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getAudio() {
        return Audio;
    }

    public void setAudio(String audio) {
        Audio = audio;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
