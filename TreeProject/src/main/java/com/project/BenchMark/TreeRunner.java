package com.project.BenchMark;

import com.project.ITree;

public class TreeRunner {
    public BenchmarkResult runSinglePass(ITree tree, BenchmarkData data) {
        long start = System.nanoTime();
        for (int val : data.insertData) tree.insert(val);
        double iTime = (System.nanoTime() - start) / 1_000_000.0;

        start = System.nanoTime();
        tree.inOrder();
        double sortTime =( (System.nanoTime() - start) / 1_000_000.0) + iTime; // insert + inorder
        int height = tree.height() ;
        start = System.nanoTime();
        for (int val : data.containsData) tree.contains(val);
        double sTime = (System.nanoTime() - start) / 1_000_000.0;

        start = System.nanoTime();
        for (int val : data.deleteData) tree.delete(val);
        double dTime = (System.nanoTime() - start) / 1_000_000.0;

        return new BenchmarkResult(iTime, sTime, dTime, sortTime, height);
    }
}