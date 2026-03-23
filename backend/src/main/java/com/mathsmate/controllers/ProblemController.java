package com.mathsmate.controllers;

import com.mathsmate.dto.ProblemRequest;
import com.mathsmate.dto.SolutionResponse;
import com.mathsmate.models.Problem;
import com.mathsmate.repositories.ProblemRepository;
import com.mathsmate.services.OCRService;
import com.mathsmate.services.SolutionService;
import com.mathsmate.services.VectorSearchService;
import com.mathsmate.utils.TextPreprocessor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemRepository problemRepository;
    private final SolutionService solutionService;
    private final OCRService ocrService;
    private final VectorSearchService vectorSearchService;

    public ProblemController(ProblemRepository problemRepository,
                             SolutionService solutionService,
                             OCRService ocrService,
                             VectorSearchService vectorSearchService) {
        this.problemRepository = problemRepository;
        this.solutionService = solutionService;
        this.ocrService = ocrService;
        this.vectorSearchService = vectorSearchService;
    }

    @GetMapping("/health")
    public String health() {
        return "ProblemController OK";
    }

    /** List all problems (optionally filtered by topic/grade). */
    @GetMapping
    public ResponseEntity<List<Problem>> getAll(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String grade) {
        List<Problem> problems;
        if (topic != null && grade != null) {
            problems = problemRepository.findByTopicAndGrade(topic, grade);
        } else if (topic != null) {
            problems = problemRepository.findByTopic(topic);
        } else if (grade != null) {
            problems = problemRepository.findByGrade(grade);
        } else {
            problems = problemRepository.findAll();
        }
        return ResponseEntity.ok(problems);
    }

    /** Get a single problem by id. */
    @GetMapping("/{id}")
    public ResponseEntity<Problem> getById(@PathVariable String id) {
        return problemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Submit a problem (text or base-64 image) and receive a solution. */
    @PostMapping("/solve")
    public ResponseEntity<SolutionResponse> solve(@Valid @RequestBody ProblemRequest request) {
        String question = request.getQuestion() != null ? request.getQuestion() : "";

        // OCR fallback: if question is blank but image provided, try OCR
        if (question.isBlank() && request.getImageBase64() != null && !request.getImageBase64().isBlank()) {
            byte[] imageBytes = Base64.getDecoder().decode(request.getImageBase64());
            question = ocrService.extractText(imageBytes);
        }

        if (question.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new SolutionResponse("No problem text could be extracted.", List.of(), "none", ""));
        }

        // Persist the problem
        Problem problem = new Problem(question);
        problem.setGrade(request.getGrade());
        problem.setTopic(request.getTopic());
        problem.setNormalizedQuestion(TextPreprocessor.preprocess(question));
        problemRepository.save(problem);

        SolutionResponse response = solutionService.solve(question);
        return ResponseEntity.ok(response);
    }

    /** Upload an image file for OCR then solve. */
    @PostMapping("/solve/image")
    public ResponseEntity<SolutionResponse> solveImage(@RequestParam("file") MultipartFile file) {
        try {
            String question = ocrService.extractText(file.getBytes());
            if (question.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new SolutionResponse("OCR extracted no text from the image.", List.of(), "none", ""));
            }
            Problem problem = new Problem(question);
            problem.setNormalizedQuestion(TextPreprocessor.preprocess(question));
            problemRepository.save(problem);
            return ResponseEntity.ok(solutionService.solve(question));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SolutionResponse("Image processing failed: " + e.getMessage(), List.of(), "none", ""));
        }
    }

    /** Search for problems by keyword. */
    @GetMapping("/search")
    public ResponseEntity<List<Problem>> search(@RequestParam String q) {
        return ResponseEntity.ok(problemRepository.findByQuestionContainingIgnoreCase(q));
    }

    /** Find problems similar to a given query. */
    @GetMapping("/similar")
    public ResponseEntity<List<Problem>> similar(@RequestParam String q) {
        return ResponseEntity.ok(vectorSearchService.findSimilar(q));
    }

    /** Delete a problem. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!problemRepository.existsById(id)) return ResponseEntity.notFound().build();
        problemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
