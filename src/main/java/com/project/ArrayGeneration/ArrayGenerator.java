package com.project.ArrayGeneration;

import java.util.Arrays;
import java.util.Random;

public class ArrayGenerator {
    private static final long DEFAULT_SEED = 12345;
    private static final int MAX_ARRAY_SIZE = 100000;
    private static final int DEFAULT_ARRAY_SIZE = 100000;
    private final int arraySize;
    private final int maxValue;
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
        this(DEFAULT_ARRAY_SIZE);
    }

    public ArrayGenerator(int arraySize) {
        if (arraySize <= 0) {
            throw new IllegalArgumentException("Array size must be positive");
        }
        if (arraySize > MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Array size cannot exceed " + MAX_ARRAY_SIZE);
        }
        this.arraySize = arraySize;
        this.maxValue = 10 * this.arraySize;
        this.seededRandom = new Random(DEFAULT_SEED);
    }

    public int[] generateRandom() {
        int[] arr = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            arr[i] = seededRandom.nextInt(maxValue);
        }
        return arr;
    }

    public int[] generateSorted() {
        int[] arr = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            arr[i] = i;
        }
        return arr;
    }

    public int[] generateNearlySorted(DisorderLevel level) {
        int[] arr = generateSorted();
        int numSwaps = (int) (arraySize * level.getPercentage() / 100.0);

        for (int swapsPerformed = 0; swapsPerformed < numSwaps; swapsPerformed++) {
            int i = seededRandom.nextInt(arraySize);
            int j = seededRandom.nextInt(arraySize);
            while (i == j) {
                j = seededRandom.nextInt(arraySize);
            }

            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }

    public int getArraySize() {
        return arraySize;
    }
    public int getMaxValue() {
        return maxValue;
    }

    public static void main(String[] args) {
        System.out.println("Testing with default size:");
        ArrayGenerator gen = new ArrayGenerator();
        System.out.println("Array size: " + gen.getArraySize());
        System.out.println("Max value: " + gen.getMaxValue());

        int[] sorted = gen.generateNearlySorted(DisorderLevel.SORTED);
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
                java.util.Arrays.toString(java.util.Arrays.copyOf(sorted, 20)));

        System.out.println("\nTesting with custom size (50000):");
        ArrayGenerator gen2 = new ArrayGenerator(50000);
        System.out.println("Array size: " + gen2.getArraySize());
        System.out.println("Max value: " + gen2.getMaxValue());

        // ArrayGenerator gen3 = new ArrayGenerator(200000);
    }
}