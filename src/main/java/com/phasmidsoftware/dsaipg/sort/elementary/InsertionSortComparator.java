package com.phasmidsoftware.dsaipg.sort.elementary;

import com.phasmidsoftware.dsaipg.sort.Helper;
import com.phasmidsoftware.dsaipg.sort.SortWithHelper;

import java.util.Comparator;

public class InsertionSortComparator<X> extends SortWithHelper<X> {

    public InsertionSortComparator(Helper<X> helper) {
        super(helper);
    }

    protected InsertionSortComparator(String description, Comparator<X> comparator, int N, int nRuns) {
        super(description, comparator, N, nRuns, null);
    }

    public InsertionSortComparator(Comparator<X> comparator, int N, int nRuns) {
        this("Insertion sort", comparator, N, nRuns);
    }

    public void sort(X[] xs, int from, int to) {
        Helper<X> helper = getHelper();
        for (int i = from + 1; i < to; i++) {
            X key = xs[i];
            int j = i - 1;
            while (j >= from && helper.less(key, xs[j])) {
                xs[j + 1] = xs[j];
                j--;
            }
            xs[j + 1] = key;
        }
    }

    public static <T extends Comparable<T>> void sort(T[] ts) {
        InsertionSortComparator<T> sorter = new InsertionSortComparator<>(Comparable::compareTo, ts.length, 1);
        sorter.mutatingSort(ts);
    }
}
