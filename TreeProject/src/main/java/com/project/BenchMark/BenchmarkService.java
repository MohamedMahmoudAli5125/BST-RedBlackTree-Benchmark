package com.project.BenchMark;

import com.project.ITree;
import com.project.ArrayGeneration.ArrayGenerator;
import com.project.bst.BinarySearchTree;
import com.project.rbtree.RedBlackTree;

import java.util.Random;

public class BenchmarkService {
//    private final ArrayGenerator gen = new ArrayGenerator();
private final ArrayGenerator gen;

    private final Random random = new Random(12345);
    public BenchmarkService() {
        this.gen = new ArrayGenerator();
    }
    public BenchmarkService(int arraySize) {
        this.gen = new ArrayGenerator(arraySize);
    }
    //Duplicates allowed for all operations
    public BenchmarkData prepareData(ArrayGenerator.DisorderLevel level, String name) {
        BenchmarkData data = new BenchmarkData(name);

        // Insertion Array (100,000 elements)
        if (name.equalsIgnoreCase("Random")) {
            data.insertData = gen.generateRandom();
        } else {
            data.insertData = gen.generateNearlySorted(level);
        }

        // Contains Array (50k from inserted, 50k new)
        int arraySize = gen.getArraySize();

        data.containsData = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            int idx = random.nextInt(data.insertData.length);
            data.containsData[i] = data.insertData[idx];
        }
        for (int i = 0; i < arraySize / 2; i++) {
            // Negative values not in tree
            data.containsData[arraySize /2 + i] = -1 - i;
        }

        // Delete Array (20% from inserted)
        data.deleteData = new int[arraySize / 5];
        for (int i = 0; i < arraySize /5 ; i++) {
            int idx = random.nextInt(data.insertData.length);
            data.deleteData[i] = data.insertData[idx];
        }

        return data;
    }

    public static void main(String[] args) {
        // Default option: using DEFAULT_ARRAY_SIZE = 100,000
        System.out.println("=== Running Benchmark with DEFAULT Configuration ===");
        System.out.println("Array Size: 100,000");
        System.out.println("Max Value: 1,000,000 (10 × array size)\n");

        BenchmarkService service = new BenchmarkService(); // Using default size
        BenchmarkCollecter collector = new BenchmarkCollecter();

        Object[][] testCases = {
                {null, "Random"},
                {ArrayGenerator.DisorderLevel.SORTED, "Sorted"},
                {ArrayGenerator.DisorderLevel.ONE_PERCENT, "1% Disorder"},
                {ArrayGenerator.DisorderLevel.FIVE_PERCENT, "5% Disorder"},
                {ArrayGenerator.DisorderLevel.TEN_PERCENT, "10% Disorder"}
        };

        for (Object[] testCase : testCases) {
            ArrayGenerator.DisorderLevel level = (ArrayGenerator.DisorderLevel) testCase[0];
            String name = (String) testCase[1];

            System.out.println("\n" + "=".repeat(60));
            System.out.println("Testing: " + name);
            System.out.println("=".repeat(60));

            BenchmarkData data = service.prepareData(level, name);

            System.gc();
            try {
                Stats bstStats = collector.collect("BST", new BinarySearchTree(), data);
                Stats rbtStats = collector.collect("RBT", new RedBlackTree(), data);

                System.out.println("\n--- Speedup (BST / RBT) ---");
                System.out.printf("Insert: %.2fx\n", bstStats.insertMean / rbtStats.insertMean);
                System.out.printf("Search: %.2fx\n", bstStats.searchMean / rbtStats.searchMean);
                System.out.printf("Delete: %.2fx\n", bstStats.deleteMean / rbtStats.deleteMean);
                System.out.printf("Sort:   %.2fx\n", bstStats.sortMean / rbtStats.sortMean);

            } catch (StackOverflowError e) {
                System.out.println("ERROR: StackOverflowError for " + name + " - BST may be too deep");
                System.out.println("Skipping this test case...");
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Optional: Run with custom size (50,000)
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("=== Running Benchmark with CUSTOM Configuration (50,000) ===");
        System.out.println("=".repeat(60));

        BenchmarkService customService = new BenchmarkService(50000);
        for (Object[] testCase : testCases) {
            ArrayGenerator.DisorderLevel level = (ArrayGenerator.DisorderLevel) testCase[0];
            String name = (String) testCase[1];

            if (level == ArrayGenerator.DisorderLevel.SORTED) {
                System.out.println("\nSkipping Sorted test for custom size (to avoid StackOverflow)");
                continue;
            }

            System.out.println("\nTesting: " + name);
            BenchmarkData data = customService.prepareData(level, name);

            System.gc();
            try {
                Stats bstStats = collector.collect("BST", new BinarySearchTree(), data);
                Stats rbtStats = collector.collect("RBT", new RedBlackTree(), data);

                System.out.println("\n--- Speedup (BST / RBT) ---");
                System.out.printf("Insert: %.2fx\n", bstStats.insertMean / rbtStats.insertMean);
                System.out.printf("Search: %.2fx\n", bstStats.searchMean / rbtStats.searchMean);
                System.out.printf("Delete: %.2fx\n", bstStats.deleteMean / rbtStats.deleteMean);
                System.out.printf("Sort:   %.2fx\n", bstStats.sortMean / rbtStats.sortMean);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

}