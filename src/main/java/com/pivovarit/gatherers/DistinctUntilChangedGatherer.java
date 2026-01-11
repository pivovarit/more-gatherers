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

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record DistinctUntilChangedGatherer<T, U>(
  Function<? super T, ? extends U> keyExtractor) implements Gatherer<T, DistinctUntilChangedGatherer.State<U>, T> {

    DistinctUntilChangedGatherer {
        Objects.requireNonNull(keyExtractor, "keyExtractor can't be null");
    }

    @Override
    public Supplier<State<U>> initializer() {
        return State::new;
    }

    @Override
    public Integrator<State<U>, T, T> integrator() {
        return (state, element, downstream) -> {
            U key = keyExtractor.apply(element);
            if (!state.hasValue || !Objects.equals(state.value, key)) {
                state.value = key;
                state.hasValue = true;
                return downstream.push(element);
            }
            return true;
        };
    }

    static class State<U> {
        U value;
        boolean hasValue;
    }
}
