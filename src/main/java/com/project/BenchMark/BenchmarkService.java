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
}