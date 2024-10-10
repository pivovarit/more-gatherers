package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.last;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LastTest {

    @Test
    void shouldRejectInvalidSize() {
        assertThatThrownBy(() -> last(0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("number of elements can't be lower than one");
    }

    @Test
    void shouldLastEmpty() {
        assertThat(Stream.of().gather(last(42))).isEmpty();
    }

    @Test
    void shouldTakeLastElement() {
        assertThat(Stream.of(1, 2, 3).gather(last(1))).containsExactly(3);
    }

    @Test
    void shouldTakeLastNElements() {
        assertThat(Stream.of(1, 2, 3).gather(last(2))).containsExactly(2, 3);
    }

    @Test
    void shouldTakeLastAllElements() {
        assertThat(Stream.of(1, 2, 3).gather(last(42))).containsExactly(1, 2, 3);
    }
}
