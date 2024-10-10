package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zip;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipCollectionTest {

    @Test
    void shouldZipEmpty() {
        assertThat(Stream.empty().gather(zip(List.of(1, 2, 3)))).isEmpty();
    }

    @Test
    void shouldZipWithEmpty() {
        assertThat(Stream.of(1, 2, 3).gather(zip(List.of()))).isEmpty();
    }

    @Test
    void shouldZip() {
        assertThat(Stream.of(1, 2, 3).gather(zip(List.of("a", "b", "c", "d"))))
          .containsExactly(
            entry(1, "a"),
            entry(2, "b"),
            entry(3, "c")
          );
    }

    @Test
    void shouldRejectNullCollection() {
        assertThatThrownBy(() -> zip((Collection<Object>) null)).isInstanceOf(NullPointerException.class);
    }
}
