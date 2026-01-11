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

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record ZipWithIndexGatherer<T>() implements Gatherer<T, AtomicLong, Map.Entry<T, Long>> {

    @Override
    public Supplier<AtomicLong> initializer() {
        return AtomicLong::new;
    }

    @Override
    public Integrator<AtomicLong, T, Map.Entry<T, Long>> integrator() {
        return Integrator.ofGreedy((state, element, downstream) -> downstream.push(Map.entry(element, state.getAndIncrement())));
    }
}
