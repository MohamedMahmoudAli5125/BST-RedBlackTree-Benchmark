package com.project.BenchMark;

public class BenchmarkStats {
    public static double getMean(double[] results) {
        double sum = 0;
        for (double d : results) sum += d;
        return sum / results.length;
    }

    public static double getMedian(double[] results) {
        double[] copy = results.clone();
        java.util.Arrays.sort(copy);
        if(results.length % 2  ==0){
            return (double) (copy[copy.length /2 ] + copy[copy.length / 2 - 1] )/ 2 ;
        }
        return copy[copy.length / 2];
    }

    public static double getStdDev(double[] results) {
        double mean = getMean(results);
        double temp = 0;
        for (double d : results) temp += (d - mean) * (d - mean);
        return Math.sqrt(temp /( results.length - 1 ));
    }
}