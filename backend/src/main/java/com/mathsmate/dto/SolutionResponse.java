package com.mathsmate.dto;

import java.util.List;

public class SolutionResponse {

    private String answer;
    private List<String> steps;
    private String method;
    private String topic;
    private boolean fromCache;

    public SolutionResponse() {}

    public SolutionResponse(String answer, List<String> steps, String method, String topic) {
        this.answer = answer;
        this.steps = steps;
        this.method = method;
        this.topic = topic;
    }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public List<String> getSteps() { return steps; }
    public void setSteps(List<String> steps) { this.steps = steps; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public boolean isFromCache() { return fromCache; }
    public void setFromCache(boolean fromCache) { this.fromCache = fromCache; }
}
