/*
 * Copyright (c) 2024. Robin Hillyard
 */
package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class PriorityQueue<K> implements Iterable<K> {

    private final boolean max;
    private final int first;
    private final Comparator<K> comparator;
    private final K[] binHeap;
    private int last;
    private final boolean floyd;

    public PriorityQueue(boolean max, Object[] binHeap, int first, int last, Comparator<K> comparator, boolean floyd) {
        this.max = max;
        this.first = first;
        this.comparator = comparator;
        this.last = last;
        this.binHeap = (K[]) binHeap;
        this.floyd = floyd;
    }

    public PriorityQueue(int n, int first, boolean max, Comparator<K> comparator, boolean floyd) {
        this(max, new Object[n + first], first, 0, comparator, floyd);
    }

    public PriorityQueue(int n, boolean max, Comparator<K> comparator, boolean floyd) {
        this(n, 1, max, comparator, floyd);
    }

    public PriorityQueue(int n, boolean max, Comparator<K> comparator) {
        this(n, 1, max, comparator, false);
    }

    public PriorityQueue(int n, Comparator<K> comparator) {
        this(n, 1, true, comparator, true);
    }

    public boolean isEmpty() {
        return last == 0;
    }

    public int size() {
        return last;
    }

    public void give(K key) {
        if (last == binHeap.length - first) last--;
        binHeap[++last + first - 1] = key;
        swimUp(last + first - 1);
    }

    public K take() throws NoSuchElementException {
        if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
        return floyd ? doTake(this::snake) : doTake(this::sink);
    }

    private K doTake(Consumer<Integer> f) {
        K result = binHeap[first];
        swap(first, last-- + first - 1);
        f.accept(first);
        binHeap[last + first] = null;
        return result;
    }

    private void sink(int k) {
        doHeapify(k, (a, b) -> !unordered(a, b));
    }

    private void snake(int k) {
        swimUp(doHeapify(k, (a, b) -> !unordered(a, b)));
    }

    private void swimUp(int k) {
        int i = k;
        while (i > first && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private boolean unordered(int i, int j) {
        return (comparator.compare(binHeap[i], binHeap[j]) > 0) ^ max;
    }

    private int doHeapify(int k, BiPredicate<Integer, Integer> p) {
        int i = k;
        while (firstChild(i) <= last + first - 1) {
            int j = firstChild(i);
            if (j < last + first - 1 && unordered(j, j + 1)) j++;
            if (p.test(i, j)) break;
            swap(i, j);
            i = j;
        }
        return i;
    }

    private void swap(int i, int j) {
        K tmp = binHeap[i];
        binHeap[i] = binHeap[j];
        binHeap[j] = tmp;
    }

    private int parent(int k) {
        return (k - first) / 2 + first;
    }

    private int firstChild(int k) {
        return (k - first) * 2 + first + 1;
    }

    @Override
    public Iterator<K> iterator() {
        Collection<K> copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(binHeap, last + first)));
        Iterator<K> result = copy.iterator();
        if (first > 0) result.next();
        return result;
    }

    public static void main(String[] args) {
        doMain();
    }

    static void doMain() {
        String[] s1 = {"A", "B", "C", "D", "E"};
        boolean max = true;
        boolean floyd = true;
        Iterable<String> PQ_string_floyd = new PriorityQueue<>(max, s1, 1, 5, Comparator.comparing(String::toString), floyd);
        Iterable<String> PQ_string_nofloyd = new PriorityQueue<>(max, s1, 1, 5, Comparator.comparing(String::toString), false);
        Integer[] s2 = {0, 1, 2, 3, 4};
        Iterable<Integer> PQ_int_floyd = new PriorityQueue<>(max, s2, 1, 5, Comparator.comparing(Integer::intValue), floyd);
        Iterable<Integer> PQ_int_nofloyd = new PriorityQueue<>(max, s2, 1, 5, Comparator.comparing(Integer::intValue), false);
        System.out.println("Start benchmark...");
// run your benchmark
        System.out.println("Benchmark finished!");

    }

}
