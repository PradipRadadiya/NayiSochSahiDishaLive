package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by GrapesA1-PC on 10/19/2016.
 */

@SuppressWarnings("ALL")
class AudioGet {

    private String id;
    private String audioname;
    private String categoryid;
    private String audio;
    private String photo;
    private String duration;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudioname() {
        return audioname;
    }

    public void setAudioname(String audioname) {
        this.audioname = audioname;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
