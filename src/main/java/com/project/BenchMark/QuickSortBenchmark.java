package com.project.BenchMark;

public class QuickSortBenchmark {
    private QuickSortBenchmark() {
    }

    public static Stats collectSortOnly(BenchmarkData data, int runs) {
        double[] sortTimes = new double[runs];

        // JVM warmup
        quickSort(data.insertData.clone());

        for (int i = 0; i < runs; i++) {
            int[] working = data.insertData.clone();
            long start = System.nanoTime();
            quickSort(working);
            sortTimes[i] = (System.nanoTime() - start) / 1_000_000.0;
        }

        Stats stats = new Stats();
        stats.height = -1;
        stats.sortMean = BenchmarkStats.getMean(sortTimes);
        stats.sortMedian = BenchmarkStats.getMedian(sortTimes);
        stats.sortStdDev = BenchmarkStats.getStdDev(sortTimes);
        printReport("QuickSort", "Sort   ", sortTimes);

        return stats;
    }
    private static void printReport(String algo, String op, double[] times) {
        System.out.printf("[%s %s] Mean: %.4f ms | Median: %.4f ms | StdDev: %.4f ms\n",
                algo, op, BenchmarkStats.getMean(times),
                BenchmarkStats.getMedian(times), BenchmarkStats.getStdDev(times));
    }

    private static int[] quickSort(int[] array) {
        quickSortHelper(array, 0, array.length - 1);
        return array;
    }

    private static void quickSortHelper(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSortHelper(array, low, pi - 1);
            quickSortHelper(array, pi + 1, high);
        }
    }

    private static int partition(int[] array, int low, int high) {
        int randomizedPivotIndex = low + (int) (Math.random() * (high - low + 1));
        int temp2 = array[low];
        array[low] = array[randomizedPivotIndex];
        array[randomizedPivotIndex] = temp2;

        int pivot = array[low];
        int i = low + 1;
        for (int j = low + 1; j <= high; j++) {
            if (array[j] < pivot) {
                int temp1 = array[i];
                array[i] = array[j];
                array[j] = temp1;
                i++;
            }
        }

        int temp = array[low];
        array[low] = array[i - 1];
        array[i - 1] = temp;
        return i - 1;
    }
}
