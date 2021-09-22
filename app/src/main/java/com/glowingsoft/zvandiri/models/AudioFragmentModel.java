package com.glowingsoft.zvandiri.models;

public class AudioFragmentModel {
    String id, title, url;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public AudioFragmentModel(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }
}
