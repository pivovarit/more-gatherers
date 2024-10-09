package com.pivovarit.gatherers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Gatherer.Integrator;
import java.util.stream.Stream;

import static java.util.stream.Gatherer.Integrator.ofGreedy;
import static java.util.stream.Gatherer.ofSequential;

public final class MoreGatherers {

    private MoreGatherers() {
    }

    public static <T> Gatherer<T, ?, T> sample(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("sample size can't be lower than 1");
        }
        return n == 1
          ? noop()
          : Gatherer.ofSequential(
          () -> new AtomicLong(),
          Integrator.ofGreedy((state, element, downstream) -> {
                if (state.getAndIncrement() % n == 0) {
                    downstream.push(element);
                }
                return true;
            }
          ));
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

    public static <T1, T2> Gatherer<T1, ?, Map.Entry<T1, T2>> zip(Stream<T2> other) {
        return zip(other.iterator());
    }

    public static <T1, T2, R> Gatherer<T1, ?, R> zip(Stream<T2> other, BiFunction<? super T1, ? super T2, ? extends R> mapper) {
        return zip(other.iterator(), mapper);
    }

    public static <T1, T2> Gatherer<T1, ?, Map.Entry<T1, T2>> zip(Collection<T2> other) {
        return zip(other.iterator());
    }

    public static <T1, T2, R> Gatherer<T1, ?, R> zip(Collection<T2> other, BiFunction<? super T1, ? super T2, ? extends R> mapper) {
        return zip(other.iterator(), mapper);
    }

    public static <T1, T2> Gatherer<T1, ?, Map.Entry<T1, T2>> zip(Iterator<T2> iterator) {
        return zip(iterator, Map::entry);
    }

    public static <T1, T2, R> Gatherer<T1, ?, R> zip(Iterator<T2> iterator, BiFunction<? super T1, ? super T2, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return Gatherer.ofSequential(
          () -> iterator,
          (state, element, downstream) -> {
              if (state.hasNext()) {
                  downstream.push(mapper.apply(element, state.next()));
                  return true;
              } else {
                  return false;
              }
          });
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

    static <T> Gatherer<T, ?, T> noop() {
        return ofSequential(
          () -> null,
          (_, element, downstream) -> {
              downstream.push(element);
              return true;
          }
        );
    }
}
