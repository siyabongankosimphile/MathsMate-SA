package com.mathsmate.services;

import com.mathsmate.models.Problem;
import com.mathsmate.repositories.ProblemRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VectorSearchService {

    private final ProblemRepository problemRepository;

    public VectorSearchService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    /**
     * Finds problems similar to the query using keyword overlap scoring.
     * Returns up to {@code topK} results sorted by relevance.
     */
    public List<Problem> findSimilar(String query, int topK) {
        if (query == null || query.isBlank()) return List.of();

        Set<String> queryTokens = tokenize(query);
        List<Problem> allProblems = problemRepository.findAll();

        return allProblems.stream()
                .filter(p -> p.getQuestion() != null)
                .map(p -> Map.entry(p, score(queryTokens, tokenize(p.getQuestion()))))
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<Problem, Double>comparingByValue().reversed())
                .limit(topK)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /** Convenience overload returning top 5. */
    public List<Problem> findSimilar(String query) {
        return findSimilar(query, 5);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Set<String> tokenize(String text) {
        return Arrays.stream(text.toLowerCase()
                        .replaceAll("[^a-z0-9\\s]", " ")
                        .split("\\s+"))
                .filter(t -> t.length() > 1)
                .collect(Collectors.toSet());
    }

    /** Jaccard similarity between two token sets. */
    private double score(Set<String> a, Set<String> b) {
        if (a.isEmpty() || b.isEmpty()) return 0.0;
        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        return (double) intersection.size() / union.size();
    }
}
