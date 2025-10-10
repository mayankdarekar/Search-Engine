package com.search;

public class Document {
    private String name;
    private String content;
    private double score;
    
    public Document(String name, String content) {
        this.name = name;
        this.content = content;
        this.score = 0.0;
    }
    
    public String getName() { return name; }
    public String getContent() { return content; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    
    public String getPreview() {
        return content.length() <= 200 ? content : content.substring(0, 200) + "...";
    }
}
