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
            System.out.println("\n--- Tree Operations ---");
            System.out.println("1. Insert to BST   2. Delete from BST   3. View BST");
            System.out.println("4. Insert to RBT   5. Delete from RBT   6. View RBT");
            System.out.println("7. Exit");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();

            if (choice == 7) break;

            if (choice == 3) {
                System.out.println("\nBinary Search Tree Structure:");
                bst.printTree();
                continue;
            }
            if (choice == 6) {
                System.out.println("\nRed-Black Tree Structure:");
                rbt.printTree();
                continue;
            }

            System.out.print("Enter value: ");
            int val = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (bst.insert(val)) System.out.println(val + " inserted into BST.");
                    else System.out.println(val + " already exists in BST.");
                    break;
                case 2:
                    if (bst.delete(val)) System.out.println(val + " deleted from BST.");
                    else System.out.println(val + " not found in BST.");
                    break;
                case 4:
                    if (rbt.insert(val)) System.out.println(val + " inserted into RBT.");
                    else System.out.println(val + " already exists in RBT.");
                    break;
                case 5:
                    if (rbt.delete(val)) System.out.println(val + " deleted from RBT.");
                    else System.out.println(val + " not found in RBT.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        System.out.println("Program terminated.");
        scanner.close();
    }
}