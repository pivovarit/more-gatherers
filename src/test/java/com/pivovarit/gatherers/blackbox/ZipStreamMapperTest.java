package com.pivovarit.gatherers.blackbox;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipStreamMapperTest {

    @Test
    void shouldZipEmpty() {
        assertThat(Stream.<Integer>empty().gather(zip(Stream.of(1, 2, 3), Integer::sum))).isEmpty();
    }

    @Test
    void shouldZipWithEmpty() {
        assertThat(Stream.of(1, 2, 3).gather(zip(Stream.of(), Integer::sum))).isEmpty();
    }

    @Test
    void shouldZip() {
        assertThat(Stream.of(1, 2, 3).gather(zip(Stream.of("a", "b", "c", "d"), (i, s) -> i + s)))
          .containsExactly("1a", "2b", "3c");
    }

    @Test
    void shouldRejectNullStream() {
        assertThatThrownBy(() -> zip((Stream<Object>) null, (i, _) -> i)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullMapper() {
        assertThatThrownBy(() -> zip(List.of(1).stream(), null)).isInstanceOf(NullPointerException.class);
    }
}
