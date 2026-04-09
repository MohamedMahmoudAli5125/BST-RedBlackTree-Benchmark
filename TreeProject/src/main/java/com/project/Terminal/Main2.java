package com.project.Terminal;


import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           TREE IMPLEMENTATION & BENCHMARK TOOL             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\nSelect Mode:");
        System.out.println("1. Normal Mode (Interactive Tree Operations)");
        System.out.println("2. Benchmark Mode (Performance Testing)");
        System.out.print("\nChoice: ");

        int mode = scanner.nextInt();

        if (mode == 1) {
            NormalModeHandler.run(scanner);
        } else if (mode == 2) {
            BenchmarkModeHandler.run(scanner);
        } else {
            System.out.println("Invalid choice. Exiting...");
        }

        scanner.close();
    }
}