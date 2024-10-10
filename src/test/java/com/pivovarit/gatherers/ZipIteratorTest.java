package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zip;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipIteratorTest {

    @Test
    void shouldZipEmpty() {
        assertThat(Stream.empty().gather(zip(Stream.of(1, 2, 3).iterator()))).isEmpty();
    }

    @Test
    void shouldZipWithEmpty() {
        assertThat(Stream.of(1, 2, 3).gather(zip(Stream.of().iterator()))).isEmpty();
    }

    @Test
    void shouldZip() {
        assertThat(Stream.of(1, 2, 3).gather(zip(Stream.of("a", "b", "c", "d").iterator())))
          .containsExactly(
            entry(1, "a"),
            entry(2, "b"),
            entry(3, "c")
          );
    }

    @Test
    void shouldRejectNullIterator() {
        assertThatThrownBy(() -> zip((Iterator<Object>) null)).isInstanceOf(NullPointerException.class);
    }
}
