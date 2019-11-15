package com.vimalsagarji.vimalsagarjiapp.common;

/**
 * Created by Grapes-Pradip on 06-Oct-17.
 */

public class CommentsList {

    private String comment;
    private String isApproved;
    private String userID;
    private String dataID;
    private String iD;
    private String date;
    private String name;

    public CommentsList(String comment, String isApproved, String userID, String dataID, String iD, String date, String name) {
        this.comment = comment;
        this.isApproved = isApproved;
        this.userID = userID;
        this.dataID = dataID;
        this.iD = iD;
        this.date = date;
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
