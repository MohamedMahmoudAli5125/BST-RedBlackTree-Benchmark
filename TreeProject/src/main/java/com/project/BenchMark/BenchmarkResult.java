package com.project.BenchMark;

public class BenchmarkResult {
    public double insertTime;
    public double searchTime;
    public double deleteTime;
    public double sortTime;
    public int height;

    public BenchmarkResult(double i, double s, double d, double sort, int h) {
        this.insertTime = i;
        this.searchTime = s;
        this.deleteTime = d;
        this.sortTime = sort;
        this.height = h;
    }
}