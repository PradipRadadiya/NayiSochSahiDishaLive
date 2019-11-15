package com.vimalsagarji.vimalsagarjiapp.model;

@SuppressWarnings("ALL")
public class QuestionAllItem {
    private String ID;
    private String Question;
    private String Answer;
    private String Date;
    private String Is_Approved;
    private String UserID;
    private String View;
    private String viewed_user;
    private String Name;
    private String is_viewed;

    public QuestionAllItem(String ID, String question, String answer, String date, String is_Approved, String userID, String view, String viewed_user, String name, String is_viewed) {
        this.ID = ID;
        Question = question;
        Answer = answer;
        Date = date;
        Is_Approved = is_Approved;
        UserID = userID;
        View = view;
        this.viewed_user = viewed_user;
        Name = name;
        this.is_viewed = is_viewed;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
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

    public String getView() {
        return View;
    }

    public void setView(String view) {
        View = view;
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
