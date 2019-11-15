package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by BHARAT on 22/02/2016.
 */
@SuppressWarnings("ALL")
public class ThisMonthComments {
    private String ID;
    private String InformationID;
    private String Comment;
    private String Date;
    private String UserID;
    private String Is_Approved;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getInformationID() {
        return InformationID;
    }

    public void setInformationID(String informationID) {
        InformationID = informationID;
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
}
