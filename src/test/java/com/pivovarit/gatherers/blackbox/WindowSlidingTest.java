package com.pivovarit.gatherers.blackbox;

import com.pivovarit.gatherers.MoreGatherers;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WindowSlidingTest {

    @Test
    void shouldRejectInvalidWindowSize() {
        assertThatThrownBy(() -> MoreGatherers.windowSliding(0, 1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("'windowSize' must be greater than zero");
    }

    @Test
    void shouldRejectInvalidStep() {
        assertThatThrownBy(() -> MoreGatherers.windowSliding(1, -1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("'step' must be greater than or equal to zero");
    }

    @Test
    void shouldRejectInvalidWindowSizeAndStep() {
        assertThatThrownBy(() -> MoreGatherers.windowSliding(3, 4))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("'step' must be less than or equal to 'windowSize'");
    }

    @Test
    void shouldWindowSlidingEmpty() {
        assertThat(Stream.empty().gather(MoreGatherers.windowSliding(2, 1))).isEmpty();
    }

    @Test
    void shouldWindowSlidingWithWindowSizeGreaterThanStreamSize() {
        assertThat(Stream.of(1, 2, 3).gather(MoreGatherers.windowSliding(4, 1)))
          .containsExactly(of(1, 2, 3));
    }

    @Test
    void shouldWindowSlidingWithStep1() {
        assertThat(Stream.of(1, 2, 3, 4, 5).gather(MoreGatherers.windowSliding(2, 1)))
          .containsExactly(of(1, 2), of(2, 3), of(3, 4), of(4, 5));
    }

    @Test
    void shouldWindowSlidingWithStep2() {
        assertThat(Stream.of(1, 2, 3, 4, 5).gather(MoreGatherers.windowSliding(2, 2)))
          .containsExactly(of(1, 2), of(3, 4), of(5));
    }
}
