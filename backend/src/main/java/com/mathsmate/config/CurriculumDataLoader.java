package com.mathsmate.config;

import com.mathsmate.models.Curriculum;
import com.mathsmate.repositories.CurriculumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds initial South African CAPS curriculum data on startup
 * (only if the collection is empty).
 */
@Component
public class CurriculumDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CurriculumDataLoader.class);

    private final CurriculumRepository curriculumRepository;

    public CurriculumDataLoader(CurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }

    @Override
    public void run(String... args) {
        if (curriculumRepository.count() > 0) {
            log.info("Curriculum data already loaded, skipping seed.");
            return;
        }
        log.info("Seeding CAPS curriculum data...");
        List<Curriculum> data = List.of(
            new Curriculum("Whole Numbers", "Grade 7",
                "Properties of whole numbers, factors, multiples, and prime numbers.",
                List.of("Prime factorisation", "LCM", "HCF", "Rounding", "Estimation"),
                "CAPS Grade 7 â€“ Numbers, Operations & Relationships"),
            new Curriculum("Fractions", "Grade 7",
                "Operations with common fractions, percentages, and decimals.",
                List.of("Adding fractions", "Subtracting fractions", "Mixed numbers", "Percentages", "Decimal-fraction conversions"),
                "CAPS Grade 7 â€“ Numbers, Operations & Relationships"),
            new Curriculum("Integers", "Grade 8",
                "Counting, ordering, and operations with integers.",
                List.of("Adding integers", "Subtracting integers", "Multiplying integers", "Dividing integers", "Order of operations"),
                "CAPS Grade 8 â€“ Numbers, Operations & Relationships"),
            new Curriculum("Algebra â€“ Expressions", "Grade 8",
                "Introduction to algebraic expressions and simplification.",
                List.of("Like terms", "Expanding brackets", "Factorisation", "Substitution"),
                "CAPS Grade 8 â€“ Algebra"),
            new Curriculum("Linear Equations", "Grade 9",
                "Solving linear equations in one variable.",
                List.of("Equations of the form ax+b=c", "Equations with brackets", "Word problems", "Inequalities"),
                "CAPS Grade 9 â€“ Algebra"),
            new Curriculum("Trigonometry Basics", "Grade 9",
                "Introduction to sin, cos and tan ratios in right-angled triangles.",
                List.of("Naming sides (opposite, adjacent, hypotenuse)", "sin, cos, tan ratios", "Finding unknown sides", "Finding angles"),
                "CAPS Grade 9 â€“ Geometry"),
            new Curriculum("Quadratic Equations", "Grade 10",
                "Solving quadratic equations by factorisation and the quadratic formula.",
                List.of("Factorisation", "Completing the square", "Quadratic formula", "Discriminant", "Nature of roots"),
                "CAPS Grade 10 â€“ Algebra & Equations"),
            new Curriculum("Functions & Graphs", "Grade 10",
                "Understanding and sketching various function types.",
                List.of("Linear functions", "Quadratic functions", "Hyperbola", "Exponential functions", "Domain & range"),
                "CAPS Grade 10 â€“ Functions"),
            new Curriculum("Trigonometry â€“ Identities", "Grade 11",
                "Trigonometric identities and solving trig equations.",
                List.of("Compound angle formulae", "Double angle formulae", "Solving trig equations", "Reduction formulae"),
                "CAPS Grade 11 â€“ Trigonometry"),
            new Curriculum("Statistics & Probability", "Grade 11",
                "Data handling, central tendency, dispersion, and basic probability.",
                List.of("Mean, median, mode", "Range, variance, standard deviation", "Box-and-whisker plots", "Probability rules", "Complementary events"),
                "CAPS Grade 11 â€“ Statistics & Probability"),
            new Curriculum("Calculus â€“ Differentiation", "Grade 12",
                "Introduction to limits, derivatives, and application of calculus.",
                List.of("First principles", "Rules of differentiation", "Cubic functions", "Optimisation", "Rates of change"),
                "CAPS Grade 12 â€“ Calculus"),
            new Curriculum("Finance Maths", "Grade 12",
                "Simple and compound interest, annuities, and loan repayments.",
                List.of("Simple interest", "Compound interest", "Future value", "Present value", "Hire purchase"),
                "CAPS Grade 12 â€“ Finance, Growth & Decay"),
            new Curriculum("Sequences & Series", "Grade 12",
                "Arithmetic and geometric sequences, and their summation formulae.",
                List.of("Arithmetic sequences", "Geometric sequences", "Sigma notation", "Infinite geometric series"),
                "CAPS Grade 12 â€“ Sequences & Series")
        );
        curriculumRepository.saveAll(data);
        log.info("Seeded {} curriculum entries.", data.size());
    }
}
