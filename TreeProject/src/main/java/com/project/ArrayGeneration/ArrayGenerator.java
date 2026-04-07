package com.project.ArrayGeneration;

import java.util.Arrays;
import java.util.Random;

public class ArrayGenerator {
    private static final long DEFAULT_SEED = 12345;
    private static final int DEFAULT_ARRAY_SIZE = 100000;
    private static final int DEFAULT_MAX_VALUE = 1000000;
    private final Random seededRandom;

    public enum DisorderLevel {
        SORTED(0),
        ONE_PERCENT(1),
        FIVE_PERCENT(5),
        TEN_PERCENT(10);

        private final double percentage;

        DisorderLevel(double percentage) {
            this.percentage = percentage;
        }

        public double getPercentage() {
            return percentage;
        }
    }

    public ArrayGenerator() {
        this.seededRandom = new Random(DEFAULT_SEED);
    }

    public int[] generateRandom() {
        int[] arr = new int[DEFAULT_ARRAY_SIZE];
        for (int i = 0; i < DEFAULT_ARRAY_SIZE; i++) {
            arr[i] = seededRandom.nextInt(DEFAULT_MAX_VALUE);
        }
        return arr;
    }

    //public int[] generateNearlySorted(int x){
//int[] arr= generateRandom() ;
//Arrays.sort(arr);
//
//return  arr ;
//}
    public int[] generateSorted() {
        int[] arr = new int[DEFAULT_ARRAY_SIZE];
        for (int i = 0; i < DEFAULT_ARRAY_SIZE; i++) {
            arr[i] = i;
        }
        return arr;
    }

    public int[] generateNearlySorted(DisorderLevel level) {
        int[] arr = generateSorted();
        int numSwaps = (int) (DEFAULT_ARRAY_SIZE * level.getPercentage() / 100.0);

        for (int swapsPerformed = 0; swapsPerformed < numSwaps; swapsPerformed++) {
            int i = seededRandom.nextInt(DEFAULT_ARRAY_SIZE);
            int j = seededRandom.nextInt(DEFAULT_ARRAY_SIZE);
            while (i == j) {
                j = seededRandom.nextInt(DEFAULT_ARRAY_SIZE);
            }

            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }

    public static void main(String[] args) {
        ArrayGenerator gen = new ArrayGenerator();
        int[] Sorted = gen.generateNearlySorted(DisorderLevel.SORTED);

        int[] nearlySorted1 = gen.generateNearlySorted(DisorderLevel.ONE_PERCENT);
        int[] nearlySorted5 = gen.generateNearlySorted(DisorderLevel.FIVE_PERCENT);
        int[] nearlySorted10 = gen.generateNearlySorted(DisorderLevel.TEN_PERCENT);

        System.out.println("Generated arrays with 1%, 5%, and 10% disorder");
        System.out.println("First 20 elements of 1%: " +
                java.util.Arrays.toString(java.util.Arrays.copyOf(nearlySorted1, 20)));
        System.out.println("First 20 elements of 5%: " +
                java.util.Arrays.toString(java.util.Arrays.copyOf(nearlySorted5, 20)));
        System.out.println("First 20 elements of 10%: " +
                java.util.Arrays.toString(java.util.Arrays.copyOf(nearlySorted10, 20)));
        System.out.println("First 20 elements of Sorted: " +
                java.util.Arrays.toString(java.util.Arrays.copyOf(Sorted, 20)));
    }
}
