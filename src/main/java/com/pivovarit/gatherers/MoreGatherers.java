package com.pivovarit.gatherers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
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
        return LastGatherer.size(n);
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
    public static <T> Gatherer<T, ?, Map.Entry<T, Long>> zipWithIndex() {
        return new ZipWithIndexGatherer<>();
    }

    /**
     * Creates a {@link Gatherer} that collects elements into sliding windows of a specified size.
     * Each window captures a subset of elements from the input, and windows slide by a specified step.
     *
     * <p>For example, if the window size is 3 and the step is 1, the gatherer will collect
     * windows of size 3, sliding by 1 element at a time. This means each subsequent window overlaps
     * with the previous one by two elements.</p>
     *
     * <p>Common use cases include moving averages, trend analysis, and any scenario requiring
     * overlapping or rolling window operations on a list of elements.</p>
     *
     * @param <T>       the type of elements in the input and output list
     * @param windowSize the size of each window (must be a positive integer)
     * @param step       the number of elements to slide the window by (must be a positive integer)
     *
     * @return a {@link Gatherer} that collects elements into sliding windows
     *
     * @throws IllegalArgumentException if {@code windowSize} is less than one or {@code step} is less than zero, or greater than {@code windowSize}
     * @apiNote this {@link Gatherer} extends {@link java.util.stream.Gatherers#windowSliding(int)} by allowing to customize the step
     */
    public static <T> Gatherer<T, ?, List<T>> windowSliding(int windowSize, int step) {
        return new WindowSlidingGatherer<>(windowSize, step);
    }

    /**
     * Creates a {@link Gatherer} that filters elements based on their index and value using a given {@link BiPredicate}.
     * The provided {@code BiPredicate} is applied to each element of the source, along with its corresponding index
     * (starting from 0). Only the elements that satisfy the predicate (i.e., for which the predicate returns {@code true})
     * are retained.
     *
     * @param <T>       the type of elements to be filtered
     * @param predicate a {@link BiPredicate} that takes the index and element as input, and returns {@code true} to retain
     *                  the element, or {@code false} to exclude it
     *
     * @return a {@link Gatherer} that applies the given filter based on element index and value
     *
     * @apiNote The same result can be achieved by using {@code zipWithIndex()}, {@code filter()}, and {@code map()}.
     * However, this method is significantly faster because it avoids the intermediate steps and directly filters
     * elements based on their index.
     */
    public static <T> Gatherer<T, ?, T> byIndex(BiPredicate<Long, ? super T> predicate) {
        return new FilterByIndexGatherer<>(predicate);
    }
}
