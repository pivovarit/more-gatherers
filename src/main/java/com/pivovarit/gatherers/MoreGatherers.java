package com.pivovarit.gatherers;

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

    /**
     * Creates a {@link Gatherer} that gathers the last {@code n} elements.
     *
     * @param <T> the type of the input elements
     * @param n   the number of last elements to gather
     *
     * @return a {@link Gatherer} that collects the last {@code n} elements
     */
    public static <T> Gatherer<T, ?, T> last(int n) {
        return new LastGatherer<>(n);
    }

    /**
     * Creates a {@link Gatherer} that gathers every {@code n}-th element.
     *
     * @param <T> the type of the input elements
     * @param n   the interval at which elements are gathered
     *
     * @return a {@link Gatherer} that samples elements every {@code n}-th element
     */
    public static <T> Gatherer<T, ?, T> sampling(int n) {
        return new SamplingGatherer<>(n);
    }

    /**
     * Creates a {@link Gatherer} that filters elements to ensure that only distinct elements
     * remain, based on a key extracted by the given {@code keyExtractor}, keeping the last
     * occurrence of each distinct key.
     *
     * @param <T>          the type of the input elements
     * @param <U>          the type of the key extracted from the input elements
     * @param keyExtractor the function used to extract the key for distinguishing elements
     *
     * @return a {@link Gatherer} that collects distinct elements by key, keeping the last occurrence
     */
    public static <T, U> Gatherer<T, ?, T> distinctByKeepLast(Function<? super T, ? extends U> keyExtractor) {
        return new DistinctByKeepLastGatherer<>(keyExtractor);
    }

    /**
     * Creates a {@link Gatherer} that filters elements to ensure that only distinct elements
     * remain, based on a key extracted by the given {@code keyExtractor}, keeping the first
     * occurrence of each distinct key.
     *
     * @param <T>          the type of the input elements
     * @param <U>          the type of the key extracted from the input elements
     * @param keyExtractor the function used to extract the key for distinguishing elements
     *
     * @return a {@link Gatherer} that collects distinct elements by key, keeping the first occurrence
     */
    public static <T, U> Gatherer<T, ?, T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        return new DistinctByGatherer<>(keyExtractor);
    }

    /**
     * Creates a {@link Gatherer} that gathers distinct consecutive elements.
     * Elements are considered distinct if they are different from the previously gathered element.
     *
     * @param <T> the type of the input elements
     *
     * @return a {@link Gatherer} that collects distinct consecutive elements
     */
    public static <T> Gatherer<T, ?, T> distinctUntilChanged() {
        return distinctUntilChanged(Function.identity());
    }

    /**
     * Creates a {@link Gatherer} that gathers distinct consecutive elements
     * based on a key extracted by the given {@code keyExtractor}.
     * Elements are considered distinct if the extracted key is different from the previous key.
     *
     * @param <T>          the type of the input elements
     * @param <U>          the type of the key extracted from the input elements
     * @param keyExtractor the function used to extract the key for distinguishing elements
     *
     * @return a {@link Gatherer} that collects distinct consecutive elements by key
     */
    public static <T, U> Gatherer<T, ?, T> distinctUntilChanged(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor can't be null");
        return new DistinctUntilChangedGatherer<>(keyExtractor);
    }

    /**
     * Creates a {@link Gatherer} that zips elements of type {@code T1} with elements from
     * another {@link Stream} of type {@code T2}. The resulting pair is returned as a {@link Map.Entry}.
     *
     * @param <T1>  the type of the first stream elements
     * @param <T2>  the type of the second stream elements
     * @param other the other stream to zip with
     *
     * @return a {@link Gatherer} that pairs elements from the two streams
     */
    public static <T1, T2> Gatherer<T1, ?, Map.Entry<T1, T2>> zip(Stream<T2> other) {
        Objects.requireNonNull(other, "other can't be null");
        return zip(other.iterator());
    }

    /**
     * Creates a {@link Gatherer} that zips elements of type {@code T1} with elements from
     * another {@link Stream} of type {@code T2}, and combines them using the provided {@code mapper}.
     *
     * @param <T1>   the type of the first stream elements
     * @param <T2>   the type of the second stream elements
     * @param <R>    the type of the result produced by the {@code mapper}
     * @param other  the other stream to zip with
     * @param mapper the function that combines elements from both streams
     *
     * @return a {@link Gatherer} that pairs elements from the two streams using the {@code mapper}
     */
    public static <T1, T2, R> Gatherer<T1, ?, R> zip(Stream<T2> other, BiFunction<? super T1, ? super T2, ? extends R> mapper) {
        Objects.requireNonNull(other, "other can't be null");
        Objects.requireNonNull(mapper, "mapper can't be null");
        return zip(other.iterator(), mapper);
    }

    /**
     * Creates a {@link Gatherer} that zips elements of type {@code T1} with elements from
     * another {@link Iterable} of type {@code T2}. The resulting pair is returned as a {@link Map.Entry}.
     *
     * @param <T1>  the type of the first iterable elements
     * @param <T2>  the type of the second iterable elements
     * @param other the other iterable to zip with
     *
     * @return a {@link Gatherer} that pairs elements from the two iterables
     */
    public static <T1, T2> Gatherer<T1, ?, Map.Entry<T1, T2>> zipWithIterable(Iterable<T2> other) {
        Objects.requireNonNull(other, "other can't be null");
        return zip(other.iterator());
    }

    /**
     * Creates a {@link Gatherer} that zips elements of type {@code T1} with elements from
     * another {@link Iterable} of type {@code T2}, and combines them using the provided {@code mapper}.
     *
     * @param <T1>   the type of the first iterable elements
     * @param <T2>   the type of the second iterable elements
     * @param <R>    the type of the result produced by the {@code mapper}
     * @param other  the other iterable to zip with
     * @param mapper the function that combines elements from both iterables
     *
     * @return a {@link Gatherer} that pairs elements from the two iterables using the {@code mapper}
     */
    public static <T1, T2, R> Gatherer<T1, ?, R> zipWithIterable(Iterable<T2> other, BiFunction<? super T1, ? super T2, ? extends R> mapper) {
        Objects.requireNonNull(other, "other can't be null");
        Objects.requireNonNull(mapper, "mapper can't be null");
        return zip(other.iterator(), mapper);
    }

    /**
     * Creates a {@link Gatherer} that zips elements of type {@code T1} with elements from
     * another {@link Iterator} of type {@code T2}. The resulting pair is returned as a {@link Map.Entry}.
     *
     * @param <T1>     the type of the first iterator elements
     * @param <T2>     the type of the second iterator elements
     * @param iterator the iterator to zip with
     *
     * @return a {@link Gatherer} that pairs elements from the two iterators
     */
    public static <T1, T2> Gatherer<T1, ?, Map.Entry<T1, T2>> zip(Iterator<T2> iterator) {
        Objects.requireNonNull(iterator, "iterator can't be null");
        return zip(iterator, Map::entry);
    }

    /**
     * Creates a {@link Gatherer} that zips elements of type {@code T1} with elements from
     * another {@link Iterator} of type {@code T2}, and combines them using the provided {@code mapper}.
     *
     * @param <T1>     the type of the first iterator elements
     * @param <T2>     the type of the second iterator elements
     * @param <R>      the type of the result produced by the {@code mapper}
     * @param iterator the iterator to zip with
     * @param mapper   the function that combines elements from both iterators
     *
     * @return a {@link Gatherer} that pairs elements from the two iterators using the {@code mapper}
     */
    public static <T1, T2, R> Gatherer<T1, ?, R> zip(Iterator<T2> iterator, BiFunction<? super T1, ? super T2, ? extends R> mapper) {
        return new ZipIteratorGatherer<>(iterator, mapper);
    }

    /**
     * Creates a {@link Gatherer} that zips elements with their corresponding index,
     * using the given {@code mapper} to produce the final result.
     *
     * @param <T>    the type of the input elements
     * @param <R>    the type of the result produced by the {@code mapper}
     * @param mapper the function that combines an index and an element to produce the result
     *
     * @return a {@link Gatherer} that pairs elements with their corresponding index using the {@code mapper}
     */
    public static <T, R> Gatherer<T, ?, R> zipWithIndex(BiFunction<Long, ? super T, ? extends R> mapper) {
        return new ZipWithIndexMappingGatherer<>(mapper);
    }

    /**
     * Creates a {@link Gatherer} that zips elements with their corresponding index,
     * producing a {@link Map.Entry} where the key is the index and the value is the element.
     *
     * @param <T> the type of the input elements
     *
     * @return a {@link Gatherer} that pairs elements with their corresponding index
     */
    public static <T> Gatherer<T, ?, Map.Entry<Long, T>> zipWithIndex() {
        return new ZipWithIndexGatherer<>();
    }
}
