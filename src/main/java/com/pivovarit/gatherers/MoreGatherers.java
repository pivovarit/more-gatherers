package com.pivovarit.gatherers;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Gatherer;

import static java.util.stream.Gatherer.Integrator.ofGreedy;
import static java.util.stream.Gatherer.ofSequential;

public final class MoreGatherers {

    private MoreGatherers() {
    }

    public static <T, U> Gatherer<T, ?, T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return Gatherer.ofSequential(
          () -> new HashSet<U>(),
          (state, element, downstream) -> {
              if (state.add(keyExtractor.apply(element))) {
                  downstream.push(element);
              }
              return true;
          }
        );
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
