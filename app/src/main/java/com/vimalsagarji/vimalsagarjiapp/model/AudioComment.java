package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by BHARAT on 25/02/2016.
 */
@SuppressWarnings("ALL")
class AudioComment {
    private String ID;
    private String AudioID;
    private String Comment;
    private String Date;
    private String Is_Approved;
    private String UserID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAudioID() {
        return AudioID;
    }

    public void setAudioID(String audioID) {
        AudioID = audioID;
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

    public String getIs_Approved() {
        return Is_Approved;
    }

    public void setIs_Approved(String is_Approved) {
        Is_Approved = is_Approved;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
