package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zipWithIterable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipWithIterableMapperTest {

    @Test
    void shouldZipEmpty() {
        assertThat(Stream.<Integer>empty().gather(zipWithIterable(List.of(1, 2, 3), Integer::sum))).isEmpty();
    }

    @Test
    void shouldZipWithEmpty() {
        assertThat(Stream.of(1, 2, 3).gather(zipWithIterable(List.of(), Integer::sum))).isEmpty();
    }

    @Test
    void shouldZip() {
        assertThat(Stream.of(1, 2, 3).gather(zipWithIterable(List.of("a", "b", "c", "d"), (i, s) -> i + s)))
          .containsExactly("1a", "2b", "3c");
    }

    @Test
    void shouldRejectNullCollection() {
        assertThatThrownBy(() -> zipWithIterable(null, (i, _) -> i)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullMapper() {
        assertThatThrownBy(() -> zipWithIterable(List.of(1), null)).isInstanceOf(NullPointerException.class);
    }
}
