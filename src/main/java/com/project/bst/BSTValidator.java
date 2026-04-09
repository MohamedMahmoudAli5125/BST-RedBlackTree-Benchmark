package com.project.bst;

public class BSTValidator {

    public static void check(BSTNode root, int count) {
        checkBSTProperty(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
        int actualCount = countNodes(root);
        if (count != actualCount) {
            throw new AssertionError("Size mismatch: " + count + " vs " + actualCount);
        }
    }

    private static void checkBSTProperty(BSTNode node, int min, int max) {
        if (node == null) return;
        if (node.value <= min || node.value >= max)
            throw new AssertionError("BST Order violation: " + node.value);

        checkBSTProperty(node.left, min, node.value);
        checkBSTProperty(node.right, node.value, max);
    }

    private static int countNodes(BSTNode node) {
        return (node == null) ? 0 : 1 + countNodes(node.left) + countNodes(node.right);
    }
}