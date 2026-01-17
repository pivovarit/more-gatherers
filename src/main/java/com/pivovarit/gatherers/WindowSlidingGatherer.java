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

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record WindowSlidingGatherer<T>(int windowSize, int step)
  implements Gatherer<T, WindowSlidingGatherer<T>.SlidingWindow, List<T>> {
    WindowSlidingGatherer {
        if (windowSize < 1) {
            throw new IllegalArgumentException("'windowSize' must be greater than zero");
        }

        if (step < 0) {
            throw new IllegalArgumentException("'step' must be greater than or equal to zero");
        }

        if (step > windowSize) {
            throw new IllegalArgumentException("'step' must be less than or equal to 'windowSize'");
        }
    }

    @Override
    public Supplier<WindowSlidingGatherer<T>.SlidingWindow> initializer() {
        return WindowSlidingGatherer.SlidingWindow::new;
    }

    @Override
    public Integrator<WindowSlidingGatherer<T>.SlidingWindow, T, List<T>> integrator() {
        return Integrator.ofGreedy((state, e, downstream) -> state.integrate(e, downstream));
    }

    @Override
    public BiConsumer<WindowSlidingGatherer<T>.SlidingWindow, Downstream<? super List<T>>> finisher() {
        return SlidingWindow::finish;
    }

    class SlidingWindow {
        private Object[] window = new Object[windowSize];
        private int at = 0;
        private boolean emitted = false;

        boolean integrate(T element, Downstream<? super List<T>> downstream) {
            window[at++] = element;
            emitted = false;
            if (at < windowSize) {
                return true;
            } else {
                final var oldWindow = window;
                final var newWindow = new Object[windowSize];
                System.arraycopy(oldWindow, step, newWindow, 0, windowSize - step);
                window = newWindow;
                at -= step;
                emitted = true;
                return downstream.push((List<T>) Arrays.asList(oldWindow));
            }
        }

        void finish(Downstream<? super List<T>> downstream) {
            if (!emitted && at > 0 && !downstream.isRejecting()) {
                var lastWindow = new Object[at];
                System.arraycopy(window, 0, lastWindow, 0, at);
                at = 0;
                emitted = true;
                downstream.push((List<T>) Arrays.asList(lastWindow));
            }
        }
    }
}
