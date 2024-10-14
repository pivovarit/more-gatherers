package com.pivovarit.gatherers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public final class MoreGatherers {

    private MoreGatherers() {
    }

    public static <T> Gatherer<T, ?, T> last(int n) {
        return new LastGatherer<>(n);
    }

    public static <T> Gatherer<T, ?, T> sampling(int n) {
        return new SamplingGatherer<>(n);
    }

    public static <T, U> Gatherer<T, ?, T> distinctByKeepLast(Function<? super T, ? extends U> keyExtractor) {
        return new DistinctByKeepLastGatherer<>(keyExtractor);
    }

    public static <T, U> Gatherer<T, ?, T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor can't be null");
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

    public static <T> Gatherer<T, ?, T> distinctUntilChanged() {
        return distinctUntilChanged(Function.identity());
    }

    public static <T, U> Gatherer<T, ?, T> distinctUntilChanged(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor can't be null");
        class State {
            U value;
            boolean hasValue;
        }
        return Gatherer.ofSequential(
          State::new,
          (state, element, downstream) -> {
              U key = keyExtractor.apply(element);
              if (!state.hasValue || !Objects.equals(state.value, key)) {
                  state.value = key;
                  state.hasValue = true;
                  downstream.push(element);
              }
              return true;
          }
        );
    }

    public static <T1, T2> Gatherer<T1, ?, Map.Entry<T1, T2>> zip(Stream<T2> other) {
        Objects.requireNonNull(other, "other can't be null");
        return zip(other.iterator());
    }

    public static <T1, T2, R> Gatherer<T1, ?, R> zip(Stream<T2> other, BiFunction<? super T1, ? super T2, ? extends R> mapper) {
        Objects.requireNonNull(other, "other can't be null");
        Objects.requireNonNull(mapper, "mapper can't be null");
        return zip(other.iterator(), mapper);
    }

    public static <T1, T2> Gatherer<T1, ?, Map.Entry<T1, T2>> zipWithIterable(Iterable<T2> other) {
        Objects.requireNonNull(other, "other can't be null");
        return zip(other.iterator());
    }

    public static <T1, T2, R> Gatherer<T1, ?, R> zipWithIterable(Iterable<T2> other, BiFunction<? super T1, ? super T2, ? extends R> mapper) {
        Objects.requireNonNull(other, "other can't be null");
        Objects.requireNonNull(mapper, "mapper can't be null");
        return zip(other.iterator(), mapper);
    }

    public static <T1, T2> Gatherer<T1, ?, Map.Entry<T1, T2>> zip(Iterator<T2> iterator) {
        Objects.requireNonNull(iterator, "iterator can't be null");
        return zip(iterator, Map::entry);
    }

    public static <T1, T2, R> Gatherer<T1, ?, R> zip(Iterator<T2> iterator, BiFunction<? super T1, ? super T2, ? extends R> mapper) {
        return new ZipIteratorGatherer<>(iterator, mapper);
    }

    public static <T, R> Gatherer<T, ?, R> zipWithIndex(BiFunction<Long, ? super T, ? extends R> mapper) {
        return new ZipWithIndexMappingGatherer<>(mapper);
    }

    public static <T> Gatherer<T, ?, Map.Entry<Long, T>> zipWithIndex() {
        return new ZipWithIndexGatherer<>();
    }
}
