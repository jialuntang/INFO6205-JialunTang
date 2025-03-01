package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.HashSet;
import java.util.Set;

public class FibonacciHeap<K extends Comparable<K>> {

    private static class Node<K> {
        K key;
        Node<K> parent;
        Node<K> child;
        Node<K> left;
        Node<K> right;
        int degree;
        boolean mark;

        Node(K key) {
            this.key = key;
            this.left = this;
            this.right = this;
        }
    }

    private Node<K> maxNode;
    private int n;

    public void give(K key) {
        Node<K> x = new Node<>(key);
        if (maxNode == null) {
            maxNode = x;
        } else {
            x.left = maxNode;
            x.right = maxNode.right;
            maxNode.right.left = x;
            maxNode.right = x;
            if (x.key.compareTo(maxNode.key) > 0) {
                maxNode = x;
            }
        }
        n++;
    }


    public K take() {
        Node<K> z = maxNode;
        if (z != null) {
            Node<K> child = z.child;
            if (child != null) {
                int numKids = z.degree;
                Node<K> tempRight;
                while (numKids > 0) {
                    tempRight = child.right;
                    // remove child from child list
                    child.left.right = child.right;
                    child.right.left = child.left;
                    // add child to root list
                    child.left = maxNode;
                    child.right = maxNode.right;
                    maxNode.right.left = child;
                    maxNode.right = child;
                    child.parent = null;
                    child = tempRight;
                    numKids--;
                }
            }
            z.left.right = z.right;
            z.right.left = z.left;
            if (z == z.right) {
                maxNode = null;
            } else {
                maxNode = z.right;
                consolidate();
            }
            n--;
        }
        return (z == null) ? null : z.key;
    }

    public boolean isEmpty() {
        return maxNode == null;
    }

    public int size() {
        return n;
    }

    private void consolidate() {
        int arraySize = ((int) Math.floor(Math.log(n) / Math.log(2))) + 1;
        Node<K>[] A = new Node[arraySize];
        for (int i = 0; i < arraySize; i++) {
            A[i] = null;
        }

        Set<Node<K>> rootList = new HashSet<>();
        Node<K> start = maxNode;
        Node<K> x = maxNode;
        if (x != null) {
            do {
                rootList.add(x);
                x = x.right;
            } while (x != start);
        }

        for (Node<K> w : rootList) {
            x = w;
            int d = x.degree;
            while (A[d] != null) {
                Node<K> y = A[d];
                if (x.key.compareTo(y.key) < 0) {
                    Node<K> temp = x;  x = y;  y = temp;}
                link(y, x);
                A[d] = null;
                d++;
            }
            A[d] = x;
        }
        maxNode = null;
        for (int i = 0; i < arraySize; i++) {
            if (A[i] != null) {
                if (maxNode == null) {
                    maxNode = A[i];
                    maxNode.left = maxNode;
                    maxNode.right = maxNode;
                } else {
                    A[i].left = maxNode;
                    A[i].right = maxNode.right;
                    maxNode.right.left = A[i];
                    maxNode.right = A[i];
                    if (A[i].key.compareTo(maxNode.key) > 0) {
                        maxNode = A[i];
                    }
                }
            }
        }
    }

    private void link(Node<K> y, Node<K> x) {
        y.left.right = y.right;
        y.right.left = y.left;
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.left = y;
            y.right = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }
        x.degree++;
        y.mark = false;
    }
}
