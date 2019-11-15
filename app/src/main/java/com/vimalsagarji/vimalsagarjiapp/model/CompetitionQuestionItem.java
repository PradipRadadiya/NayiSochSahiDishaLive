package com.vimalsagarji.vimalsagarjiapp.model;

@SuppressWarnings("ALL")
public class CompetitionQuestionItem {
    private String id,question,qtype,competition_id,answer,options;
    private boolean isSelected = false;
    private int checkedId;

    public CompetitionQuestionItem(String id, String question, String qtype, String competition_id, String answer, String options, boolean isSelected, int checkedId) {
        this.id = id;
        this.question = question;
        this.qtype = qtype;
        this.competition_id = competition_id;
        this.answer = answer;
        this.options = options;
        this.isSelected = isSelected;
        this.checkedId = checkedId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(String competition_id) {
        this.competition_id = competition_id;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(int checkedId) {
        this.checkedId = checkedId;
    }


    @Override
    public String toString() {
        return "CompetitionQuestionItem{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", qtype='" + qtype + '\'' +
                ", competition_id='" + competition_id + '\'' +
                ", answer='" + answer + '\'' +
                ", options='" + options + '\'' +
                ", isSelected=" + isSelected +
                ", checkedId=" + checkedId +
                '}';
    }
}
