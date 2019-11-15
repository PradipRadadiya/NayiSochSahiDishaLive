package com.vimalsagarji.vimalsagarjiapp.activity.competition;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompetitionWinner {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total_competition")
    @Expose
    private String totalCompetition;
    @SerializedName("total_marks")
    @Expose
    private String totalMarks;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String getTotalCompetition() {
        return totalCompetition;
    }

    public void setTotalCompetition(String totalCompetition) {
        this.totalCompetition = totalCompetition;
    }

    String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Phone")
        @Expose
        private String phone;
        @SerializedName("Total_attented_competition")
        @Expose
        private Integer totalAttentedCompetition;
        @SerializedName("Total_received_marks")
        @Expose
        private String totalReceivedMarks;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        Integer getTotalAttentedCompetition() {
            return totalAttentedCompetition;
        }

        public void setTotalAttentedCompetition(Integer totalAttentedCompetition) {
            this.totalAttentedCompetition = totalAttentedCompetition;
        }

        String getTotalReceivedMarks() {
            return totalReceivedMarks;
        }

        public void setTotalReceivedMarks(String totalReceivedMarks) {
            this.totalReceivedMarks = totalReceivedMarks;
        }

    }

}


