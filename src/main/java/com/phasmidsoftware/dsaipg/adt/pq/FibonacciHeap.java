
package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.HashSet;
import java.util.Set;

/**
 * Simplified Fibonacci Heap for max-heap usage.
 * You can adapt to min-heap by changing the compare logic or storing negative keys.
 *
 * @param <K> The type of keys stored in this heap, must be Comparable for ordering.
 */
public class FibonacciHeap<K extends Comparable<K>> {

    /**
     * Node structure for Fibonacci heap.
     */
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

    // Pointer to the root of the max-tree (the node with the largest key).
    private Node<K> maxNode;
    // Total number of nodes in the heap.
    private int n;

    /**
     * Insert a new key into this Fibonacci heap. (Max-Heap version)
     */
    public void give(K key) {
        Node<K> x = new Node<>(key);
        // Add node x to the root list
        if (maxNode == null) {
            maxNode = x;
        } else {
            // insert x into root list next to maxNode
            x.left = maxNode;
            x.right = maxNode.right;
            maxNode.right.left = x;
            maxNode.right = x;
            // update max if needed
            if (x.key.compareTo(maxNode.key) > 0) {
                maxNode = x;
            }
        }
        n++;
    }

    /**
     * Extract the node with the largest key. (Max-Heap version)
     * Returns that key or null if empty.
     */
    public K take() {
        Node<K> z = maxNode;
        if (z != null) {
            // For each child of z, add it to root list
            Node<K> child = z.child;
            if (child != null) {
                // we need to iterate through all children
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
            // remove z from root list
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

    /**
     * @return true if this heap is empty
     */
    public boolean isEmpty() {
        return maxNode == null;
    }

    /**
     * @return total number of elements in this Fibonacci heap
     */
    public int size() {
        return n;
    }

    /**
     * Combine roots of the same degree to maintain the Fibonacci Heap property.
     */
    private void consolidate() {
        int arraySize = ((int) Math.floor(Math.log(n) / Math.log(2))) + 1;
        Node<K>[] A = new Node[arraySize];
        for (int i = 0; i < arraySize; i++) {
            A[i] = null;
        }

        // gather all root nodes in a set to iterate easily
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
                    Node<K> temp = x;
                    x = y;
                    y = temp;
                }
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
                    // insert A[i] into root list
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

    /**
     * Make y a child of x.
     */
    private void link(Node<K> y, Node<K> x) {
        // remove y from root list
        y.left.right = y.right;
        y.right.left = y.left;
        // make y child of x
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
