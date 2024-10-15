package com.pivovarit.gatherers;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record LastGatherer<T>(
  long n) implements Gatherer<T, LastGatherer.AppendOnlyCircularBuffer<T>, T> {

    LastGatherer {
        if (n <= 0) {
            throw new IllegalArgumentException("number of elements can't be lower than one");
        }
    }

    @Override
    public Supplier<LastGatherer.AppendOnlyCircularBuffer<T>> initializer() {
        return () -> new LastGatherer.AppendOnlyCircularBuffer<>((int) n);
    }

    @Override
    public Integrator<LastGatherer.AppendOnlyCircularBuffer<T>, T, T> integrator() {
        return Integrator.ofGreedy((state, element, _) -> {
            state.add(element);
            return true;
        });
    }

    @Override
    public BiConsumer<LastGatherer.AppendOnlyCircularBuffer<T>, Downstream<? super T>> finisher() {
        return (state, downstream) -> state.forEach(downstream::push);
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
