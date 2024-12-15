package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.filteringByIndex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ByIndexTest {

    @Test
    void shouldRejectNullPredicate() {
        assertThatThrownBy(() -> filteringByIndex(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldFilterByIndexEmptyStream() {
        assertThat(Stream.empty().gather(filteringByIndex((_, _) -> true))).isEmpty();
    }

    @Test
    void shouldFilterByIndex() {
        assertThat(Stream.of("a", "bb", "cc", "ddd")
          .gather(filteringByIndex((i, _) -> i % 2 == 0)))
          .containsExactly("a", "cc");
    }
}
