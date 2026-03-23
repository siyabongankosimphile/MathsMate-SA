package com.mathsmate.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Document("users")
public class User {

    @Id
    private String id;

    @Email
    @NotBlank
    private String email;

    private String name;
    private String grade;
    private int solvedProblems = 0;
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public int getSolvedProblems() { return solvedProblems; }
    public void setSolvedProblems(int solvedProblems) { this.solvedProblems = solvedProblems; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
