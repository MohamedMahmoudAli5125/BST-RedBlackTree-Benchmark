package com.project.BenchMark;

import com.project.ITree;
import com.project.bst.BinarySearchTree;
import com.project.rbtree.RedBlackTree;

public class BenchmarkCollecter {
    private final TreeRunner runner = new TreeRunner();
    private static final int MIN_RUNS = 5;
    private static final int DEFAULT_RUNS = 5;
    private final int runs;

    public BenchmarkCollecter() {
        this(DEFAULT_RUNS);
    }

    public BenchmarkCollecter(int runs) {
        if (runs < MIN_RUNS) {
            throw new IllegalArgumentException("Number of runs must be at least " + MIN_RUNS +
                    ". Provided: " + runs);
        }
        this.runs = runs;
    }
    public Stats collect(String label, ITree treePrototype, BenchmarkData data) {
        double[] iTimes = new double[runs];
        double[] sTimes = new double[runs];
        double[] dTimes = new double[runs];
        double[] sortTimes = new double[runs];
        int InitialHeight = 0;

        // JVM Warmup
        runner.runSinglePass(createNewInstance(treePrototype), data);

        for (int i = 0; i < runs; i++) {
            BenchmarkResult res = runner.runSinglePass(createNewInstance(treePrototype), data);
            iTimes[i] = res.insertTime;
            sTimes[i] = res.searchTime;
            dTimes[i] = res.deleteTime;
            sortTimes[i] = res.sortTime;
            InitialHeight = res.height;
        }

        printReport(label, "Insert ", iTimes, InitialHeight);
        printReport(label, "Search ", sTimes, -1);
        printReport(label, "Delete ", dTimes, -1);
        printReport(label, "Sort   ", sortTimes, -1);

        Stats stats = new Stats();

        stats.insertMean = BenchmarkStats.getMean(iTimes);
        stats.searchMean = BenchmarkStats.getMean(sTimes);
        stats.deleteMean = BenchmarkStats.getMean(dTimes);
        stats.sortMean = BenchmarkStats.getMean(sortTimes);
        return stats ;
    }

    private void printReport(String tree, String op, double[] times, int height) {
        String hStr = (height != -1) ? "| Height: " + height : "";
        System.out.printf("[%s %s] Mean: %.4f ms | Median: %.4f ms | StdDev: %.4f ms %s\n",
                tree, op, BenchmarkStats.getMean(times),
                BenchmarkStats.getMedian(times), BenchmarkStats.getStdDev(times), hStr);
    }

    private ITree createNewInstance(ITree prototype) {
        return (prototype instanceof BinarySearchTree) ? new BinarySearchTree() : new RedBlackTree();
    }
    public int getRuns() {
        return runs;
    }
}