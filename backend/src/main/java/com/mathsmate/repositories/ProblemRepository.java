package com.mathsmate.repositories;

import com.mathsmate.models.Problem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProblemRepository extends MongoRepository<Problem, String> {
    List<Problem> findByTopic(String topic);
    List<Problem> findByGrade(String grade);
    List<Problem> findByTopicAndGrade(String topic, String grade);
    List<Problem> findByQuestionContainingIgnoreCase(String keyword);
}
