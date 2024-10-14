package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.distinctUntilChanged;
import static org.assertj.core.api.Assertions.assertThat;

class DistinctUntilChangedTest {

    @Test
    void shouldDistinctUntilChangedEmptyStream() {
        assertThat(Stream.empty().gather(distinctUntilChanged())).isEmpty();
    }

    @Test
    void shouldDistinctUntilChanged() {
        assertThat(Stream.of(1, 1, 2, 2, 3, 3, 4, 5, 5, 5, 1, 1)
          .gather(distinctUntilChanged()))
          .containsExactly(1, 2, 3, 4, 5, 1);
    }
}
