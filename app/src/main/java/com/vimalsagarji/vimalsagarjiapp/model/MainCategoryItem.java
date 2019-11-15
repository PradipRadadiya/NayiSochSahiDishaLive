package com.vimalsagarji.vimalsagarjiapp.model;

public class MainCategoryItem {

    private String cat_id, id, name, image;

    public MainCategoryItem(String cat_id, String id, String name, String image) {
        this.cat_id = cat_id;
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
