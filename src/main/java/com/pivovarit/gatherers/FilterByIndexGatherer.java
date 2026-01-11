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
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record FilterByIndexGatherer<T>(BiPredicate<Long, ? super T> predicate) implements Gatherer<T, AtomicLong, T> {

    FilterByIndexGatherer {
        Objects.requireNonNull(predicate);
    }

    @Override
    public Supplier<AtomicLong> initializer() {
        return AtomicLong::new;
    }

    @Override
    public Integrator<AtomicLong, T, T> integrator() {
        return Integrator.ofGreedy((seq, t, downstream) -> predicate.test(seq.getAndIncrement(), t) && downstream.push(t));
    }
}
