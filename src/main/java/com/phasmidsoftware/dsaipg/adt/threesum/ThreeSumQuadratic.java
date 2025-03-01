package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of ThreeSum using a Quadratic approach (O(N^2)).
 * This method uses a two-pointer technique to find all unique triplets that sum to zero.
 */
public class ThreeSumQuadratic implements ThreeSum {
    /**
     * Construct a ThreeSumQuadratic on a.
     * @param a a sorted array.
     */
    public ThreeSumQuadratic(int[] a) {
        this.a = a;
        length = a.length;
    }

    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++) triples.addAll(getTriples(i));
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    /**
     * Get a list of Triples such that the middle index is the given value j.
     *
     * @param j the index of the middle value.
     * @return a Triple such that
     */
    public List<Triple> getTriples(int j) {
        List<Triple> triples = new ArrayList<>();
        // FIXME : for each candidate, test if a[i] + a[j] + a[k] = 0.
        int l = j - 1;
        int r = j + 1;
        while( l > -1 && r < length){
            int sum = a[l] + a[j] + a[r];
            if(sum == 0){
                triples.add(new Triple(a[l], a[j], a[r]));
                r++;
                l--;
            }
            else if(sum < 0)
                r++;
            else
                l--;
        }
        return triples;
    }
    private final int[] a;
    private final int length;
}