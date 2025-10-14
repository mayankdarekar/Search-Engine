package com.search;

public class AnswerCard {
    private String question;
    private String answer;
    private String category;
    private String imageUrl;
    
    public AnswerCard(String question, String answer, String category, String imageUrl) {
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.imageUrl = imageUrl;
    }
    
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
