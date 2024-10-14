package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.*;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipStreamTest {

    @Test
    void shouldZipEmpty() {
        assertThat(Stream.empty().gather(zip(Stream.of(1, 2, 3)))).isEmpty();
    }

    @Test
    void shouldZipWithEmpty() {
        assertThat(Stream.of(1, 2, 3).gather(zip(Stream.of()))).isEmpty();
    }

    @Test
    void shouldZip() {
        assertThat(Stream.of(1, 2, 3).gather(zip(Stream.of("a", "b", "c", "d"))))
          .containsExactly(
            entry(1, "a"),
            entry(2, "b"),
            entry(3, "c")
          );
    }

    @Test
    void shouldRejectNullStream() {
        assertThatThrownBy(() -> zip((Iterator<Object>) null)).isInstanceOf(NullPointerException.class);
    }
}
