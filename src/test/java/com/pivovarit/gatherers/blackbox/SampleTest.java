package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.sample;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SampleTest {

    @Test
    void shouldRejectInvalidSampleSize() {
        assertThatThrownBy(() -> sample(0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("sample size can't be lower than 1");
    }

    @Test
    void shouldSampleEmpty() throws Exception {
        assertThat(Stream.empty().gather(sample(42))).isEmpty();
    }

    @Test
    void shouldSampleEvery() {
        assertThat(Stream.of(1,2,3).gather(sample(1))).containsExactly(1,2,3);
    }

    @Test
    void shouldSampleEveryOther() {
        assertThat(Stream.of(1,2,3).gather(sample(2))).containsExactly(1,3);
    }
}
