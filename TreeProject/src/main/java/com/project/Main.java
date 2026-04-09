//package com.project;
//
//import com.project.bst.BinarySearchTree;
//import com.project.rbtree.RedBlackTree;
//import java.util.Arrays;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        BinarySearchTree bst = new BinarySearchTree(true);
//        RedBlackTree rbt = new RedBlackTree(true);
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            System.out.println("\n--- Tree Operations ---");
//            System.out.println("1. Insert to BST   2. Delete from BST   3. View BST   4. BST In-Order & Size 5. BST height");
//            System.out.println("6. Insert to RBT   7. Delete from RBT   8. View RBT   9. RBT In-Order & Size 10. RBT height");
//            System.out.println("11. Exit");
//            System.out.print("Choice: ");
//
//            int choice = scanner.nextInt();
//            if (choice == 11) break;
//
//            if (choice == 3) {
//                System.out.println("\nBinary Search Tree Structure:");
//                bst.printTree();
//                continue;
//            }
//            if (choice == 4) {
//                System.out.println("BST Size: " + bst.size() );
//                System.out.println("BST In-Order: " + Arrays.toString(bst.inOrder()));
//                continue;
//            }
//            if (choice == 8) {
//                System.out.println("\nRed-Black Tree Structure:");
//                rbt.printTree();
//                continue;
//            }
//            if (choice == 9) {
//                System.out.println("RBT Size: " + rbt.size() );
//                System.out.println("RBT In-Order: " + Arrays.toString(rbt.inOrder()));
//                continue;
//            }
//            if (choice == 5) {
//                System.out.println("BST Height: " + bst.height() );
//                continue;
//            }
//            if (choice == 10) {
//                System.out.println("RBT Height: " + rbt.height() );
//                continue;
//            }
//
//            System.out.print("Enter value: ");
//            int val = scanner.nextInt();
//
//            switch (choice) {
//                case 1:
//                    if (bst.insert(val)) System.out.println(val + " inserted into BST.");
//                    else System.out.println(val + " already exists in BST.");
//                    break;
//                case 2:
//                    if (bst.delete(val)) System.out.println(val + " deleted from BST.");
//                    else System.out.println(val + " not found in BST.");
//                    break;
//                case 6:
//                    if (rbt.insert(val)) System.out.println(val + " inserted into RBT.");
//                    else System.out.println(val + " already exists in RBT.");
//                    break;
//                case 7:
//                    if (rbt.delete(val)) System.out.println(val + " deleted from RBT.");
//                    else System.out.println(val + " not found in RBT.");
//                    break;
//                default:
//                    System.out.println("Invalid choice.");
//            }
//        }
//        System.out.println("Program terminated.");
//        scanner.close();
//    }
//}