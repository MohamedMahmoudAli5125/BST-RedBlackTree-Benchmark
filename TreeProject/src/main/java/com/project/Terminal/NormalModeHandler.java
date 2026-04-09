package com.project.Terminal;


import com.project.bst.BinarySearchTree;
import com.project.rbtree.RedBlackTree;
import java.util.Arrays;
import java.util.Scanner;

public class NormalModeHandler {
    public static void run(Scanner scanner) {
        System.out.println("\n--- Starting Normal Mode with Validation ENABLED ---");
        BinarySearchTree bst = new BinarySearchTree(true);
        RedBlackTree rbt = new RedBlackTree(true);

        while (true) {
            System.out.println("\n--- Tree Operations ---");
            System.out.println("1. Insert to BST   2. Delete from BST   3. View BST   4. BST In-Order & Size 5. BST height");
            System.out.println("6. Insert to RBT   7. Delete from RBT   8. View RBT   9. RBT In-Order & Size 10. RBT height");
            System.out.println("11. Exit");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            if (choice == 11) break;

            if (handleViewOperations(choice, bst, rbt)) continue;

            System.out.print("Enter value: ");
            int val = scanner.nextInt();

            switch (choice) {
                case 1 -> { if (bst.insert(val)) System.out.println(val + " inserted."); else System.out.println("Exists."); }
                case 2 -> { if (bst.delete(val)) System.out.println(val + " deleted."); else System.out.println("Not found."); }
                case 6 -> { if (rbt.insert(val)) System.out.println(val + " inserted."); else System.out.println("Exists."); }
                case 7 -> { if (rbt.delete(val)) System.out.println(val + " deleted."); else System.out.println("Not found."); }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static boolean handleViewOperations(int choice, BinarySearchTree bst, RedBlackTree rbt) {
        switch (choice) {
            case 3 -> { bst.printTree(); return true; }
            case 4 -> { printStats(bst.size(), bst.inOrder()); return true; }
            case 5 -> { System.out.println("BST Height: " + bst.height()); return true; }
            case 8 -> { rbt.printTree(); return true; }
            case 9 -> { printStats(rbt.size(), rbt.inOrder()); return true; }
            case 10 -> { System.out.println("RBT Height: " + rbt.height()); return true; }
            default -> { return false; }
        }
    }

    private static void printStats(int size, int[] inorder) {
        System.out.println("Size: " + size);
        System.out.println("In-Order (Top 50): " + (inorder.length > 50 ? Arrays.toString(Arrays.copyOf(inorder, 50)) + "..." : Arrays.toString(inorder)));
    }
}