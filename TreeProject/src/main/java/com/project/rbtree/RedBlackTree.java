package com.project.rbtree;

public class RedBlackTree {
    private final RBTNode T_nil;
    private RBTNode root;
    public RedBlackTree() {
        T_nil = new RBTNode(0);
        T_nil.color = Color.BLACK;
        root = T_nil;
    }
    public boolean insert(int v) {
        if (contains(v)) return false;
        RBTNode z = new RBTNode(v,T_nil);
        RBTNode y = T_nil;
        RBTNode x =root ;
        while (x != T_nil){
            y = x ;
            if(z.val < x.val){
                x = x.left ;
            }
            else x = x.right ;
        }
z.parent = y ;
        if(y == T_nil){
            this.root = z ;
        } else if (z.val < y.val) {
y.left = z;
        }
        else{
            y.right = z ;
        }
        insertFixup(z);
        return true ;
    }
    private void insertFixup(RBTNode z) {
        while (z.parent.color == Color.RED){
            if(z.parent.parent.left == z.parent){
              if(z.parent.parent.right.color==Color.RED){
                  z.parent.parent.color = Color.RED ;
                  z.parent.color = Color.BLACK ;
                  z.parent.parent.right.color = Color.BLACK ;
                  z = z.parent.parent ;
              }
              else {
                  if(z.parent.right == z ){
                      z = z.parent ;
                      LeftRotate(z);
                  }
                  z.parent.color = Color.BLACK ;
                  z.parent.parent.color = Color.RED ;
                  RightRotate(z.parent.parent);
              }
            }
            else {
                if(z.parent.parent.left.color==Color.RED){
                    z.parent.parent.color = Color.RED ;
                    z.parent.color = Color.BLACK ;
                    z.parent.parent.left.color = Color.BLACK ;
                    z = z.parent.parent ;
                }
                else {
                    if(z.parent.left == z ){
                        z = z.parent ;
                        RightRotate(z);
                    }
                    z.parent.color = Color.BLACK ;
                    z.parent.parent.color = Color.RED ;
                    LeftRotate(z.parent.parent);
                }
            }
        }
        this.root.color = Color.BLACK ;
    }
public  void  RightRotate(RBTNode node){
    RBTNode y = node.left ;

    node.left = y.right;
    if(y.right != T_nil){
        y.right.parent = node ;
    }

    y.parent = node.parent ;
    if(node.parent == T_nil){
        this.root = y;
    } else if (node.parent.left==node) {
        node.parent.left = y ;

    }else {
        node.parent.right = y ;
    }
    y.right = node ;
    node.parent = y ;

}
    public  void  LeftRotate(RBTNode node){
        RBTNode y = node.right ;

        node.right = y.left;
        if(y.left != T_nil){
            y.left.parent = node ;
        }

        y.parent = node.parent ;
        if(node.parent == T_nil){
            this.root = y;
        } else if (node.parent.left==node) {
            node.parent.left = y ;

        }else {
            node.parent.right = y ;
        }
        y.left = node ;
        node.parent = y ;

    }

    public boolean contains(int v) {
        RBTNode current = root;
        while (current != T_nil) {
            if (v == current.val) return true;
            current = (v < current.val) ? current.left : current.right;
        }
        return false;
    }
    public void printTree() {
        printHelper(this.root, "", true);
    }

    private void printHelper(RBTNode node, String indent, boolean last) {
        if (node != T_nil) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "   ";
            } else {
                System.out.print("L----");
                indent += "|  ";
            }
            String color = (node.color == Color.RED) ? "RED" : "BLACK";
            System.out.println(node.val + " (" + color + ")");

            printHelper(node.left, indent, false);
            printHelper(node.right, indent, true);
        }
    }
}