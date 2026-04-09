package com.project;

import com.project.bst.BinarySearchTree;
import com.project.rbtree.RedBlackTree;
import com.project.BenchMark.BenchmarkService;
import com.project.BenchMark.BenchmarkCollecter;
import com.project.BenchMark.BenchmarkData;
import com.project.BenchMark.Stats;
import com.project.ArrayGeneration.ArrayGenerator;

import java.util.Arrays;
import java.util.Scanner;

public class Main2 {
    private static final int MAX_ARRAY_SIZE = 100000;
    private static final int DEFAULT_ARRAY_SIZE = 100000;
    private static final int MIN_RUNS = 5;
    private static final int DEFAULT_RUNS = 5;

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
            runNormalMode(scanner);
        } else if (mode == 2) {
            runBenchmarkMode(scanner);
        } else {
            System.out.println("Invalid choice. Exiting...");
        }

        scanner.close();
    }

    // ==================== NORMAL MODE ====================
    private static void runNormalMode(Scanner scanner) {
        System.out.println("\n--- Starting Normal Mode with Validation ENABLED ---");
        BinarySearchTree bst = new BinarySearchTree(true);
        RedBlackTree rbt = new RedBlackTree(true);

        while (true) {
            System.out.println("\n--- Tree Operations ---");
            System.out.println("1. Insert to BST   2. Delete from BST   3. View BST   4. BST In-Order & Size 5. BST height");
            System.out.println("6. Insert to RBT   7. Delete from RBT   8. View RBT   9. RBT In-Order & Size 10. RBT height");
            System.out.println("11. Exit");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            if (choice == 11) break;

            if (choice == 3) {
                System.out.println("\nBinary Search Tree Structure:");
                bst.printTree();
                continue;
            }
            if (choice == 4) {
                System.out.println("BST Size: " + bst.size());
                int[] inorder = bst.inOrder();
                System.out.println("BST In-Order (first 50 elements if larger): " +
                        (inorder.length > 50 ? Arrays.toString(Arrays.copyOf(inorder, 50)) + "..." : Arrays.toString(inorder)));
                continue;
            }
            if (choice == 8) {
                System.out.println("\nRed-Black Tree Structure:");
                rbt.printTree();
                continue;
            }
            if (choice == 9) {
                System.out.println("RBT Size: " + rbt.size());
                int[] inorder = rbt.inOrder();
                System.out.println("RBT In-Order (first 50 elements if larger): " +
                        (inorder.length > 50 ? Arrays.toString(Arrays.copyOf(inorder, 50)) + "..." : Arrays.toString(inorder)));
                continue;
            }
            if (choice == 5) {
                System.out.println("BST Height: " + bst.height());
                continue;
            }
            if (choice == 10) {
                System.out.println("RBT Height: " + rbt.height());
                continue;
            }

            System.out.print("Enter value: ");
            int val = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (bst.insert(val)) System.out.println(val + " inserted into BST.");
                    else System.out.println(val + " already exists in BST.");
                    break;
                case 2:
                    if (bst.delete(val)) System.out.println(val + " deleted from BST.");
                    else System.out.println(val + " not found in BST.");
                    break;
                case 6:
                    if (rbt.insert(val)) System.out.println(val + " inserted into RBT.");
                    else System.out.println(val + " already exists in RBT.");
                    break;
                case 7:
                    if (rbt.delete(val)) System.out.println(val + " deleted from RBT.");
                    else System.out.println(val + " not found in RBT.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        System.out.println("Normal Mode terminated.");
    }

    // ==================== BENCHMARK MODE ====================
    private static void runBenchmarkMode(Scanner scanner) {
        System.out.println("\n--- Benchmark Mode ---");

        // Get array size from user
        int arraySize = getArraySizeFromUser(scanner);

        // Get number of runs from user
        int numRuns = getNumRunsFromUser(scanner);

        // Ask about validation
//        System.out.print("\nEnable validation during benchmark? (y/n) [default: n]: ");
//        scanner.nextLine(); // consume newline
//        String validationInput = scanner.nextLine().trim().toLowerCase();
//        boolean enableValidation = validationInput.equals("y") || validationInput.equals("yes");

        System.out.println("\n" + "=".repeat(70));
        System.out.println("BENCHMARK CONFIGURATION");
        System.out.println("=".repeat(70));
        System.out.println("Array Size: " + arraySize);
        System.out.println("Max Value: " + (10 * arraySize) + " (10 × array size)");
        System.out.println("Number of Runs: " + numRuns);
//        System.out.println("Validation: " + (enableValidation ? "ENABLED" : "DISABLED"));
        System.out.println("=".repeat(70));

        System.out.println("\nSelect Disorder Levels to Test:");
        System.out.println("1. All Levels (Random, Sorted, 1%, 5%, 10%)");
        System.out.println("2. Random Only");
        System.out.println("3. Sorted Only");
        System.out.println("4. Custom Selection");
        System.out.print("Choice: ");

        int testChoice = scanner.nextInt();

        Object[][] testCases = getTestCases(testChoice, scanner);

        if (testCases.length == 0) {
            System.out.println("No test cases selected. Exiting benchmark.");
            return;
        }

        // Run benchmark
        BenchmarkService service = new BenchmarkService(arraySize);
        BenchmarkCollecter collector = new BenchmarkCollecter(numRuns);

        System.out.println("\n" + "🔥".repeat(35));
        System.out.println("STARTING BENCHMARK");
        System.out.println("🔥".repeat(35));

        for (Object[] testCase : testCases) {
            ArrayGenerator.DisorderLevel level = (ArrayGenerator.DisorderLevel) testCase[0];
            String name = (String) testCase[1];

            System.out.println("\n" + "=".repeat(70));
            System.out.println("Testing: " + name);
            System.out.println("=".repeat(70));

            BenchmarkData data = service.prepareData(level, name);

            System.gc();
            try {
                // Small delay for GC to work
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            try {
                Stats bstStats = collector.collect("BST", new BinarySearchTree(), data);
                Stats rbtStats = collector.collect("RBT", new RedBlackTree(), data);


                System.out.println("\n📊 Performance Summary:");
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
                System.out.println("❌ ERROR: StackOverflowError for " + name + " - BST may be too deep");
                System.out.println("   Try reducing array size or disabling sorted test");
            } catch (Exception e) {
                System.out.println("❌ ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("\n" + "✅".repeat(35));
        System.out.println("BENCHMARK COMPLETED");
        System.out.println("✅".repeat(35));
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

    // Helper method to get test cases based on user selection
    private static Object[][] getTestCases(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                return new Object[][]{
                        {null, "Random"},
                        {ArrayGenerator.DisorderLevel.SORTED, "Sorted"},
                        {ArrayGenerator.DisorderLevel.ONE_PERCENT, "1% Disorder"},
                        {ArrayGenerator.DisorderLevel.FIVE_PERCENT, "5% Disorder"},
                        {ArrayGenerator.DisorderLevel.TEN_PERCENT, "10% Disorder"}
                };
            case 2:
                return new Object[][]{
                        {null, "Random"}
                };
            case 3:
                return new Object[][]{
                        {ArrayGenerator.DisorderLevel.SORTED, "Sorted"}
                };
            case 4:
                return getCustomTestCases(scanner);
            default:
                System.out.println("Invalid choice. Running all tests.");
                return new Object[][]{
                        {null, "Random"},
                        {ArrayGenerator.DisorderLevel.SORTED, "Sorted"},
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
        System.out.println("2. Sorted");
        System.out.println("3. 1% Disorder");
        System.out.println("4. 5% Disorder");
        System.out.println("5. 10% Disorder");
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
                    testCases.add(new Object[]{ArrayGenerator.DisorderLevel.SORTED, "Sorted"});
                    break;
                case 3:
                    testCases.add(new Object[]{ArrayGenerator.DisorderLevel.ONE_PERCENT, "1% Disorder"});
                    break;
                case 4:
                    testCases.add(new Object[]{ArrayGenerator.DisorderLevel.FIVE_PERCENT, "5% Disorder"});
                    break;
                case 5:
                    testCases.add(new Object[]{ArrayGenerator.DisorderLevel.TEN_PERCENT, "10% Disorder"});
                    break;
                default:
                    System.out.println("Invalid choice: " + choice + ". Skipping.");
            }
        }

        return testCases.toArray(new Object[0][]);
    }
}