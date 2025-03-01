package com.phasmidsoftware.dsaipg.util;

import com.phasmidsoftware.dsaipg.adt.pq.PQException;
import com.phasmidsoftware.dsaipg.adt.pq.PriorityQueue;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import static com.phasmidsoftware.dsaipg.util.SortBenchmarkHelper.getWords;
import java.util.function.Supplier;
import com.phasmidsoftware.dsaipg.adt.pq.FibonacciHeap;

public class PQBenchmark {

    public PQBenchmark(Config config) {
        this.config = config;
    }

    public static void main(String[] args) throws IOException {
        Config config = Config.load(PQBenchmark.class);
        logger.info("SortBenchmark.main: " + config.get("huskysort", "version")
                + " with word counts: " + Arrays.toString(args));
        if (args.length == 0) logger.warn("No word counts specified on the command line");
        PQBenchmark benchmark = new PQBenchmark(config);
        System.out.println("with floyd: " + benchmark.insertDeleteN(10000, 1000, true));
        System.out.println("no floyd: " + benchmark.insertDeleteN(10000, 1000, false));
        benchmark.runFullAssignmentBenchmarks();
    }

    private void insertArray(int[] a, boolean floyd) {
        PriorityQueue<Integer> pq =
                new PriorityQueue<Integer>(a.length, true, Comparator.naturalOrder(), floyd);
        Random random = new Random();
        for (int j : a) {
            pq.give(j);
            if (random.nextBoolean()) {}
        }
    }

    private double insertDeleteN(int n, int m, boolean floyd) {
        Random ran = new Random();
        int[] r = new int[n];
        for (int i = 0; i < n; i++) {
            r[i] = ran.nextInt(n);
        }
        Benchmark<Boolean> bm = new Benchmark_Timer<>(
                "testPQwithFloydoff",
                null,
                b -> insertArray(r, floyd),
                null
        );
        return bm.run(true, m);
    }

    public final static TimeLogger[] timeLoggersLinearithmic = {
            new TimeLogger("Raw time per run (mSec): ", null),
            new TimeLogger("Normalized time per run (n log n): ", SortBenchmark::minComparisons)
    };

    final static LazyLogger logger = new LazyLogger(PQBenchmark.class);

    static double meanInversions(int n) {
        return 0.25 * n * (n - 1);
    }

    private static Collection<String> lineAsList(String line) {
        List<String> words = new ArrayList<>();
        words.add(line);
        return words;
    }

    private static Collection<String> getLeipzigWords(String line) {
        return getWords(SortBenchmarkHelper.regexLeipzig, line);
    }

    private static Benchmark<LocalDateTime[]> benchmarkFactory(
            String description, Consumer<LocalDateTime[]> sorter, Consumer<LocalDateTime[]> checker
    ) {
        return new Benchmark_Timer<>(description, xs -> Arrays.copyOf(xs, xs.length), sorter, checker);
    }

    private static final double LgE = Utilities.lg(Math.E);

    boolean isConfigBoolean(String section, String option) {
        return config.getBoolean(section, option);
    }

    private final Config config;

    public void runFullAssignmentBenchmarks() {
        System.out.println("\n===== Four (or Five) Heap Implementations =====");
        List<Integer> data = generateRandomData(16000);
        double timeBin = runOneHeapTest(data, "Binary Heap", 2, false);
        double timeBinFloyd = runOneHeapTest(data, "Binary Heap + Floyd", 2, true);
        double time4ary = runOneHeapTest(data, "4-ary Heap", 4, false);
        double time4aryFloyd = runOneHeapTest(data, "4-ary Heap + Floyd", 4, true);
        double timeFib = runFibonacciHeapTest(data, "Fibonacci Heap");
        System.out.printf("Fibonacci Heap time: %.3f ms\n", timeFib);
    }

    private double runOneHeapTest(List<Integer> data, String name, int d, boolean floyd) {
        List<Integer> spilled = new ArrayList<>();
        Consumer<PriorityQueue<Integer>> fRun = pq -> {
            int M = 4095;
            for (int i = 0; i < M; i++) {
                pq.give(data.get(i));
            }
            for (int i = M; i < 16000; i++) {
                pq.give(data.get(i));
                spilled.add(pq.take());
            }
            for (int i = 0; i < 4000; i++) {
                spilled.add(pq.take());
            }
        };
        Benchmark_Timer<PriorityQueue<Integer>> bench = new Benchmark_Timer<>(name, fRun);
       Supplier<PriorityQueue<Integer>> supplier = () ->
                new PriorityQueue<Integer>(4095, d, true, Integer::compareTo, floyd);


        double time = bench.runFromSupplier(supplier, 5);
        System.out.printf("%s time: %.3f ms\n", name, time);
        Integer highest = spilled.isEmpty() ? null : Collections.max(spilled);
        System.out.println("  Highest priority removed => " + highest);
        return time;
    }

    private double runFibonacciHeapTest(List<Integer> data, String name) {
        List<Integer> spilled = new ArrayList<>();
        Consumer<FibonacciHeap<Integer>> fRun = fib -> {
            int M = 4095;
            for (int i = 0; i < M; i++) {
                fib.give(data.get(i));
            }
            for (int i = M; i < 16000; i++) {
                fib.give(data.get(i));
                spilled.add(fib.take());
            }
            for (int i = 0; i < 4000; i++) {
                spilled.add(fib.take());
            }
        };
        Benchmark_Timer<FibonacciHeap<Integer>> bench = new Benchmark_Timer<>(name, fRun);
        Supplier<FibonacciHeap<Integer>> supplier = FibonacciHeap::new;
        double time = bench.runFromSupplier(supplier, 5);
        Integer highest = spilled.isEmpty() ? null : Collections.max(spilled);
        System.out.printf("  Highest priority removed => %s\n", highest);
        return time;
    }

    private List<Integer> generateRandomData(int size) {
        Random rand = new Random();
        List<Integer> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(rand.nextInt());
        }
        return data;
    }
    
}
