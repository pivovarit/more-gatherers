/*
 * Copyright 2014-2026 Grzegorz Piwowarek, https://4comprehension.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pivovarit.gatherers;

import java.util.function.BiConsumer;
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
      int n) implements Gatherer<T, CircularBufferLastGatherer.AppendOnlyCircularBuffer<T>, T> {

        CircularBufferLastGatherer {
            if (n <= 0) {
                throw new IllegalArgumentException("number of elements can't be lower than one");
            }
        }

        @Override
        public Supplier<AppendOnlyCircularBuffer<T>> initializer() {
            return () -> new AppendOnlyCircularBuffer<>(n);
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
            return AppendOnlyCircularBuffer::pushAll;
        }

        static final class AppendOnlyCircularBuffer<T> {
            private final Object[] buffer;
            private final int mask;
            private final int limit;

            private int size;
            private int write;

            AppendOnlyCircularBuffer(int limit) {
                this.limit = Math.max(0, limit);
                int capacity = nextPowerOfTwo(Math.max(1, this.limit));
                this.buffer = new Object[capacity];
                this.mask = capacity - 1;
            }

            void add(T e) {
                buffer[write & mask] = e;
                write++;
                if (size < limit) {
                    size++;
                }
            }

            @SuppressWarnings("unchecked")
            T getFromOldest(int index) {
                int start = (write - size) & mask;
                return (T) buffer[(start + index) & mask];
            }

            void pushAll(Gatherer.Downstream<? super T> ds) {
                for (int i = 0; i < size && !ds.isRejecting(); i++) {
                    if (!ds.push(getFromOldest(i))) {
                        break;
                    }
                }
            }

            private static int nextPowerOfTwo(int x) {
                int highest = Integer.highestOneBit(x);
                return (x == highest) ? x : (highest << 1);
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
