package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zipWithIndex;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

class ZipWithIndexTest {

    @Test
    void shouldZipEmptyStream() {
        assertThat(Stream.empty().gather(zipWithIndex())).isEmpty();
    }

    @Test
    void shouldZipWithIndex() {
        assertThat(Stream.of("a", "b", "c")
          .gather(zipWithIndex()))
          .containsExactly(
            entry("a", 0L),
            entry("b", 1L),
            entry("c", 2L)
          );
    }
}
