package com.mathsmate.dto;

import jakarta.validation.constraints.NotBlank;

public class ProblemRequest {

    @NotBlank(message = "Question is required")
    private String question;

    private String imageBase64;
    private String grade;
    private String topic;

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
}
