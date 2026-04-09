package com.project.rbtree;

import com.project.ITree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedBlackTree implements ITree {
    private final RBTNode T_nil;
    private RBTNode root;
    private int count = 0;
    static final boolean VALIDATE = false;
    private static final Logger logger = LoggerFactory.getLogger(RedBlackTree.class);
    public RedBlackTree() {
        T_nil = new RBTNode(0);
        T_nil.color = Color.BLACK;
        root = T_nil;
    }

    public boolean insert(int v) {
        logger.debug("Attempting to insert value: {}", v);
        if (contains(v)) {
            logger.debug("Value {} already exists in tree, skipping insertion", v);
            return false;
        }
        RBTNode z = new RBTNode(v, T_nil);
        RBTNode y = T_nil;
        RBTNode x = root;
        while (x != T_nil) {
            y = x;
            if (z.val < x.val) {
                x = x.left;
            } else x = x.right;
        }
        z.parent = y;
        if (y == T_nil) {
            this.root = z;
        } else if (z.val < y.val) {
            y.left = z;
        } else {
            y.right = z;
        }
        insertFixup(z);
        count++;
        logger.debug("Successfully inserted value: {}, tree size now: {}", v, count);
        if (VALIDATE) RBTValidator.check(root, T_nil, count);
        return true;
    }

    private void insertFixup(RBTNode z) {
        logger.debug("Insert fixup starting at node: {}", z.val);
        while (z.parent.color == Color.RED) {
            if (z.parent.parent.left == z.parent) {
                logger.debug("Case: parent is left child of grandparent");

                if (z.parent.parent.right.color == Color.RED) {
                    logger.debug("Case 1: Uncle is RED - recoloring nodes");
                    z.parent.parent.color = Color.RED;
                    z.parent.color = Color.BLACK;
                    z.parent.parent.right.color = Color.BLACK;
                    z = z.parent.parent;
                    logger.debug("Moved up to grandparent node: {}", z.val);
                } else {
                    if (z.parent.right == z) {
                        logger.debug("Case 2: Node is right child - performing left rotation on parent: {}", z.parent.val);
                        z = z.parent;
                        LeftRotate(z);
                    }
                    logger.debug("Case 3: Node is left child - performing right rotation on grandparent: {}", z.parent.parent.val);
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    RightRotate(z.parent.parent);
                }
            } else {
                logger.debug("Case: parent is right child of grandparent");
                if (z.parent.parent.left.color == Color.RED) {
                    logger.debug("Case 1: Uncle is RED - recoloring nodes");
                    z.parent.parent.color = Color.RED;
                    z.parent.color = Color.BLACK;
                    z.parent.parent.left.color = Color.BLACK;
                    z = z.parent.parent;
                    logger.debug("Moved up to grandparent node: {}", z.val);
                } else {
                    if (z.parent.left == z) {
                        logger.debug("Case 2: Node is left child - performing right rotation on parent: {}", z.parent.val);
                        z = z.parent;
                        RightRotate(z);
                    }
                    logger.debug("Case 3: Node is right child - performing left rotation on grandparent: {}", z.parent.parent.val);
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    LeftRotate(z.parent.parent);
                }
            }
        }
        this.root.color = Color.BLACK;
        logger.debug("Insert fixup completed, root set to BLACK");
    }

    public void RightRotate(RBTNode node) {
        logger.debug("Right rotate on node: {}", node.val);
        RBTNode y = node.left;

        node.left = y.right;
        if (y.right != T_nil) {
            y.right.parent = node;
        }

        y.parent = node.parent;
        if (node.parent == T_nil) {
            this.root = y;
        } else if (node.parent.left == node) {
            node.parent.left = y;

        } else {
            node.parent.right = y;
        }
        y.right = node;
        node.parent = y;

    }

    public void LeftRotate(RBTNode node) {
        logger.debug("Left rotate on node: {}", node.val);
        RBTNode y = node.right;

        node.right = y.left;
        if (y.left != T_nil) {
            y.left.parent = node;
        }

        y.parent = node.parent;
        if (node.parent == T_nil) {
            this.root = y;
        } else if (node.parent.left == node) {
            node.parent.left = y;

        } else {
            node.parent.right = y;
        }
        y.left = node;
        node.parent = y;

    }

    public boolean contains(int v) {
        logger.debug("Checking if tree contains value: {}", v);
        int steps = 0;
        RBTNode current = root;
        while (current != T_nil) {
            steps++ ;
            if (v == current.val){
                logger.debug("Found value {} after {} comparisons", v, steps);

                return true;
            }
            current = (v < current.val) ? current.left : current.right;
        }
        logger.debug("Value {} not found in tree after {} comparisons", v, steps);
        return false;
    }

    public void printTree() {
        printHelper(this.root, "", true);
    }

    public boolean delete(int v) {
        logger.debug("Attempting to delete value: {}", v);
        RBTNode z = root;
        while (z != T_nil) {
            if (z.val == v) break;
            z = (v < z.val) ? z.left : z.right;
        }

        if (z == T_nil){
            logger.debug("Value {} not found, deletion failed", v);
            return false;
        }
        logger.debug("Found node {} to delete", v);

        RBTNode x;
        RBTNode y = z;
        Color yOriginalColor = y.color;

        if (z.left == T_nil) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == T_nil) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == Color.BLACK) {
            logger.debug("Deleted node was BLACK, performing delete fixup");
            deleteFixup(x);
        }
        else {
            logger.debug("Deleted node was RED, no fixup needed");
        }
        count--;
        logger.debug("Successfully deleted value: {}, tree size now: {}", v, count);
        if (VALIDATE) RBTValidator.check(root, T_nil, count);
        return true;
    }

    private void transplant(RBTNode u, RBTNode v) {
        if (u.parent == T_nil) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private RBTNode minimum(RBTNode node) {
        while (node.left != T_nil) node = node.left;
        return node;
    }

    private void deleteFixup(RBTNode x) {
        logger.debug("Delete fixup starting at node: {}", x.val);
        while (x != root && x.color == Color.BLACK) {
            if (x == x.parent.left) {
                logger.debug("Case: Node is left child");
                RBTNode w = x.parent.right;

                if (w.color == Color.RED) {
                    logger.debug("Case 1: Sibling is RED - recoloring and rotating");
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    LeftRotate(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == Color.BLACK && w.right.color == Color.BLACK) {
                    logger.debug("Case 2: Both nephews are BLACK - recoloring sibling");
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == Color.BLACK) {
                        logger.debug("Case 3: Right nephew is BLACK - recolor and right rotate");
                        w.left.color = Color.BLACK;
                        w.color = Color.RED;
                        RightRotate(w);
                        w = x.parent.right;
                    }
                    logger.debug("Case 4: Recoloring and left rotation");
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    LeftRotate(x.parent);
                    x = root;

                }
            } else {
                logger.debug("Case: Node is right child");
                RBTNode w = x.parent.left;
                if (w.color == Color.RED) {
                    logger.debug("Case 1: Sibling is RED - recoloring and rotating");
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    RightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == Color.BLACK && w.left.color == Color.BLACK) {
                    logger.debug("Case 2: Both nephews are BLACK - recoloring sibling");
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == Color.BLACK) {
                        logger.debug("Case 3: Left nephew is BLACK - recolor and left rotate");
                        w.right.color = Color.BLACK;
                        w.color = Color.RED;
                        LeftRotate(w);
                        w = x.parent.left;
                    }
                    logger.debug("Case 4: Recoloring and right rotation");
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    RightRotate(x.parent);
                    x = root;

                }
            }
        }
        x.color = Color.BLACK;
        logger.debug("Delete fixup completed, final node colored BLACK");
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

    public int[] inOrder() {
        logger.debug("Getting in-order traversal of tree");
        int[] result = new int[this.size()];
        int[] counter = {0};
        inOrderRec(root, result, counter);
        return result;
    }

    private void inOrderRec(RBTNode node, int[] result, int[] counter) {
        if (node == T_nil) {
            return;
        }
        inOrderRec(node.left, result, counter);
        result[counter[0]++] = node.val;
        inOrderRec(node.right, result, counter);
    }

    public int size() {
        logger.debug("Tree size requested: {}", this.count);
        return this.count;
    }

    public RBTNode getRoot() {
        return root;
    }

    public int getCount() {
        return count;
    }

    public int height(){
        int height = heightRec(root);
        logger.debug("Tree height calculated: {}", height);
        return height;
    }
    public int heightRec(RBTNode node){
        if(node == T_nil){
            return  -1 ;
        }
        return 1 +  Math.max(heightRec(node.left) , heightRec(node.right));
    }

    public RBTNode getT_nil() {
        return T_nil;
    }
}