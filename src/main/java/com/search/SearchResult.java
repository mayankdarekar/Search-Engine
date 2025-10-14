package com.search;

public class SearchResult {
    private String title;
    private String link;
    private String snippet;
    
    public SearchResult(String title, String link, String snippet) {
        this.title = title;
        this.link = link;
        this.snippet = snippet;
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    
    public String getSnippet() { return snippet; }
    public void setSnippet(String snippet) { this.snippet = snippet; }
}

