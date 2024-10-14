package com.pivovarit.gatherers;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record ZipIteratorGatherer<T1, T2, R>(Iterator<T2> iterator, BiFunction<? super T1, ? super T2, ? extends R> mapper)
  implements Gatherer<T1, Iterator<T2>, R> {

    ZipIteratorGatherer {
        Objects.requireNonNull(mapper, "mapper can't be null");
        Objects.requireNonNull(iterator, "iterator can't be null");
    }

    @Override
    public Supplier<Iterator<T2>> initializer() {
        return () -> iterator;
    }

    @Override
    public Integrator<Iterator<T2>, T1, R> integrator() {
        return (state, element, downstream) -> {
            if (state.hasNext()) {
                downstream.push(mapper.apply(element, state.next()));
                return true;
            } else {
                return false;
            }
        };
    }
}
