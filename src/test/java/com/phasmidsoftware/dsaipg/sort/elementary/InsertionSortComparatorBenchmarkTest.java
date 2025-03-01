package com.phasmidsoftware.dsaipg.sort.elementary;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class InsertionSortComparatorBenchmarkTest {

    @Test
    public void testGenerateRandomArray() {
        Integer[] randomArray = InsertionSortComparatorBenchmark.generateRandomArray(100);
        assert randomArray.length == 100;
        boolean diverse = Arrays.stream(randomArray).distinct().count() > 1;
        assert diverse;
    }

    @Test
    public void testGenerateOrderedArray() {
        Integer[] orderedArray = InsertionSortComparatorBenchmark.generateOrderedArray(100);
        Integer[] expected = new Integer[100];
        for (int i = 0; i < 100; i++) expected[i] = i;
        assertArrayEquals(expected, orderedArray);
    }

    @Test
    public void testGeneratePartiallyOrderedArray() {
        Integer[] partiallyOrderedArray = InsertionSortComparatorBenchmark.generatePartiallyOrderedArray(100);
        assert partiallyOrderedArray.length == 100;
        boolean validRange = Arrays.stream(partiallyOrderedArray).allMatch(x -> x >= 0 && x < 100);
        assert validRange;
    }

    @Test
    public void testGenerateReverseOrderedArray() {
        Integer[] reverseOrderedArray = InsertionSortComparatorBenchmark.generateReverseOrderedArray(100);
        Integer[] expected = new Integer[100];
        for (int i = 0; i < 100; i++) expected[i] = 100 - i;
        assertArrayEquals(expected, reverseOrderedArray);
    }

}
