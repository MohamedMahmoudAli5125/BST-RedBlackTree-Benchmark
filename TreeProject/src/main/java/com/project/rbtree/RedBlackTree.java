package com.project.rbtree;

public class RedBlackTree {
    private final RBTNode T_nil;
    private RBTNode root;
    private int count = 0;

    public RedBlackTree() {
        T_nil = new RBTNode(0);
        T_nil.color = Color.BLACK;
        root = T_nil;
    }

    public boolean insert(int v) {
        if (contains(v)) return false;
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
        return true;
    }

    private void insertFixup(RBTNode z) {
        while (z.parent.color == Color.RED) {
            if (z.parent.parent.left == z.parent) {
                if (z.parent.parent.right.color == Color.RED) {
                    z.parent.parent.color = Color.RED;
                    z.parent.color = Color.BLACK;
                    z.parent.parent.right.color = Color.BLACK;
                    z = z.parent.parent;
                } else {
                    if (z.parent.right == z) {
                        z = z.parent;
                        LeftRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    RightRotate(z.parent.parent);
                }
            } else {
                if (z.parent.parent.left.color == Color.RED) {
                    z.parent.parent.color = Color.RED;
                    z.parent.color = Color.BLACK;
                    z.parent.parent.left.color = Color.BLACK;
                    z = z.parent.parent;
                } else {
                    if (z.parent.left == z) {
                        z = z.parent;
                        RightRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    LeftRotate(z.parent.parent);
                }
            }
        }
        this.root.color = Color.BLACK;
    }

    public void RightRotate(RBTNode node) {
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

    public boolean delete(int v) {
        RBTNode z = root;
        while (z != T_nil) {
            if (z.val == v) break;
            z = (v < z.val) ? z.left : z.right;
        }

        if (z == T_nil) return false;

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
            deleteFixup(x);
        }
        count--;
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
        while (x != root && x.color == Color.BLACK) {
            if (x == x.parent.left) {
                RBTNode w = x.parent.right;

                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    LeftRotate(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == Color.BLACK && w.right.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == Color.BLACK) {
                        w.left.color = Color.BLACK;
                        w.color = Color.RED;
                        RightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    LeftRotate(x.parent);
                    x = root;
                }
            } else {
                RBTNode w = x.parent.left;
                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    RightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == Color.BLACK && w.left.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == Color.BLACK) {
                        w.right.color = Color.BLACK;
                        w.color = Color.RED;
                        LeftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    RightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = Color.BLACK;
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
        return this.count;
    }
}