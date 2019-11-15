package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by GrapesA1-PC on 11/10/2016.
 */

@SuppressWarnings("ALL")
public class CompetitionQuestion {
    private String ID;
    private String Question;
    private String QType;
    private String CategoryID;
    private String Name;
    private String Options;
    private String Answer;
    private String status;

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

    public String getQType() {
        return QType;
    }

    public void setQType(String QType) {
        this.QType = QType;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOptions() {
        return Options;
    }

    public void setOptions(String options) {
        Options = options;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
