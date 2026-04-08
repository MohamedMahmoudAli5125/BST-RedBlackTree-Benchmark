package com.project.rbtree;

public class RBTValidator {

    public static void check(RBTNode root, RBTNode T_nil, int count) {
        //The root and leaves (NIL’s) are black.
        if (T_nil.color != Color.BLACK)
            throw new AssertionError("T_nil must be BLACK");
        if (root != T_nil && root.color != Color.BLACK)
            throw new AssertionError("Root must be BLACK");

        int[] nodeCount = {0};
        checkNode(root, T_nil, Integer.MIN_VALUE, Integer.MAX_VALUE, nodeCount);
        if (count != nodeCount[0])
            throw new AssertionError("Size mismatch. Expected: " + count + " Found: " + nodeCount[0]);
    }

    private static int checkNode(RBTNode node, RBTNode T_nil, int min, int max, int[] nodeCount) {
        // NIL nodes contribute 1 to black-height
        if (node == T_nil) return 1;

        nodeCount[0]++;

        // BST Property
        if (node.val <= min || node.val >= max)
            throw new AssertionError("BST violation at node: " + node.val);

        // If a node is red, then its parent should be black. (No Red-Red violation)
        if (node.color == Color.RED) {
            if (node.left.color == Color.RED || node.right.color == Color.RED)
                throw new AssertionError("Double RED violation at node: " + node.val);
        }

        // Parent Pointer consistency
        if (node.left != T_nil && node.left.parent != node) throw new AssertionError("Broken link at: " + node.left.val);
        if (node.right != T_nil && node.right.parent != node) throw new AssertionError("Broken link at: " + node.right.val);

        int leftBH = checkNode(node.left, T_nil, min, node.val, nodeCount);
        int rightBH = checkNode(node.right, T_nil, node.val, max, nodeCount);

        // All simple paths from any node x to a descendant leaf have the same number of black nodes = black-height(x).
        if (leftBH != rightBH)
            throw new AssertionError("Black-height mismatch at node: " + node.val);

        return leftBH + (node.color == Color.BLACK ? 1 : 0);
    }
}