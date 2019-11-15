package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by BHARAT on 24/02/2016.
 */
@SuppressWarnings("ALL")
public class EventComment {
    private String ID;
    private String EventID;
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

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
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
