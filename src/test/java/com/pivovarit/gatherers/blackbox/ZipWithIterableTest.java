package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zipWithIterable;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipWithIterableTest {

    @Test
    void shouldZipEmpty() {
        assertThat(Stream.empty().gather(zipWithIterable(List.of(1, 2, 3)))).isEmpty();
    }

    @Test
    void shouldZipWithEmpty() {
        assertThat(Stream.of(1, 2, 3).gather(zipWithIterable(List.of()))).isEmpty();
    }

    @Test
    void shouldZip() {
        assertThat(Stream.of(1, 2, 3).gather(zipWithIterable(List.of("a", "b", "c", "d"))))
          .containsExactly(
            entry(1, "a"),
            entry(2, "b"),
            entry(3, "c")
          );
    }

    @Test
    void shouldZipWithShorter() {
        assertThat(Stream.of(1, 2, 3).gather(zipWithIterable(List.of("a", "b"))))
          .containsExactly(
            entry(1, "a"),
            entry(2, "b")
          );
    }

    @Test
    void shouldRejectNullCollection() {
        assertThatThrownBy(() -> zipWithIterable(null)).isInstanceOf(NullPointerException.class);
    }
}
