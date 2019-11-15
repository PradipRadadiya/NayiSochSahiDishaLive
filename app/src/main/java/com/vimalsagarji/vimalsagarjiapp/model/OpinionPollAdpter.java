package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by BHARAT on 21/02/2016.
 */
@SuppressWarnings("ALL")
public class OpinionPollAdpter {
    private String ID;
    private String Ques;
    private String total_polls;
    private String yes_polls;
    private String no_polls;
    private String flag;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQues() {
        return Ques;
    }

    public void setQues(String ques) {
        Ques = ques;
    }

    public String getTotal_polls() {
        return total_polls;
    }

    public void setTotal_polls(String total_polls) {
        this.total_polls = total_polls;
    }

    public String getYes_polls() {
        return yes_polls;
    }

    public void setYes_polls(String yes_polls) {
        this.yes_polls = yes_polls;
    }

    public String getNo_polls() {
        return no_polls;
    }

    public void setNo_polls(String no_polls) {
        this.no_polls = no_polls;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}


