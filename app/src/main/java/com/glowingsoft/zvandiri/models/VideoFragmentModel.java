package com.glowingsoft.zvandiri.models;

public class VideoFragmentModel {
    public String getVideoId() {
        return videoId;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }


    public VideoFragmentModel(String videoId, String publishedAt, String title, String url) {
        this.videoId = videoId;
        this.publishedAt = publishedAt;
        this.title = title;
        this.url = url;
    }

    String videoId, publishedAt, title, url, description;
}