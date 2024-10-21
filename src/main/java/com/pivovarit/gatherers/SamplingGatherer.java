package com.pivovarit.gatherers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record SamplingGatherer<T>(int n) implements Gatherer<T, AtomicLong, T> {

    SamplingGatherer {
        if (n <= 0) {
            throw new IllegalArgumentException("sampling frequency can't be lower than 1");
        }
    }

    @Override
    public Supplier<AtomicLong> initializer() {
        return AtomicLong::new;
    }

    @Override
    public Integrator<AtomicLong, T, T> integrator() {
        return Integrator.ofGreedy((state, element, downstream) -> {
            if (state.getAndIncrement() % n == 0) {
                return downstream.push(element);
            }
            return true;
        });
    }
}
