package com.pivovarit.gatherers;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Gatherer;

import static java.util.stream.Gatherer.Integrator.ofGreedy;
import static java.util.stream.Gatherer.ofSequential;

public final class MoreGatherers {

    private MoreGatherers() {
    }

    public static <T> Gatherer<T, ?, Map.Entry<Long, T>> zipWithIndex() {
        return ofSequential(
          AtomicLong::new,
          ofGreedy((state, element, downstream) -> {
              downstream.push(Map.entry(state.getAndIncrement(), element));
              return true;
          })
        );
    }
}
