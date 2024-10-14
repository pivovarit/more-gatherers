package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.distinctByKeepLast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistinctByKeepLastTest {

    @Test
    void shouldDistinctByEmptyStream() {
        assertThat(Stream.empty().gather(distinctByKeepLast(i -> i))).isEmpty();
    }

    @Test
    void shouldDistinctBy() {
        assertThat(Stream.of("a", "bb", "cc", "ddd")
          .gather(distinctByKeepLast(String::length)))
          .containsExactly("a", "cc", "ddd");
    }

    @Test
    void shouldRejectNullExtractor() {
        assertThatThrownBy(() -> distinctByKeepLast(null)).isInstanceOf(NullPointerException.class);
    }
}
