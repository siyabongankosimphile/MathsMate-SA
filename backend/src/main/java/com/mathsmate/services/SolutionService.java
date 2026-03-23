package com.mathsmate.services;

import com.mathsmate.dto.SolutionResponse;
import com.mathsmate.utils.MathParser;
import com.mathsmate.utils.TextPreprocessor;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SolutionService {

    private final CacheService cacheService;

    public SolutionService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public SolutionResponse solve(String problemText) {
        if (problemText == null || problemText.isBlank()) {
            return new SolutionResponse("Please enter a valid problem.", List.of(), "none", "");
        }

        String cleaned = TextPreprocessor.preprocess(problemText);
        String key = cleaned.toLowerCase().replaceAll("\\s", "");

        String cached = cacheService.get(key);
        if (cached != null) {
            SolutionResponse r = new SolutionResponse(cached, List.of("Retrieved from cache."), "cache", detectTopic(cleaned));
            r.setFromCache(true);
            return r;
        }

        SolutionResponse response;
        if (isQuadraticEquation(cleaned)) {
            response = solveQuadratic(cleaned);
        } else if (isLinearEquation(cleaned)) {
            response = solveLinear(cleaned);
        } else if (isPercentageProblem(cleaned)) {
            response = solvePercentage(cleaned);
        } else if (isTrigonometry(cleaned)) {
            response = solveTrigonometry(cleaned);
        } else if (isLCMHCFProblem(cleaned)) {
            response = solveLCMHCF(cleaned);
        } else if (isSimpleInterest(cleaned)) {
            response = solveSimpleInterest(cleaned);
        } else if (isArithmeticExpression(cleaned)) {
            response = evaluateArithmetic(cleaned);
        } else {
            response = new SolutionResponse(
                    "Problem type not recognised. Supported: arithmetic, linear/quadratic equations, percentages, trigonometry, LCM/HCF, simple interest.",
                    List.of("Input received: " + cleaned),
                    "unknown",
                    detectTopic(cleaned)
            );
        }

        if (response.getAnswer() != null && !response.getAnswer().isEmpty()) {
            cacheService.put(key, response.getAnswer());
        }
        return response;
    }

    // ── Type detectors ────────────────────────────────────────────────────────

    private boolean isLinearEquation(String s) {
        return s.matches(".*[a-zA-Z].*=.*") && !isQuadraticEquation(s);
    }

    private boolean isQuadraticEquation(String s) {
        return s.contains("^2") || s.contains("\u00B2");
    }

    private boolean isPercentageProblem(String s) {
        String l = s.toLowerCase();
        return l.contains("%") || l.contains("percent");
    }

    private boolean isTrigonometry(String s) {
        String l = s.toLowerCase();
        return l.contains("sin") || l.contains("cos") || l.contains("tan")
                || l.contains("sinh") || l.contains("cosh") || l.contains("tanh");
    }

    private boolean isLCMHCFProblem(String s) {
        String l = s.toLowerCase();
        return l.contains("lcm") || l.contains("hcf") || l.contains("gcd")
                || l.contains("lowest common") || l.contains("highest common");
    }

    private boolean isSimpleInterest(String s) {
        String l = s.toLowerCase();
        return l.contains("simple interest") || l.contains("principal") && l.contains("rate") && l.contains("time");
    }

    private boolean isArithmeticExpression(String s) {
        return s.matches("[0-9+\\-*/().^\\s]+");
    }

    // ── Solvers ───────────────────────────────────────────────────────────────

    private SolutionResponse evaluateArithmetic(String expr) {
        List<String> steps = new ArrayList<>();
        steps.add("Evaluate: " + expr);
        try {
            Expression e = new ExpressionBuilder(expr).build();
            double result = e.evaluate();
            steps.add("Result = " + MathParser.formatNumber(result));
            return new SolutionResponse(MathParser.formatNumber(result), steps, "arithmetic", "Number Operations");
        } catch (Exception ex) {
            return new SolutionResponse("Cannot evaluate expression: " + expr, steps, "arithmetic", "Number Operations");
        }
    }

    private SolutionResponse solveLinear(String equation) {
        List<String> steps = new ArrayList<>();
        steps.add("Given equation: " + equation);
        try {
            String s = equation.replaceAll("\\s+", "").replace("\u2212", "-");
            String[] parts = s.split("=");
            if (parts.length != 2) throw new Exception("Equation must contain exactly one '=' sign.");

            double[] lhs = parseSide(parts[0]);
            double[] rhs = parseSide(parts[1]);

            double netCoeff = lhs[0] - rhs[0];
            double netConst = rhs[1] - lhs[1];

            steps.add("Move variable terms to left, constants to right:");
            steps.add(MathParser.fmt(lhs[0]) + "x - " + MathParser.fmt(rhs[0]) + "x = "
                    + MathParser.fmt(rhs[1]) + " - " + MathParser.fmt(lhs[1]));
            steps.add(MathParser.fmt(netCoeff) + "x = " + MathParser.fmt(netConst));

            if (netCoeff == 0) {
                String answer = (netConst == 0) ? "All real numbers (identity)" : "No solution (contradiction)";
                steps.add(answer);
                return new SolutionResponse(answer, steps, "linear equation", "Algebra");
            }

            double x = netConst / netCoeff;
            steps.add("Divide both sides by " + MathParser.fmt(netCoeff) + ":");
            steps.add("x = " + MathParser.fmt(netConst) + " / " + MathParser.fmt(netCoeff));
            steps.add("x = " + MathParser.formatNumber(x));

            return new SolutionResponse("x = " + MathParser.formatNumber(x), steps, "linear equation", "Algebra");
        } catch (Exception e) {
            return new SolutionResponse("Could not solve: " + e.getMessage(), steps, "linear equation", "Algebra");
        }
    }

    /** Parse one side of a linear equation into [coefficient-of-x, constant]. */
    private double[] parseSide(String expr) {
        double coeff = 0, constant = 0;
        // Ensure leading sign
        if (!expr.startsWith("-") && !expr.startsWith("+")) expr = "+" + expr;
        // Split on + or - while keeping the sign
        Matcher m = Pattern.compile("[+-][^+-]*").matcher(expr);
        while (m.find()) {
            String term = m.group().trim();
            if (term.isEmpty()) continue;
            boolean hasVar = term.matches(".*[a-zA-Z].*");
            String numStr = term.replaceAll("[a-zA-Z]", "").trim();
            double num;
            if (numStr.equals("+") || numStr.isEmpty()) num = 1.0;
            else if (numStr.equals("-")) num = -1.0;
            else {
                try { num = Double.parseDouble(numStr); }
                catch (NumberFormatException ignored) { continue; }
            }
            if (hasVar) coeff += num;
            else constant += num;
        }
        return new double[]{coeff, constant};
    }

    private SolutionResponse solveQuadratic(String equation) {
        List<String> steps = new ArrayList<>();
        steps.add("Given: " + equation);
        try {
            String s = equation.replace("^2", "\u00B2")
                    .replaceAll("\\s+", "")
                    .replace("\u2212", "-");

            // Normalise to one-side: ax²+bx+c=0
            String lhsStr = s.contains("=") ? s.split("=")[0] : s;
            String rhsStr = s.contains("=") ? s.split("=")[1] : "0";

            double[] lhsCoeffs = parseQuadratic(lhsStr);
            double[] rhsCoeffs = parseQuadratic(rhsStr);

            double a = lhsCoeffs[0] - rhsCoeffs[0];
            double b = lhsCoeffs[1] - rhsCoeffs[1];
            double c = lhsCoeffs[2] - rhsCoeffs[2];

            steps.add("Standard form: " + MathParser.fmt(a) + "x\u00B2 + "
                    + MathParser.fmt(b) + "x + " + MathParser.fmt(c) + " = 0");
            steps.add("Quadratic formula: x = (-b \u00B1 \u221A(b\u00B2 - 4ac)) / 2a");

            double disc = b * b - 4 * a * c;
            steps.add("Discriminant \u0394 = (" + MathParser.fmt(b) + ")\u00B2 - 4(" + MathParser.fmt(a) + ")(" + MathParser.fmt(c) + ") = " + MathParser.formatNumber(disc));

            String answer;
            if (disc > 0) {
                double x1 = (-b + Math.sqrt(disc)) / (2 * a);
                double x2 = (-b - Math.sqrt(disc)) / (2 * a);
                steps.add("Two real roots:");
                steps.add("x\u2081 = " + MathParser.formatNumber(x1));
                steps.add("x\u2082 = " + MathParser.formatNumber(x2));
                answer = "x = " + MathParser.formatNumber(x1) + " or x = " + MathParser.formatNumber(x2);
            } else if (disc == 0) {
                double x = -b / (2 * a);
                steps.add("One repeated root: x = " + MathParser.formatNumber(x));
                answer = "x = " + MathParser.formatNumber(x) + " (repeated root)";
            } else {
                double realPart = -b / (2 * a);
                double imagPart = Math.sqrt(-disc) / (2 * a);
                steps.add("No real roots (\u0394 < 0).");
                steps.add("Complex: x = " + MathParser.formatNumber(realPart) + " \u00B1 " + MathParser.formatNumber(imagPart) + "i");
                answer = "x = " + MathParser.formatNumber(realPart) + " \u00B1 " + MathParser.formatNumber(imagPart) + "i";
            }
            return new SolutionResponse(answer, steps, "quadratic equation", "Algebra");
        } catch (Exception e) {
            return new SolutionResponse("Could not parse quadratic. Format: ax\u00B2+bx+c=0", steps, "quadratic equation", "Algebra");
        }
    }

    /** Returns [a, b, c] for ax\u00B2+bx+c expression. */
    private double[] parseQuadratic(String expr) {
        double a = 0, b = 0, c = 0;
        if (!expr.startsWith("-") && !expr.startsWith("+")) expr = "+" + expr;
        Matcher m = Pattern.compile("[+-][^+-]*").matcher(expr);
        while (m.find()) {
            String term = m.group().trim();
            if (term.isEmpty()) continue;
            if (term.contains("\u00B2") || term.contains("^2")) {
                String n = term.replaceAll("[^0-9.+-]", "").replaceAll("\\^2", "").trim();
                a += parseCoeff(n);
            } else if (term.matches(".*[a-zA-Z].*")) {
                String n = term.replaceAll("[a-zA-Z]", "").trim();
                b += parseCoeff(n);
            } else {
                String n = term.trim();
                try { c += Double.parseDouble(n); } catch (NumberFormatException ignored) {}
            }
        }
        return new double[]{a, b, c};
    }

    private double parseCoeff(String s) {
        if (s.equals("+") || s.isEmpty()) return 1.0;
        if (s.equals("-")) return -1.0;
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return 1.0; }
    }

    private SolutionResponse solvePercentage(String problem) {
        List<String> steps = new ArrayList<>();
        steps.add("Given: " + problem);
        try {
            // Pattern: "X% of Y" or "X percent of Y"
            Matcher m = Pattern.compile("([0-9.]+)\\s*%\\s*of\\s*([0-9.]+)", Pattern.CASE_INSENSITIVE).matcher(problem);
            if (m.find()) {
                double pct = Double.parseDouble(m.group(1));
                double total = Double.parseDouble(m.group(2));
                double result = pct / 100.0 * total;
                steps.add(pct + "% of " + total + " = (" + pct + " / 100) \u00D7 " + total);
                steps.add("= " + MathParser.formatNumber(result));
                return new SolutionResponse(MathParser.formatNumber(result), steps, "percentage", "Number Operations");
            }
            // Pattern: "what is X% of Y"
            Matcher m2 = Pattern.compile("what\\s+is\\s+([0-9.]+)\\s*%", Pattern.CASE_INSENSITIVE).matcher(problem);
            Matcher m3 = Pattern.compile("of\\s+([0-9.]+)", Pattern.CASE_INSENSITIVE).matcher(problem);
            if (m2.find() && m3.find()) {
                double pct = Double.parseDouble(m2.group(1));
                double total = Double.parseDouble(m3.group(1));
                double result = pct / 100.0 * total;
                steps.add(pct + "% \u00D7 " + total + " = " + MathParser.formatNumber(result));
                return new SolutionResponse(MathParser.formatNumber(result), steps, "percentage", "Number Operations");
            }
            return new SolutionResponse("Could not parse percentage problem.", steps, "percentage", "Number Operations");
        } catch (Exception e) {
            return new SolutionResponse("Error: " + e.getMessage(), steps, "percentage", "Number Operations");
        }
    }

    private SolutionResponse solveTrigonometry(String problem) {
        List<String> steps = new ArrayList<>();
        steps.add("Given: " + problem);
        try {
            String p = problem.toLowerCase().replaceAll("\\s+", "");
            // Match sin(X), cos(X), tan(X) where X is degrees
            Pattern pat = Pattern.compile("(sin|cos|tan|sinh|cosh|tanh)\\(?([0-9.]+)(?:deg|\u00B0)?\\)?");
            Matcher m = pat.matcher(p);
            if (m.find()) {
                String fn = m.group(1);
                double angleDeg = Double.parseDouble(m.group(2));
                double angleRad = Math.toRadians(angleDeg);
                double result;
                switch (fn) {
                    case "sin"  -> result = Math.sin(angleRad);
                    case "cos"  -> result = Math.cos(angleRad);
                    case "tan"  -> result = Math.tan(angleRad);
                    case "sinh" -> result = Math.sinh(angleRad);
                    case "cosh" -> result = Math.cosh(angleRad);
                    case "tanh" -> result = Math.tanh(angleRad);
                    default -> throw new Exception("Unknown trig function");
                }
                steps.add(fn + "(" + angleDeg + "\u00B0) = " + fn + "(" + MathParser.formatNumber(angleRad) + " rad)");
                steps.add("= " + MathParser.formatNumber(result));
                return new SolutionResponse(MathParser.formatNumber(result), steps, "trigonometry", "Trigonometry");
            }
            return new SolutionResponse("Could not parse trig expression. Example: sin(30)", steps, "trigonometry", "Trigonometry");
        } catch (Exception e) {
            return new SolutionResponse("Error: " + e.getMessage(), steps, "trigonometry", "Trigonometry");
        }
    }

    private SolutionResponse solveLCMHCF(String problem) {
        List<String> steps = new ArrayList<>();
        steps.add("Given: " + problem);
        try {
            boolean isLCM = problem.toLowerCase().contains("lcm") || problem.toLowerCase().contains("lowest common");
            Matcher m = Pattern.compile("([0-9]+)").matcher(problem);
            List<Long> nums = new ArrayList<>();
            while (m.find()) nums.add(Long.parseLong(m.group()));
            if (nums.size() < 2) throw new Exception("Need at least 2 numbers.");

            steps.add("Numbers: " + nums);
            long result;
            if (isLCM) {
                result = nums.get(0);
                for (int i = 1; i < nums.size(); i++) result = lcm(result, nums.get(i));
                steps.add("LCM = " + result);
                return new SolutionResponse(String.valueOf(result), steps, "LCM", "Number Operations");
            } else {
                result = nums.get(0);
                for (int i = 1; i < nums.size(); i++) result = gcd(result, nums.get(i));
                steps.add("HCF/GCD = " + result);
                return new SolutionResponse(String.valueOf(result), steps, "HCF", "Number Operations");
            }
        } catch (Exception e) {
            return new SolutionResponse("Error: " + e.getMessage(), steps, "LCM/HCF", "Number Operations");
        }
    }

    private SolutionResponse solveSimpleInterest(String problem) {
        List<String> steps = new ArrayList<>();
        steps.add("Given: " + problem);
        try {
            double principal = extractLabelled(problem, "principal", "p");
            double rate = extractLabelled(problem, "rate", "r");
            double time = extractLabelled(problem, "time", "t");
            steps.add("Formula: SI = (P \u00D7 R \u00D7 T) / 100");
            steps.add("P = R" + MathParser.formatNumber(principal) + ", R = " + MathParser.formatNumber(rate) + "%, T = " + MathParser.formatNumber(time) + " years");
            double si = (principal * rate * time) / 100.0;
            double amount = principal + si;
            steps.add("SI = (" + MathParser.fmt(principal) + " \u00D7 " + MathParser.fmt(rate) + " \u00D7 " + MathParser.fmt(time) + ") / 100 = " + MathParser.formatNumber(si));
            steps.add("Total Amount = P + SI = R" + MathParser.formatNumber(amount));
            return new SolutionResponse("SI = R" + MathParser.formatNumber(si) + ", Amount = R" + MathParser.formatNumber(amount), steps, "simple interest", "Finance Maths");
        } catch (Exception e) {
            return new SolutionResponse("Format: principal=1000, rate=5, time=3", steps, "simple interest", "Finance Maths");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private double extractLabelled(String text, String... labels) throws Exception {
        String lower = text.toLowerCase();
        for (String label : labels) {
            Pattern p = Pattern.compile(label + "\\s*[=:]?\\s*([0-9]+\\.?[0-9]*)", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(lower);
            if (m.find()) return Double.parseDouble(m.group(1));
        }
        throw new Exception("Cannot find value for: " + String.join("/", labels));
    }

    private long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    private String detectTopic(String text) {
        String l = text.toLowerCase();
        if (l.contains("sin") || l.contains("cos") || l.contains("tan")) return "Trigonometry";
        if (l.contains("^2") || l.contains("quadratic")) return "Algebra";
        if (l.contains("interest") || l.contains("principal")) return "Finance Maths";
        if (l.contains("%") || l.contains("percent")) return "Number Operations";
        if (l.contains("lcm") || l.contains("hcf") || l.contains("gcd")) return "Number Operations";
        if (l.contains("=")) return "Algebra";
        return "Number Operations";
    }
}
