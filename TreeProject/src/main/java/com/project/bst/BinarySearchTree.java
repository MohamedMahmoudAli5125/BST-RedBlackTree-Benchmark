package com.project.bst;

public class BinarySearchTree {
    private BSTNode root;
    private int count = 0;

    public BinarySearchTree() {
        this.root = null;
    }

    public boolean insert(int v) {
        if (contains(v)) {
            return false;
        }
        root = insertRec(root, v);
        count++;
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
        if (!contains(v)) {
            return false;
        }
        root = deleteRec(root, v);
        count--;
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
        BSTNode current = root;
        while (current != null) {
            if (v == current.value) {
                return true;
            }
            current = (v < current.value) ? current.left : current.right;
        }
        return false;
    }

    public int[] inOrder() {
        int[] result = new int[this.size()];
        int[] counter = {0};
        inOrderRec(root, result, counter);
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
        return this.count;
    }
    public int height(){
        return heightRec(root);
    }
    public int heightRec(BSTNode node){
        if(node == null){
            return  -1 ;
        }
        return 1 +  Math.max(heightRec(node.left) , heightRec(node.right));
    }
}