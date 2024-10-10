package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.distinctUntilChanged;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistinctUntilChangedMapperTest {

    @Test
    void shouldDistinctUntilChangedEmptyStream() {
        assertThat(Stream.empty().gather(distinctUntilChanged(i -> i))).isEmpty();
    }

    @Test
    void shouldDistinctUntilChangedMapper() {
        assertThat(Stream.of("a", "b", "bb", "cc", "ddd", "eee", "ffff", "ggggg", "hhhhh", "iiiii", "j", "k")
          .gather(distinctUntilChanged(s -> s.length())))
          .containsExactly("a", "bb", "ddd", "ffff", "ggggg", "j");
    }

    @Test
    void shouldRejectNullMapper() {
        assertThatThrownBy(() -> distinctUntilChanged(null)).isInstanceOf(NullPointerException.class);
    }
}
