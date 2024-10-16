package com.pivovarit.gatherers;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.RunnerException;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FilterByIndexBenchmark {

    private static List<Integer> source = Stream.iterate(0, i -> i + 1).limit(10_000_000).toList();

    @Benchmark
    public List<Integer> filterByIndex() {
        return source.stream()
          .gather(MoreGatherers.byIndex((i, _) -> i % 2 == 0))
          .toList();
    }

    @Benchmark
    public List<Integer> zipWithIndexThenFilter() {
        return source.stream()
          .gather(MoreGatherers.zipWithIndex())
          .filter(t -> t.getValue() % 2 == 0)
          .map(Map.Entry::getKey)
          .toList();
    }

    public static void main(String[] ignored) throws RunnerException {
        Benchmarks.run(FilterByIndexBenchmark.class);
    }
}
