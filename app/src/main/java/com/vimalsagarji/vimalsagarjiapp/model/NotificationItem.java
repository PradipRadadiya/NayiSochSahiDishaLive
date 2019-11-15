package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class NotificationItem {
    private String table, id, title, description, date,nid,is_viewed;

    public NotificationItem(String table, String id, String title, String description, String date,String nid,String is_viewed) {
        this.table = table;
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.nid=nid;
        this.is_viewed=is_viewed;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(String is_viewed) {
        this.is_viewed = is_viewed;
    }
}
