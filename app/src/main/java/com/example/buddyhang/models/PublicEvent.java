package com.example.buddyhang.models;

public class PublicEvent {

    private String name;
    private String url;
    private String imageUrl;

    public PublicEvent() {
    }

    public PublicEvent(String name, String url, String imageUrl) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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



}
