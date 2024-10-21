package com.pivovarit.gatherers;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record DistinctUntilChangedGatherer<T, U>(
  Function<? super T, ? extends U> keyExtractor) implements Gatherer<T, DistinctUntilChangedGatherer.State, T> {

    DistinctUntilChangedGatherer {
        Objects.requireNonNull(keyExtractor, "keyExtractor can't be null");
    }

    @Override
    public Supplier<State> initializer() {
        return State::new;
    }

    @Override
    public Integrator<State, T, T> integrator() {
        return (state, element, downstream) -> {
            U key = keyExtractor.apply(element);
            if (!state.hasValue || !Objects.equals(state.value, key)) {
                state.value = key;
                state.hasValue = true;
                return downstream.push(element);
            }
            return true;
        };
    }

    static class State<U> {
        U value;
        boolean hasValue;
    }
}
