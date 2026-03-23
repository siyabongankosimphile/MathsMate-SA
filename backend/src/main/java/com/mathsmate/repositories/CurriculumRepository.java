package com.mathsmate.repositories;

import com.mathsmate.models.Curriculum;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface CurriculumRepository extends MongoRepository<Curriculum, String> {
    List<Curriculum> findByGrade(String grade);
    Optional<Curriculum> findByTopicIgnoreCase(String topic);
    List<Curriculum> findByTopicContainingIgnoreCase(String keyword);
}
