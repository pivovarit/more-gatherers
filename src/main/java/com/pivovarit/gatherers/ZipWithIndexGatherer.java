package com.pivovarit.gatherers;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record ZipWithIndexGatherer<T>() implements Gatherer<T, AtomicLong, Map.Entry<T, Long>> {

    @Override
    public Supplier<AtomicLong> initializer() {
        return AtomicLong::new;
    }

    @Override
    public Integrator<AtomicLong, T, Map.Entry<T, Long>> integrator() {
        return Integrator.ofGreedy((state, element, downstream) -> {
            downstream.push(Map.entry(element, state.getAndIncrement()));
            return true;
        });
    }
}
