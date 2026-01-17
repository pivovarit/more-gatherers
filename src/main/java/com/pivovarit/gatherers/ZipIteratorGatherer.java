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

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record ZipIteratorGatherer<T1, T2, R>(Iterator<T2> iterator, BiFunction<? super T1, ? super T2, ? extends R> mapper)
  implements Gatherer<T1, Iterator<T2>, R> {

    ZipIteratorGatherer {
        Objects.requireNonNull(mapper, "mapper can't be null");
        Objects.requireNonNull(iterator, "iterator can't be null");
    }

    @Override
    public Supplier<Iterator<T2>> initializer() {
        return () -> iterator;
    }

    @Override
    public Integrator<Iterator<T2>, T1, R> integrator() {
        return (state, element, downstream) -> state.hasNext()
          ? downstream.push(mapper.apply(element, state.next()))
          : false;
    }
}
