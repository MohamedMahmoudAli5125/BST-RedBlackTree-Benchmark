package com.project.bst;
public class BinarySearchTree {
    private BSTNode root;
    public BinarySearchTree() {
        this.root = null;
    }

    public boolean insert(int v) {
     if(contains(v)){
         return false ;
     }
        root = insertRec(root, v);
     return true ;
    }
    public BSTNode insertRec(BSTNode root , int v) {
        if(root == null){
            return new BSTNode(v) ;
        }
        if(v < root.value  ){
           root.left = insertRec(root.left , v );
        }
        else {
            root.right =insertRec(root.right , v);
        }
        return root ;

    }
    public boolean contains(int v ){
        BSTNode current = root ;
        while (current != null){
            if(v == current.value) { return true ;}
            current = (v<current.value )? current.left : current.right ;
            }
            return false ;
        }

}