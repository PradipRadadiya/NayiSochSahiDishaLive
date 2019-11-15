package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by Grapes-Pradip on 10-Oct-17.
 */

public class PhotoAudioVideoItem {

    String url, name, specification;

    public PhotoAudioVideoItem(String url, String name, String specification) {
        this.url = url;
        this.name = name;
        this.specification = specification;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }
}
