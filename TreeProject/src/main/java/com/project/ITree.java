package com.project;

public interface ITree {
    boolean insert(int v);
    boolean contains(int v);
    boolean delete(int v);
    int size();
    int height();
    int[] inOrder();
    void printTree();
}
