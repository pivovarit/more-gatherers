package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.distinctBy;
import static org.assertj.core.api.Assertions.assertThat;

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
}
