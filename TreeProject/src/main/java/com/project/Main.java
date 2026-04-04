package com.project;

import com.project.bst.BinarySearchTree;
import com.project.rbtree.RedBlackTree;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        RedBlackTree rbt = new RedBlackTree();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Insert to BST\n2. Insert to RBT\n3. View RBT Structure\n4. Exit");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();

            if (choice == 4) {
                break;
            }

            if (choice == 3) {
                System.out.println("Current Red-Black Tree Structure:");
                rbt.printTree();
                continue;
            }
            System.out.print("Enter value: ");
            int val = scanner.nextInt();

            if (choice == 1) {
                boolean added = bst.insert(val);
                if (added) {
                    System.out.println(val + " added to BST");
                } else {
                    System.out.println(val + " exists in BST");
                }
            } else if (choice == 2) {
                boolean added = rbt.insert(val);
                if (added) {
                    System.out.println(val + " added to RBT");
                } else {
                    System.out.println(val + " exists in RBT");
                }
            }
        }
        System.out.println("Program terminated.");
        scanner.close();
    }
}