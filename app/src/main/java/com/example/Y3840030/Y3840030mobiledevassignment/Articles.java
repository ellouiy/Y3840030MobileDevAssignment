package com.example.Y3840030.Y3840030mobiledevassignment;


import java.util.List;

public class Articles {
    private int count;
    private int page;
    private int pages;
    private List<ArticleItem> results;


    public List<ArticleItem> getResults() {
        return results;
    }

    public void setResults(List<ArticleItem> eventItems) {
        this.results = eventItems;
    }

}