package com.mathsmate.controllers;

import com.mathsmate.models.Curriculum;
import com.mathsmate.repositories.CurriculumRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {

    private final CurriculumRepository curriculumRepository;

    public CurriculumController(CurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }

    @GetMapping("/health")
    public String health() {
        return "CurriculumController OK";
    }

    /** Return all curriculum entries (optionally by grade). */
    @GetMapping
    public ResponseEntity<List<Curriculum>> getAll(
            @RequestParam(required = false) String grade) {
        if (grade != null) {
            return ResponseEntity.ok(curriculumRepository.findByGrade(grade));
        }
        return ResponseEntity.ok(curriculumRepository.findAll());
    }

    /** Look up a specific topic. */
    @GetMapping("/{topic}")
    public ResponseEntity<Curriculum> getByTopic(@PathVariable String topic) {
        return curriculumRepository.findByTopicIgnoreCase(topic)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Search curriculum by keyword. */
    @GetMapping("/search")
    public ResponseEntity<List<Curriculum>> search(@RequestParam String q) {
        return ResponseEntity.ok(curriculumRepository.findByTopicContainingIgnoreCase(q));
    }
}
