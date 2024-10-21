package com.pivovarit.gatherers;

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record DistinctByGatherer<T, U>(Function<? super T, ? extends U> keyExtractor) implements Gatherer<T, HashSet<U>, T> {

    DistinctByGatherer {
        Objects.requireNonNull(keyExtractor, "keyExtractor can't be null");
    }

    @Override
    public Supplier<HashSet<U>> initializer() {
        return HashSet::new;
    }

    @Override
    public Integrator<HashSet<U>, T, T> integrator() {
        return (state, element, downstream) -> {
            if (state.add(keyExtractor.apply(element))) {
                return downstream.push(element);
            }
            return true;
        };
    }
}
