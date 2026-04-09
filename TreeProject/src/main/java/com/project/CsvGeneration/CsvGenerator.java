package com.project.CsvGeneration;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvGenerator {
    public static void saveToCsv(String fileName, List<String[]> records) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String[] record : records) {
                writer.println(String.join(",", record));
            }
            System.out.println(" CSV report saved to: " + fileName);
        } catch (IOException e) {
            System.err.println(" Error saving CSV: " + e.getMessage());
        }
    }
}