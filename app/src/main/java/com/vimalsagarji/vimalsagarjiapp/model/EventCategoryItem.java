package com.vimalsagarji.vimalsagarjiapp.model;

public class EventCategoryItem {

    private String id;
    private String name;
    private String categoryicon;
    private String new_event;
    private String module_name;


    public EventCategoryItem(String id, String name, String categoryicon,String new_event,String module_name) {
        this.id = id;
        this.name = name;
        this.categoryicon = categoryicon;
        this.new_event=new_event;
        this.module_name=module_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryicon() {
        return categoryicon;
    }

    public void setCategoryicon(String categoryicon) {
        this.categoryicon = categoryicon;
    }

    public String getNew_event() {
        return new_event;
    }

    public void setNew_event(String new_event) {
        this.new_event = new_event;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }
}
