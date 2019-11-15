package com.vimalsagarji.vimalsagarjiapp.model;

public class VideoItem {

    private String ID, VideoName, CategoryID, Video, Photo, Duration, Date, View, Description, video_link, viewed_user, Name, is_viewed;

    public VideoItem(String ID, String videoName, String categoryID, String video, String photo, String duration, String date, String view, String description, String video_link, String viewed_user, String name, String is_viewed) {
        this.ID = ID;
        this.VideoName = videoName;
        this.CategoryID = categoryID;
        this.Video = video;
        this.Photo = photo;
        this.Duration = duration;
        this.Date = date;
        this.View = view;
        this.Description = description;
        this.video_link = video_link;
        this.viewed_user = viewed_user;
        this.Name = name;
        this.is_viewed = is_viewed;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
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
        return View;
    }

    public void setView(String view) {
        View = view;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getViewed_user() {
        return viewed_user;
    }

    public void setViewed_user(String viewed_user) {
        this.viewed_user = viewed_user;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(String is_viewed) {
        this.is_viewed = is_viewed;
    }
}
