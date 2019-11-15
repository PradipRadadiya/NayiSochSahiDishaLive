package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by GrapesA1-PC on 11/9/2016.
 */

@SuppressWarnings("ALL")
public class ThisMonthVideo {
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getIs_Approved() {
        return Is_Approved;
    }

    public void setIs_Approved(String is_Approved) {
        Is_Approved = is_Approved;
    }

    private String videoID;
    private String Comment;
    private String Date;
    private String UserID;
    private String Is_Approved;

}
