package Q3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class SalesReportGenerator {
    public static void manualCloseVersion(Path input, Path output) throws IOException {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
            writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8);
            writeReport(reader, writer);
        } finally {
            IOException firstFailure = null;
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    firstFailure = e;
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    if (firstFailure == null) {
                        firstFailure = e;
                    }
                }
            }
            if (firstFailure != null) {
                throw firstFailure;
            }
        }
    }

    public static void tryWithResourcesVersion(Path input, Path output) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
            writeReport(reader, writer);
        }
    }

    private static void writeReport(BufferedReader reader, BufferedWriter writer)
            throws IOException {
        writer.write("Daily Sales Report - " + LocalDate.now());
        writer.newLine();
        double total = 0.0;
        int count = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length != 2) {
                throw new IOException("Invalid sales row: " + line);
            }
            try {
                double amount = Double.parseDouble(fields[1].trim());
                total += amount;
                count++;
                writer.write(fields[0].trim() + "," + String.format("%.2f", amount));
                writer.newLine();
            } catch (NumberFormatException e) {
                throw new IOException("Invalid amount in row: " + line, e);
            }
        }
        writer.write("Count=" + count);
        writer.newLine();
        writer.write("Total=" + String.format("%.2f", total));
        writer.newLine();
    }

    public static void main(String[] args) {
        Path input = Path.of("Q3", "sales.csv");
        Path manualOutput = Path.of("Q3", "manual-report.txt");
        Path safeOutput = Path.of("Q3", "safe-report.txt");
        try {
            manualCloseVersion(input, manualOutput);
            tryWithResourcesVersion(input, safeOutput);
            System.out.println("Both report versions completed.");
        } catch (IOException e) {
            System.err.println("Report generation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
