package com.andriynev.driver_helper_bot.dto;

public class NewsItem {
    private String photoUrl;
    private String title;
    private String text;
    private String originLink;
    private String category;

    public NewsItem(String title, String text, String photoUrl, String originLink, String category) {
        this.title = title;
        this.text = text;
        this.photoUrl = photoUrl;
        this.originLink = originLink;
        this.category = category;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOriginLink() {
        return originLink;
    }

    public void setOriginLink(String originLink) {
        this.originLink = originLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "photoUrl='" + photoUrl + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", originLink='" + originLink + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
