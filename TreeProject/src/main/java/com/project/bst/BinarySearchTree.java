package com.project.bst;

import com.project.ITree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BinarySearchTree implements ITree {
    static final boolean DEFAULT_VALIDATE  = false;
    private final boolean validate;
    public BinarySearchTree() {
        this(DEFAULT_VALIDATE);
    }
    public BinarySearchTree(boolean validate) {
        this.root = null;
        this.validate = validate;
        if (validate) {
            logger.info("BinarySearchTree initialized with validation ENABLED");
        } else {
            logger.debug("BinarySearchTree initialized with validation disabled");
        }
    }
    private BSTNode root;
    private int count = 0;

    private static final Logger logger = LoggerFactory.getLogger(BinarySearchTree.class);

    public boolean insert(int v) {
        logger.debug("Attempting to insert value: {}", v);
        if (contains(v)) {
            logger.debug("Insert skipped — duplicate value: {}", v);
            return false;
        }
        root = insertRec(root, v);
        count++;
        logger.debug("Inserted value: {}, new size: {}", v, count);
        if (validate) BSTValidator.check(root, count);
        return true;
    }

    public BSTNode insertRec(BSTNode root, int v) {
        if (root == null) {
            return new BSTNode(v);
        }
        if (v < root.value) {
            root.left = insertRec(root.left, v);
        } else {
            root.right = insertRec(root.right, v);
        }
        return root;

    }

    public boolean delete(int v) {
        logger.debug("Attempting to delete value: {}", v);
        if (!contains(v)) {
            logger.debug("Delete skipped — value not found: {}", v);
            return false;
        }
        root = deleteRec(root, v);
        count--;
        logger.debug("Deleted value: {}, new size: {}", v, count);
        if (validate) BSTValidator.check(root, count);
        return true;
    }

    public BSTNode deleteRec(BSTNode root, int v) {
        if (v < root.value) {
            root.left = deleteRec(root.left, v);
        } else if (v > root.value) {
            root.right = deleteRec(root.right, v);
        } else {
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            BSTNode successor = specialGetSuccessor(root.right);
            root.value = successor.value;

            root.right = deleteRec(root.right, successor.value);
        }
        return root;

    }

    public BSTNode specialGetSuccessor(BSTNode node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    public void printTree() {
        if (root == null) {
            System.out.println("Tree is empty.");
            return;
        }
        System.out.println(root.value);
        printHelper(root.left, "", false);
        printHelper(root.right, "", true);
    }

    private void printHelper(BSTNode node, String indent, boolean last) {
        if (node != null) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "   ";
            } else {
                System.out.print("L----");
                indent += "|  ";
            }
            System.out.println(node.value);
            printHelper(node.left, indent, false);
            printHelper(node.right, indent, true);
        }
    }

    public boolean contains(int v) {
        logger.debug("Checking if tree contains value: {}", v);
        int steps = 0;
        BSTNode current = root;
        while (current != null) {
            steps++ ;
            if (v == current.value) {
                logger.debug("Found value {} after {} comparisons", v, steps);
                return true;
            }
            current = (v < current.value) ? current.left : current.right;
        }
        logger.debug("Value {} not found in tree after {} comparisons", v, steps);
        return false;
    }

    public int[] inOrder() {
        logger.debug("Getting in-order traversal of tree");
        int[] result = new int[this.size()];
        int[] counter = {0};
        inOrderRec(root, result, counter);
        logger.debug("In-order traversal completed, tree size: {}", result.length);
        return result;
    }

    private void inOrderRec(BSTNode node, int[] result, int[] counter) {
        if (node == null) {
            return;
        }
        inOrderRec(node.left, result, counter);
        result[counter[0]++] = node.value;
        inOrderRec(node.right, result, counter);
    }

    public int size() {
        logger.debug("Tree size requested: {}", this.count);
        return this.count;
    }
    public int height(){
        int height = heightRec(root);
        logger.debug("Tree height calculated: {}", height);
        return height;
    }
    public int heightRec(BSTNode node){
        if(node == null){
            return  0 ;
        }
        return 1 +  Math.max(heightRec(node.left) , heightRec(node.right));
    }
    public BSTNode getRoot(){
        return root;
    }

    public int getCount() {
        return count;
    }
}