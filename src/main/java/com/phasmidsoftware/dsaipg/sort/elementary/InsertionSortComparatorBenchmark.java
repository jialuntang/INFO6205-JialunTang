package com.phasmidsoftware.dsaipg.sort.elementary;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class InsertionSortComparatorBenchmark {

    public static void main(String[] args) {
        int[] sizes = {1000, 2000, 4000, 8000, 16000};
        for (int n : sizes) {
            Integer[] randomArray = generateRandomArray(n);
            Integer[] orderedArray = generateOrderedArray(n);
            Integer[] partiallyOrderedArray = generatePartiallyOrderedArray(n);
            Integer[] reverseOrderedArray = generateReverseOrderedArray(n);

            System.out.println("Array size: " + n);
            benchmarkSort("Random", randomArray);
            benchmarkSort("Ordered", orderedArray);
            benchmarkSort("Partially Ordered", partiallyOrderedArray);
            benchmarkSort("Reverse Ordered", reverseOrderedArray);
        }
    }

    public static void benchmarkSort(String type, Integer[] array) {
        Comparator<Integer> comparator = Integer::compareTo;
        InsertionSortComparator<Integer> sorter = new InsertionSortComparator<>(comparator, array.length, 1);

        long startTime = System.nanoTime();
        sorter.mutatingSort(Arrays.copyOf(array, array.length));
        long endTime = System.nanoTime();

        double duration = (endTime - startTime) / 1_000_000.0;
        System.out.println(type + " array execution time: " + duration + " ms");
    }

    public static Integer[] generateRandomArray(int size) {
        Random random = new Random();
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }
        return array;
    }

    public static Integer[] generateOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    public static Integer[] generatePartiallyOrderedArray(int size) {
        Integer[] array = generateOrderedArray(size);
        Random random = new Random();
        for (int i = 0; i < size / 10; i++) {
            int index1 = random.nextInt(size);
            int index2 = random.nextInt(size);
            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }
        return array;
    }

    public static Integer[] generateReverseOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }
}
