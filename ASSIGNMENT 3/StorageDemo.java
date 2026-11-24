package Q2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class StorageDemo {
    public static void saveSubmission(Path file, String data)
            throws SubmissionStorageException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8)) {
            writer.write(data);
        } catch (IOException cause) {
            throw new SubmissionStorageException(
                    "Could not store submission to " + file, cause);
        }
    }

    public static final class SubmissionStorageException extends Exception {
        public SubmissionStorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static void main(String[] args) {
        try {
            saveSubmission(Path.of("submission.txt"), "Java assignment response");
            System.out.println("Submission saved successfully.");
        } catch (SubmissionStorageException e) {
            System.err.println("Submission failed: " + e.getMessage());
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                System.out.println("Chaining confirmed: IOException is the cause.");
            }
            e.printStackTrace();
        }
    }
}
