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

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record DistinctByGatherer<T, U>(Function<? super T, ? extends U> keyExtractor) implements Gatherer<T, HashSet<U>, T> {

    DistinctByGatherer {
        Objects.requireNonNull(keyExtractor, "keyExtractor can't be null");
    }

    @Override
    public Supplier<HashSet<U>> initializer() {
        return HashSet::new;
    }

    @Override
    public Integrator<HashSet<U>, T, T> integrator() {
        return (state, element, downstream) -> {
            if (state.add(keyExtractor.apply(element))) {
                return downstream.push(element);
            }
            return true;
        };
    }
}
