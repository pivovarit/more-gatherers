package com.pivovarit.gatherers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

record GroupingByGatherer<T, K, R>(Function<? super T, ? extends K> classifier,
                                   Collector<? super T, ?, ? extends R> collector)
  implements Gatherer<T, HashMap<K, Stream.Builder<T>>, Map.Entry<K, R>> {

    GroupingByGatherer {
        Objects.requireNonNull(classifier, "classifier can't be null");
        Objects.requireNonNull(collector, "collector can't be null");
    }

    @Override
    public Supplier<HashMap<K, Stream.Builder<T>>> initializer() {
        return HashMap::new;
    }

    @Override
    public Integrator<HashMap<K, Stream.Builder<T>>, T, Map.Entry<K, R>> integrator() {
        return Integrator.ofGreedy((state, element, _) -> {
            state.computeIfAbsent(classifier.apply(element), _ -> Stream.builder()).accept(element);
            return true;
        });
    }

    @Override
    public BiConsumer<HashMap<K, Stream.Builder<T>>, Downstream<? super Map.Entry<K, R>>> finisher() {
        return (map, downstream) -> map.forEach((key, builder) -> downstream.push(Map.entry(key, builder.build()
          .collect(collector))));
    }
}
