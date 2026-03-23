package com.mathsmate.utils;

public class TextPreprocessor {

    public static String clean(String input) {
        if (input == null) return "";
        return input
                .replaceAll("[\u200B-\u200D\uFEFF]", "")  // zero-width chars
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static String preprocess(String input) {
        String cleaned = clean(input);
        // Normalize unicode math symbols
        cleaned = MathParser.normalize(cleaned);
        // Normalise common shorthands
        cleaned = cleaned
                .replace("x squared", "x^2")
                .replace("x cubed", "x^3")
                .replace("squared", "^2")
                .replace("cubed", "^3")
                .replace("times", "*")
                .replace("divided by", "/")
                .replace("plus", "+")
                .replace("minus", "-")
                .replace("percent", "%")
                .replace("pi", "3.14159");
        return cleaned.trim();
    }
}
