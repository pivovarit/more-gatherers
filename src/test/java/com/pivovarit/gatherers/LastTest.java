package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LastTest {

    @Test
    void shouldLastEmpty() {
        assertThat(Stream.of().gather(MoreGatherers.last(42))).isEmpty();
    }

    @Test
    void shouldTakeLastElement() {
        assertThat(Stream.of(1, 2, 3).gather(MoreGatherers.last(1))).containsExactly(3);
    }

    @Test
    void shouldTakeLastNElements() {
        assertThat(Stream.of(1, 2, 3).gather(MoreGatherers.last(2))).containsExactly(2, 3);
    }

    @Test
    void shouldTakeLastAllElements() {
        assertThat(Stream.of(1, 2, 3).gather(MoreGatherers.last(42))).containsExactly(1, 2, 3);
    }
}
