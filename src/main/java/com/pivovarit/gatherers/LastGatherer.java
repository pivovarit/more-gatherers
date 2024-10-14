package com.pivovarit.gatherers;

import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record LastGatherer<T>(long n) implements Gatherer<T, LinkedList<T>, T> {

    LastGatherer {
        if (n <= 0) {
            throw new IllegalArgumentException("number of elements can't be lower than one");
        }
    }

    @Override
    public Supplier<LinkedList<T>> initializer() {
        return LinkedList::new;
    }

    @Override
    public Integrator<LinkedList<T>, T, T> integrator() {
        return Integrator.ofGreedy((state, element, _) -> {
            if (state.size() == n) {
                state.removeFirst();
                state.addLast(element);
            } else {
                state.addLast(element);
            }
            return true;
        });
    }

    @Override
    public BiConsumer<LinkedList<T>, Downstream<? super T>> finisher() {
        return (state, downstream) -> state.forEach(downstream::push);
    }
}
