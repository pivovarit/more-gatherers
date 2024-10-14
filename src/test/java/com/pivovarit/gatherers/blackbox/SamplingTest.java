package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.sampling;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SamplingTest {

    @Test
    void shouldRejectInvalidSampleSize() {
        assertThatThrownBy(() -> sampling(0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("sampling frequency can't be lower than 1");
    }

    @Test
    void shouldSampleEmpty() throws Exception {
        assertThat(Stream.empty().gather(sampling(42))).isEmpty();
    }

    @Test
    void shouldSampleEvery() {
        assertThat(Stream.of(1,2,3).gather(sampling(1))).containsExactly(1,2,3);
    }

    @Test
    void shouldSampleEveryOther() {
        assertThat(Stream.of(1,2,3).gather(sampling(2))).containsExactly(1,3);
    }
}
