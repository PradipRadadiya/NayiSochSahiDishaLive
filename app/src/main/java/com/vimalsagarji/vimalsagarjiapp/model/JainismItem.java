package com.vimalsagarji.vimalsagarjiapp.model;

import java.io.Serializable;

public class JainismItem implements Serializable {

    private String id, title, description, date, audio, countLike, countComment, countViews;

    public JainismItem(String id, String title, String description, String date, String audio, String countLike, String countComment, String countViews) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.audio = audio;
        this.countLike = countLike;
        this.countComment = countComment;
        this.countViews = countViews;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getCountLike() {
        return countLike;
    }

    public void setCountLike(String countLike) {
        this.countLike = countLike;
    }

    public String getCountComment() {
        return countComment;
    }

    public void setCountComment(String countComment) {
        this.countComment = countComment;
    }

    public String getCountViews() {
        return countViews;
    }

    public void setCountViews(String countViews) {
        this.countViews = countViews;
    }
}
