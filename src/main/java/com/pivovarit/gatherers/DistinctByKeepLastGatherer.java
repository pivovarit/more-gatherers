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

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record DistinctByKeepLastGatherer<T, U>(
  Function<? super T, ? extends U> keyExtractor) implements Gatherer<T, LinkedHashMap<U, T>, T> {

    DistinctByKeepLastGatherer {
        Objects.requireNonNull(keyExtractor, "keyExtractor can't be null");
    }

    @Override
    public Supplier<LinkedHashMap<U, T>> initializer() {
        return LinkedHashMap::new;
    }

    @Override
    public Integrator<LinkedHashMap<U, T>, T, T> integrator() {
        return Integrator.ofGreedy((state, element, _) -> {
            state.putLast(keyExtractor.apply(element), element);
            return true;
        });
    }

    @Override
    public BiConsumer<LinkedHashMap<U, T>, Downstream<? super T>> finisher() {
        return (state, downstream) -> {
            for (T element : state.sequencedValues()) {
                downstream.push(element);
            }
        };
    }
}
