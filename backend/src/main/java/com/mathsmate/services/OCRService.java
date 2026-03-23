package com.mathsmate.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@Service
public class OCRService {

    private static final Logger log = LoggerFactory.getLogger(OCRService.class);

    @Value("${ocr.tesseract.command:tesseract}")
    private String tesseractCommand;

    @Value("${ocr.enabled:false}")
    private boolean ocrEnabled;

    /**
     * Attempts to extract text from image bytes using the Tesseract CLI.
     * Falls back to empty string if Tesseract is not available or disabled.
     */
    public String extractText(byte[] imageBytes) {
        if (!ocrEnabled || imageBytes == null || imageBytes.length == 0) {
            return "";
        }
        File inputFile = null;
        File outputFile = null;
        try {
            inputFile = File.createTempFile("ocr_input_", ".png");
            String outputBase = inputFile.getAbsolutePath().replace(".png", "");
            outputFile = new File(outputBase + ".txt");

            try (FileOutputStream fos = new FileOutputStream(inputFile)) {
                fos.write(imageBytes);
            }

            ProcessBuilder pb = new ProcessBuilder(
                    tesseractCommand, inputFile.getAbsolutePath(), outputBase, "-l", "eng");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            boolean finished = process.waitFor(30, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                log.warn("Tesseract timed out.");
                return "";
            }
            if (process.exitValue() != 0) {
                try (InputStream es = process.getInputStream()) {
                    log.warn("Tesseract error: {}", new String(es.readAllBytes()));
                }
                return "";
            }
            if (outputFile.exists()) {
                return Files.readString(outputFile.toPath()).trim();
            }
        } catch (Exception e) {
            log.error("OCR failed", e);
        } finally {
            if (inputFile != null) inputFile.delete();
            if (outputFile != null) outputFile.delete();
        }
        return "";
    }
}
