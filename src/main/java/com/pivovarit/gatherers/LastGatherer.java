package com.pivovarit.gatherers;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

final class LastGatherer {
    private LastGatherer() {
    }

    static <T> Gatherer<T, ?, T> size(int n) {
        return switch (n) {
            case 1 -> new SingleElementLastGatherer<>();
            default -> new CircularBufferLastGatherer<>(n);
        };
    }

    private record CircularBufferLastGatherer<T>(
      long n) implements Gatherer<T, CircularBufferLastGatherer.AppendOnlyCircularBuffer<T>, T> {

        CircularBufferLastGatherer {
            if (n <= 0) {
                throw new IllegalArgumentException("number of elements can't be lower than one");
            }
        }

        @Override
        public Supplier<AppendOnlyCircularBuffer<T>> initializer() {
            return () -> new AppendOnlyCircularBuffer<>((int) n);
        }

        @Override
        public Integrator<AppendOnlyCircularBuffer<T>, T, T> integrator() {
            return Integrator.ofGreedy((state, element, _) -> {
                state.add(element);
                return true;
            });
        }

        @Override
        public BiConsumer<AppendOnlyCircularBuffer<T>, Downstream<? super T>> finisher() {
            return (state, downstream) -> {
                if (!downstream.isRejecting()) {
                    state.forEach(downstream::push);
                }
            };
        }

        static class AppendOnlyCircularBuffer<T> {
            private final T[] buffer;
            private final int maxSize;
            private final AtomicInteger endIdx = new AtomicInteger(0);
            private final AtomicInteger size = new AtomicInteger(0);

            public AppendOnlyCircularBuffer(int size) {
                this.maxSize = size;
                this.buffer = (T[]) new Object[size];
            }

            public void add(T element) {
                buffer[endIdx.getAndIncrement() % maxSize] = element;
                if (size.get() < maxSize) {
                    size.incrementAndGet();
                }
            }

            public void forEach(Consumer<T> consumer) {
                int startIdx = (endIdx.get() - size.get() + maxSize) % maxSize;
                for (int i = 0; i < size.get(); i++) {
                    consumer.accept(buffer[(startIdx + i) % maxSize]);
                }
            }
        }
    }

    private record SingleElementLastGatherer<T>() implements Gatherer<T, SingleElementLastGatherer.ValueHolder<T>, T> {

        @Override
        public Supplier<ValueHolder<T>> initializer() {
            return ValueHolder::new;
        }

        @Override
        public Integrator<ValueHolder<T>, T, T> integrator() {
            return Integrator.ofGreedy((state, element, _) -> {
                state.value = element;
                state.isSet = true;
                return true;
            });
        }

        @Override
        public BiConsumer<ValueHolder<T>, Downstream<? super T>> finisher() {
            return (state, downstream) -> {
                if (state.isSet && !downstream.isRejecting()) {
                    downstream.push(state.value);
                }
            };
        }

        static class ValueHolder<T> {
            private T value;
            private boolean isSet;
        }
    }
}
