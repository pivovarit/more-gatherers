package com.pivovarit.gatherers;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record FilterByIndexGatherer<T>(BiPredicate<Long, ? super T> predicate) implements Gatherer<T, AtomicLong, T> {

    FilterByIndexGatherer {
        Objects.requireNonNull(predicate);
    }

    @Override
    public Supplier<AtomicLong> initializer() {
        return AtomicLong::new;
    }

    @Override
    public Integrator<AtomicLong, T, T> integrator() {
        return Integrator.ofGreedy((seq, t, downstream) -> {
            if (predicate.test(seq.getAndIncrement(), t)) {
                return downstream.push(t);
            }
            return true;
        });
    }
}
