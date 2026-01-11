/*
 * Copyright 2014-2026 Grzegorz Piwowarek, https://4comprehension.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pivovarit.gatherers;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.RunnerException;

public class FilterByIndexBenchmark {

    private static List<Integer> source = Stream.iterate(0, i -> i + 1).limit(10_000_000).toList();

    @Benchmark
    public List<Integer> filterByIndex() {
        return source.stream()
          .gather(MoreGatherers.filteringByIndex((i, _) -> i % 2 == 0))
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
