package com.pivovarit.gatherers;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

record WindowSlidingGatherer<T>(int windowSize, int step)
  implements Gatherer<T, WindowSlidingGatherer.SlidingWindow, List<T>> {
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
    public Supplier<WindowSlidingGatherer.SlidingWindow> initializer() {
        return WindowSlidingGatherer.SlidingWindow::new;
    }

    @Override
    public Integrator<WindowSlidingGatherer.SlidingWindow, T, List<T>> integrator() {
        return Integrator.ofGreedy((state, e, downstream) -> state.integrate(e, downstream));
    }

    @Override
    public BiConsumer<WindowSlidingGatherer.SlidingWindow, Downstream<? super List<T>>> finisher() {
        return SlidingWindow::finish;
    }

    class SlidingWindow {
        Object[] window = new Object[windowSize];
        int at = 0;
        boolean firstWindow = true;

        boolean integrate(T element, Downstream<? super List<T>> downstream) {
            window[at++] = element;
            if (at < windowSize) {
                return true;
            } else {
                final var oldWindow = window;
                final var newWindow = new Object[windowSize];
                System.arraycopy(oldWindow, step, newWindow, 0, windowSize - step);
                window = newWindow;
                at -= step;
                firstWindow = false;
                return downstream.push((List<T>) Arrays.asList(oldWindow));
            }
        }

        void finish(Downstream<? super List<T>> downstream) {
            if (firstWindow && at > 0 && !downstream.isRejecting()) {
                var lastWindow = new Object[at];
                System.arraycopy(window, 0, lastWindow, 0, at);
                window = null;
                at = 0;
                downstream.push((List<T>) Arrays.asList(lastWindow));
            }
        }
    }
}
