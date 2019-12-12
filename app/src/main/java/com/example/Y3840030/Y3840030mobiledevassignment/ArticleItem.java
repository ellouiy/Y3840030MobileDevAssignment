package com.example.Y3840030.Y3840030mobiledevassignment;

import java.util.List;


public class ArticleItem {
    private String body;
    private String title;
    private List<TextItem> source;
    private String date;
    private String url;
    private String image;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TextItem> getSource() {
        return source;
    }

    public void setSource(List<TextItem> source) {
        this.source = source;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}