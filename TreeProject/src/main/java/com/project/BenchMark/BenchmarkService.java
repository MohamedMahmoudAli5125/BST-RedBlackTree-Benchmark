package com.project.BenchMark;

import com.project.ITree;
import com.project.ArrayGeneration.ArrayGenerator;
import com.project.bst.BinarySearchTree;
import com.project.rbtree.RedBlackTree;

import java.util.Random;

public class BenchmarkService {
    private final ArrayGenerator gen = new ArrayGenerator();
    private final Random random = new Random(12345);
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
        data.containsData = new int[100000];
        for (int i = 0; i < 50000; i++) {
            int idx = random.nextInt(data.insertData.length);
            data.containsData[i] = data.insertData[idx];
        }
        for (int i = 0; i < 50000; i++) {
            // Negative values not in tree
            data.containsData[50000 + i] = -1 - i;
        }

        // Delete Array (20% from inserted)
        data.deleteData = new int[20000];
        for (int i = 0; i < 20000; i++) {
            int idx = random.nextInt(data.insertData.length);
            data.deleteData[i] = data.insertData[idx];
        }

        return data;
    }

//    public void runBenchmark(ITree tree, BenchmarkData data) {
//        System.out.println("\n>> Benchmarking " + tree.getClass().getSimpleName() + " [" + data.name + "]");
//
//        // INSERTION
//        long start = System.nanoTime();
//        for (int val : data.insertData) {
//            tree.insert(val);
//        }
//        long end = System.nanoTime();
//        System.out.printf("Insert:   %.2f ms | Height: %d\n",
//                (end - start) / 1_000_000.0, tree.height());
//
//        // CONTAINS
//        start = System.nanoTime();
//        for (int val : data.containsData) {
//            tree.contains(val);
//        }
//        end = System.nanoTime();
//        System.out.printf("Contains: %.2f ms\n", (end - start) / 1_000_000.0);
//
//        // DELETE
//        start = System.nanoTime();
//        for (int val : data.deleteData) {
//            tree.delete(val);
//        }
//        end = System.nanoTime();
//        System.out.printf("Delete:   %.2f ms\n", (end - start) / 1_000_000.0);
//    }

    // Operation 4: Sorting Benchmark (Build + InOrder) , To be merged isA
//    public void runSortingBenchmark(ITree tree, int[] insertData) {
//        long start = System.nanoTime();
//        for (int val : insertData) tree.insert(val);
//        tree.inOrder();
//        long end = System.nanoTime();
//        System.out.printf("Tree Sort Total: %.2f ms\n", (end - start) / 1_000_000.0);
//    }
    public static void main(String[] args) {
        BenchmarkService service = new BenchmarkService();
        BenchmarkCollecter collector = new BenchmarkCollecter();

        Object[][] testCases = {
                {null, "Random"},
//              {ArrayGenerator.DisorderLevel.SORTED, "Sorted"}, // ERROR : StackOverflowError
                {ArrayGenerator.DisorderLevel.ONE_PERCENT, "1% Disorder"},
                {ArrayGenerator.DisorderLevel.FIVE_PERCENT, "5% Disorder"},
                {ArrayGenerator.DisorderLevel.TEN_PERCENT, "10% Disorder"}
        };

        for (Object[] testCase : testCases) {
            BenchmarkData data = service.prepareData((ArrayGenerator.DisorderLevel) testCase[0], (String) testCase[1]);

            System.gc();
           Stats bstStats = collector.collect("BST", new BinarySearchTree(), data);
           Stats rbtStats = collector.collect("RBT", new RedBlackTree(), data);
            System.out.println("\n--- Speedup (BST / RBT) ---");

            System.out.printf("Insert: %.2fx\n", bstStats.insertMean / rbtStats.insertMean);
            System.out.printf("Search: %.2fx\n", bstStats.searchMean / rbtStats.searchMean);
            System.out.printf("Delete: %.2fx\n", bstStats.deleteMean / rbtStats.deleteMean);
            System.out.printf("Sort:   %.2fx\n", bstStats.sortMean / rbtStats.sortMean);
        }
    }

}