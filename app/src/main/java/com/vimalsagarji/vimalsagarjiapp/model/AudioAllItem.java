package com.vimalsagarji.vimalsagarjiapp.model;

public class AudioAllItem {

    private String title, audio;

    public AudioAllItem(String title, String audio) {
        this.title = title;
        this.audio = audio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
