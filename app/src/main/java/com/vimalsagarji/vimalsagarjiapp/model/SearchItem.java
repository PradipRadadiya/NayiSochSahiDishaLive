package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class SearchItem {

    private String table, title, id, date, description;

    public SearchItem(String table, String title, String id, String date, String description) {
        this.table = table;
        this.title = title;
        this.id = id;
        this.date = date;
        this.description = description;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
