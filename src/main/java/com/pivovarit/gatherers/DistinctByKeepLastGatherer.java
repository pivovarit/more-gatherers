package com.pivovarit.gatherers;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record DistinctByKeepLastGatherer<T, U>(
  Function<? super T, ? extends U> keyExtractor) implements Gatherer<T, LinkedHashMap<U, T>, T> {

    DistinctByKeepLastGatherer {
        Objects.requireNonNull(keyExtractor, "keyExtractor can't be null");
    }

    @Override
    public Supplier<LinkedHashMap<U, T>> initializer() {
        return LinkedHashMap::new;
    }

    @Override
    public Integrator<LinkedHashMap<U, T>, T, T> integrator() {
        return (state, element, _) -> {
            state.put(keyExtractor.apply(element), element);
            return true;
        };
    }

    @Override
    public BiConsumer<LinkedHashMap<U, T>, Downstream<? super T>> finisher() {
        return (state, downstream) -> {
            for (T element : state.values()) {
                downstream.push(element);
            }
        };
    }
}
