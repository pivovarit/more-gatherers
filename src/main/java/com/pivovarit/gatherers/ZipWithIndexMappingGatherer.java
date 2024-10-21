package com.pivovarit.gatherers;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record ZipWithIndexMappingGatherer<T, R>(BiFunction<Long, ? super T, ? extends R> mapper)
  implements Gatherer<T, AtomicLong, R> {

    ZipWithIndexMappingGatherer {
        Objects.requireNonNull(mapper, "mapper can't be null");
    }

    @Override
    public Supplier<AtomicLong> initializer() {
        return AtomicLong::new;
    }

    @Override
    public Integrator<AtomicLong, T, R> integrator() {
        return Integrator.ofGreedy((state, element, downstream) -> downstream.push(mapper.apply(state.getAndIncrement(), element)));
    }
}
