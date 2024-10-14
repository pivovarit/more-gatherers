package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.distinctBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistinctByTest {

    @Test
    void shouldDistinctByEmptyStream() {
        assertThat(Stream.empty().gather(distinctBy(i -> i))).isEmpty();
    }

    @Test
    void shouldDistinctBy() {
        assertThat(Stream.of("a", "bb", "cc", "ddd")
          .gather(distinctBy(String::length)))
          .containsExactly("a", "bb", "ddd");
    }

    @Test
    void shouldRejectNullExtractor() {
        assertThatThrownBy(() -> distinctBy(null)).isInstanceOf(NullPointerException.class);
    }
}
