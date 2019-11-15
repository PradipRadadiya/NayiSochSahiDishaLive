package com.vimalsagarji.vimalsagarjiapp.model;

public class VideoAllItem {

    private String id, title, video, videolink;

    public VideoAllItem(String id, String title, String video, String videolink) {
        this.id = id;
        this.title = title;
        this.video = video;
        this.videolink = videolink;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }
}
