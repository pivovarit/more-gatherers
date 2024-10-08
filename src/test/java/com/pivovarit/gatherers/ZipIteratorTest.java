package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

class ZipIteratorTest {

    @Test
    void shouldZipEmpty() {
        assertThat(Stream.empty().gather(MoreGatherers.zip(Stream.of(1, 2, 3).iterator()))).isEmpty();
    }

    @Test
    void shouldZipWithEmpty() {
        assertThat(Stream.of(1, 2, 3).gather(MoreGatherers.zip(Stream.of().iterator()))).isEmpty();
    }

    @Test
    void shouldZip() {
        assertThat(Stream.of(1, 2, 3).gather(MoreGatherers.zip(Stream.of("a", "b", "c", "d").iterator())))
          .containsExactly(
            entry(1, "a"),
            entry(2, "b"),
            entry(3, "c")
          );
    }
}
