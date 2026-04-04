package com.project.rbtree;

public class RBTNode {
int val ;
RBTNode left ;
RBTNode right ;
RBTNode parent ;
public Color color ;
    public RBTNode(int val , RBTNode sentinel){
        this.val = val;
        this.left = sentinel ;
        this.right = sentinel ;
        this.parent = sentinel ;
        this.color = Color.RED ;

    }
    public RBTNode(int val) {
        this.val = val;
    }
}
