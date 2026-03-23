package com.mathsmate.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document("curriculum")
public class Curriculum {

    @Id
    private String id;
    private String topic;
    private String grade;
    private String description;
    private List<String> subtopics;
    private String capsReference;

    public Curriculum() {}

    public Curriculum(String topic, String grade, String description, List<String> subtopics, String capsReference) {
        this.topic = topic;
        this.grade = grade;
        this.description = description;
        this.subtopics = subtopics;
        this.capsReference = capsReference;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getSubtopics() { return subtopics; }
    public void setSubtopics(List<String> subtopics) { this.subtopics = subtopics; }

    public String getCapsReference() { return capsReference; }
    public void setCapsReference(String capsReference) { this.capsReference = capsReference; }
}
