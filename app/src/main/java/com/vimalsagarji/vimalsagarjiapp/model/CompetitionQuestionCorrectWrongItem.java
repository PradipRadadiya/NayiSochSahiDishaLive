package com.vimalsagarji.vimalsagarjiapp.model;

@SuppressWarnings("ALL")
public class CompetitionQuestionCorrectWrongItem {

    private String question,qid,true_answer,competition_id,Name,user_id,answer,options;


    public CompetitionQuestionCorrectWrongItem(String question, String qid, String true_answer, String competition_id, String name, String user_id, String answer, String options) {
        this.question = question;
        this.qid = qid;
        this.true_answer = true_answer;
        this.competition_id = competition_id;
        Name = name;
        this.user_id = user_id;
        this.answer = answer;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getTrue_answer() {
        return true_answer;
    }

    public void setTrue_answer(String true_answer) {
        this.true_answer = true_answer;
    }

    public String getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(String competition_id) {
        this.competition_id = competition_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
