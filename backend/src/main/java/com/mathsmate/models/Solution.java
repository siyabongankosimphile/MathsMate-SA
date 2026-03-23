package com.mathsmate.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document("solutions")
public class Solution {

    @Id
    private String id;
    private String problemId;
    private String method;
    private String answer;
    private List<String> steps;
    private String topic;
    private LocalDateTime solvedAt = LocalDateTime.now();

    public Solution() {}

    public Solution(String answer, List<String> steps, String method, String topic) {
        this.answer = answer;
        this.steps = steps;
        this.method = method;
        this.topic = topic;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProblemId() { return problemId; }
    public void setProblemId(String problemId) { this.problemId = problemId; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public List<String> getSteps() { return steps; }
    public void setSteps(List<String> steps) { this.steps = steps; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public LocalDateTime getSolvedAt() { return solvedAt; }
    public void setSolvedAt(LocalDateTime solvedAt) { this.solvedAt = solvedAt; }
}
