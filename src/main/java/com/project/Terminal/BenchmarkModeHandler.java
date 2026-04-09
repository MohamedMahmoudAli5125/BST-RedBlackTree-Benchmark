package com.project.Terminal;


import com.project.CsvGeneration.CsvGenerator;
import com.project.bst.BinarySearchTree;
import com.project.rbtree.RedBlackTree;
import com.project.BenchMark.*;
import com.project.ArrayGeneration.ArrayGenerator;
import java.util.*;

public class BenchmarkModeHandler {
    private static final int MAX_ARRAY_SIZE = 100000;
    private static final int DEFAULT_ARRAY_SIZE = 100000;
    private static final int MIN_RUNS = 5;
    private static final int DEFAULT_RUNS = 5;

    public static void run(Scanner scanner) {
        System.out.println("\n--- Benchmark Mode ---");
        int arraySize = getArraySizeFromUser(scanner);
        int numRuns = getNumRunsFromUser(scanner);
        int benchmarkMode = getBenchmarkModeFromUser(scanner);

        System.out.println("\nSelect Disorder Levels:\n1. All  2. Random Only  3. Custom");
        int testChoice = scanner.nextInt();
        Object[][] testCases = getTestCases(testChoice, scanner);

        BenchmarkService service = new BenchmarkService(arraySize);
        BenchmarkCollecter collector = new BenchmarkCollecter(numRuns);
        List<String[]> csvRecords = new ArrayList<>();
        csvRecords.add(new String[]{"Distribution", "Tree", "Op", "Mean(ms)", "Median(ms)", "StdDev(ms)", "Height"});

        for (Object[] testCase : testCases) {
            if (benchmarkMode == 2) {
                runSortOnlyTest(testCase, service, collector, csvRecords, numRuns);
            } else {
                runSingleTest(testCase, service, collector, csvRecords);
            }
        }

        System.out.print("\nExport results to CSV? (y/n): ");
        if (scanner.next().toLowerCase().startsWith("y")) {
            String csvFileName = benchmarkMode == 2
                    ? "benchmark_sort_only_results.csv"
                    : "benchmark_results.csv";
            CsvGenerator.saveToCsv(csvFileName, csvRecords);
        }
    }

    private static void runSingleTest(Object[] testCase, BenchmarkService service, BenchmarkCollecter collector, List<String[]> csvRecords) {
        ArrayGenerator.DisorderLevel level = (ArrayGenerator.DisorderLevel) testCase[0];
        String name = (String) testCase[1];
        System.out.println("\nTesting: " + name);

        BenchmarkData data = service.prepareData(level, name);
        try {
            Stats bstStats = collector.collect("BST", new BinarySearchTree(), data);
            Stats rbtStats = collector.collect("RBT", new RedBlackTree(), data);
            addRecord(csvRecords, name, "BST", bstStats);
            addRecord(csvRecords, name, "RBT", rbtStats);

            System.out.println("\n Performance Summary:");
            System.out.println("-".repeat(50));
            System.out.printf("BST  - Insert: %.4f ms | Search: %.4f ms | Delete: %.4f ms | Sort: %.4f ms\n",
                    bstStats.insertMean, bstStats.searchMean, bstStats.deleteMean, bstStats.sortMean);
            System.out.printf("RBT  - Insert: %.4f ms | Search: %.4f ms | Delete: %.4f ms | Sort: %.4f ms\n",
                    rbtStats.insertMean, rbtStats.searchMean, rbtStats.deleteMean, rbtStats.sortMean);
            System.out.println("\n--- Speedup (BST / RBT) ---");
            System.out.printf("Insert: %.2fx %s\n",
                    bstStats.insertMean / rbtStats.insertMean,
                    bstStats.insertMean > rbtStats.insertMean ? "(RBT faster)" : "(BST faster)");
            System.out.printf("Search: %.2fx %s\n",
                    bstStats.searchMean / rbtStats.searchMean,
                    bstStats.searchMean > rbtStats.searchMean ? "(RBT faster)" : "(BST faster)");
            System.out.printf("Delete: %.2fx %s\n",
                    bstStats.deleteMean / rbtStats.deleteMean,
                    bstStats.deleteMean > rbtStats.deleteMean ? "(RBT faster)" : "(BST faster)");
            System.out.printf("Sort:   %.2fx %s\n",
                    bstStats.sortMean / rbtStats.sortMean,
                    bstStats.sortMean > rbtStats.sortMean ? "(RBT faster)" : "(BST faster)");

        } catch (StackOverflowError e) {
            System.out.println(" ERROR: BST may be too deep for " + name);
        }
    }

    private static void runSortOnlyTest(Object[] testCase, BenchmarkService service, BenchmarkCollecter collector, List<String[]> csvRecords, int numRuns) {
        ArrayGenerator.DisorderLevel level = (ArrayGenerator.DisorderLevel) testCase[0];
        String name = (String) testCase[1];
        System.out.println("\nTesting (Sort Only): " + name);

        BenchmarkData data = service.prepareData(level, name);
        try {
            Stats bstStats = collector.collectSortOnly("BST", new BinarySearchTree(), data);
            Stats rbtStats = collector.collectSortOnly("RBT", new RedBlackTree(), data);
            Stats quickStats = QuickSortBenchmark.collectSortOnly(data, numRuns);

            addSortOnlyRecord(csvRecords, name, "BST", bstStats);
            addSortOnlyRecord(csvRecords, name, "RBT", rbtStats);
            addSortOnlyRecord(csvRecords, name, "QuickSort", quickStats);

            System.out.println("\n Sort-Only Summary (Insert + InOrder for trees):");
            System.out.println("-".repeat(50));
            System.out.printf("BST       - Sort: %.4f ms\n", bstStats.sortMean);
            System.out.printf("RBT       - Sort: %.4f ms\n", rbtStats.sortMean);
            System.out.printf("QuickSort - Sort: %.4f ms\n", quickStats.sortMean);

            System.out.println("\n--- Speedup (vs QuickSort baseline) ---");
            System.out.printf("BST / QuickSort: %.2fx %s\n",
                    bstStats.sortMean / quickStats.sortMean,
                    bstStats.sortMean > quickStats.sortMean ? "(QuickSort faster)" : "(BST faster)");
            System.out.printf("RBT / QuickSort: %.2fx %s\n",
                    rbtStats.sortMean / quickStats.sortMean,
                    rbtStats.sortMean > quickStats.sortMean ? "(QuickSort faster)" : "(RBT faster)");

        } catch (StackOverflowError e) {
            System.out.println(" ERROR: BST may be too deep for " + name);
        }
    }
    // Helper method to get array size from user
    private static int getArraySizeFromUser(Scanner scanner) {
        System.out.print("\nEnter array size (max: " + MAX_ARRAY_SIZE + ", default: " + DEFAULT_ARRAY_SIZE + "): ");
        String input = scanner.next();

        int arraySize;
        if (input.trim().isEmpty()) {
            arraySize = DEFAULT_ARRAY_SIZE;
        } else {
            try {
                arraySize = Integer.parseInt(input);
                if (arraySize < 1) {
                    System.out.println("Array size must be positive. Using default: " + DEFAULT_ARRAY_SIZE);
                    arraySize = DEFAULT_ARRAY_SIZE;
                } else if (arraySize > MAX_ARRAY_SIZE) {
                    System.out.println("Array size exceeds maximum (" + MAX_ARRAY_SIZE + "). Using max value.");
                    arraySize = MAX_ARRAY_SIZE;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using default: " + DEFAULT_ARRAY_SIZE);
                arraySize = DEFAULT_ARRAY_SIZE;
            }
        }
        return arraySize;
    }
    // Helper method to get number of runs from user
    private static int getNumRunsFromUser(Scanner scanner) {
        System.out.print("Enter number of benchmark runs (min: " + MIN_RUNS + ", default: " + DEFAULT_RUNS + "): ");
        String input = scanner.next();

        int numRuns;
        if (input.trim().isEmpty()) {
            numRuns = DEFAULT_RUNS;
        } else {
            try {
                numRuns = Integer.parseInt(input);
                if (numRuns < MIN_RUNS) {
                    System.out.println("Number of runs must be at least " + MIN_RUNS + ". Using default: " + DEFAULT_RUNS);
                    numRuns = DEFAULT_RUNS;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using default: " + DEFAULT_RUNS);
                numRuns = DEFAULT_RUNS;
            }
        }
        return numRuns;
    }

    private static int getBenchmarkModeFromUser(Scanner scanner) {
        System.out.println("\nSelect Benchmark Mode:");
        System.out.println("1. Full Operations (Insert, Search, Delete, Sort)");
        System.out.println("2. Sort Only (BST: Insert+InOrder, RBT: Insert+InOrder, QuickSort)");
        System.out.print("Your choice (default: 1): ");

        String input = scanner.next();
        try {
            int mode = Integer.parseInt(input);
            if (mode == 1 || mode == 2) {
                return mode;
            }
        } catch (NumberFormatException ignored) {
        }

        System.out.println("Invalid choice. Using Full Operations mode.");
        return 1;
    }

    private static void addRecord(List<String[]> list, String dist, String tree, Stats s) {
        list.add(new String[]{dist, tree, "Insert", String.valueOf(s.insertMean), String.valueOf(s.insertMedian), String.valueOf(s.insertStdDev), String.valueOf(s.height)});
        list.add(new String[]{dist, tree, "Search", String.valueOf(s.searchMean), String.valueOf(s.searchMedian), String.valueOf(s.searchStdDev), ""});
        list.add(new String[]{dist, tree, "Delete", String.valueOf(s.deleteMean), String.valueOf(s.deleteMedian), String.valueOf(s.deleteStdDev), ""});
        list.add(new String[]{dist, tree, "Sort", String.valueOf(s.sortMean), String.valueOf(s.sortMedian), String.valueOf(s.sortStdDev), ""});
    }

    private static void addSortOnlyRecord(List<String[]> list, String dist, String algo, Stats s) {
        String height = s.height >= 0 ? String.valueOf(s.height) : "";
        list.add(new String[]{dist, algo, "Sort", String.valueOf(s.sortMean), String.valueOf(s.sortMedian), String.valueOf(s.sortStdDev), height});
    }
    // Helper method to get test cases based on user selection
    private static Object[][] getTestCases(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                return new Object[][]{
                        {null, "Random"},
                        {ArrayGenerator.DisorderLevel.ONE_PERCENT, "1% Disorder"},
                        {ArrayGenerator.DisorderLevel.FIVE_PERCENT, "5% Disorder"},
                        {ArrayGenerator.DisorderLevel.TEN_PERCENT, "10% Disorder"}
                };
            case 2:
                return new Object[][]{
                        {null, "Random"}
                };
            case 3:
                return getCustomTestCases(scanner);
            default:
                System.out.println("Invalid choice. Running all tests.");
                return new Object[][]{
                        {null, "Random"},
                        {ArrayGenerator.DisorderLevel.ONE_PERCENT, "1% Disorder"},
                        {ArrayGenerator.DisorderLevel.FIVE_PERCENT, "5% Disorder"},
                        {ArrayGenerator.DisorderLevel.TEN_PERCENT, "10% Disorder"}
                };
        }
    }
    // Helper method for custom test case selection
    private static Object[][] getCustomTestCases(Scanner scanner) {
        System.out.println("\nSelect disorder levels (comma-separated, e.g., 1,3,5):");
        System.out.println("1. Random");
        System.out.println("2. 1% Disorder");
        System.out.println("3. 5% Disorder");
        System.out.println("4. 10% Disorder");
        System.out.print("Your choice: ");

        scanner.nextLine(); // consume newline
        String input = scanner.nextLine();
        String[] selections = input.split(",");

        java.util.ArrayList<Object[]> testCases = new java.util.ArrayList<>();

        for (String sel : selections) {
            int choice = Integer.parseInt(sel.trim());
            switch (choice) {
                case 1:
                    testCases.add(new Object[]{null, "Random"});
                    break;
                case 2:
                    testCases.add(new Object[]{ArrayGenerator.DisorderLevel.ONE_PERCENT, "1% Disorder"});
                    break;
                case 3:
                    testCases.add(new Object[]{ArrayGenerator.DisorderLevel.FIVE_PERCENT, "5% Disorder"});
                    break;
                case 4:
                    testCases.add(new Object[]{ArrayGenerator.DisorderLevel.TEN_PERCENT, "10% Disorder"});
                    break;
                default:
                    System.out.println("Invalid choice: " + choice + ". Skipping.");
            }
        }

        return testCases.toArray(new Object[0][]);
    }
}