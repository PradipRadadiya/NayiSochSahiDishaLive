package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by GrapesA1-PC on 11/4/2016.
 */

@SuppressWarnings("ALL")
class EventInformation {
    private String ID;
    private String Title;
    private String Description;
    private String Address;
    private String Date;
    private String Audio;
    private String AudioImage;
    private String VideoLink;
    private String Video;
    private String VideoImage;
    private String Photo;

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

    public String getAudio() {
        return Audio;
    }

    public void setAudio(String audio) {
        Audio = audio;
    }

    public String getAudioImage() {
        return AudioImage;
    }

    public void setAudioImage(String audioImage) {
        AudioImage = audioImage;
    }

    public String getVideoLink() {
        return VideoLink;
    }

    public void setVideoLink(String videoLink) {
        VideoLink = videoLink;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getVideoImage() {
        return VideoImage;
    }

    public void setVideoImage(String videoImage) {
        VideoImage = videoImage;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
