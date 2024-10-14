package com.pivovarit.gatherers;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record ZipWithIndexGatherer<T>() implements Gatherer<T, AtomicLong, Map.Entry<Long, T>> {

    @Override
    public Supplier<AtomicLong> initializer() {
        return AtomicLong::new;
    }

    @Override
    public Integrator<AtomicLong, T, Map.Entry<Long, T>> integrator() {
        return Integrator.ofGreedy((state, element, downstream) -> {
            downstream.push(Map.entry(state.getAndIncrement(), element));
            return true;
        });
    }
}
