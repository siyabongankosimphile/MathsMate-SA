package com.mathsmate.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.TextIndexed;
import java.time.LocalDateTime;
import java.util.List;

@Document("problems")
public class Problem {

    @Id
    private String id;

    @TextIndexed
    private String question;

    private String topic;
    private String grade;
    private String difficulty;
    private String imageUrl;
    private String normalizedQuestion;
    private LocalDateTime createdAt = LocalDateTime.now();
    private List<String> tags;

    public Problem() {}

    public Problem(String question) {
        this.question = question;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getNormalizedQuestion() { return normalizedQuestion; }
    public void setNormalizedQuestion(String normalizedQuestion) { this.normalizedQuestion = normalizedQuestion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
