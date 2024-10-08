package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zip;
import static org.assertj.core.api.Assertions.assertThat;

class ZipCollectionMapperTest {

    @Test
    void shouldZipEmpty() {
        assertThat(Stream.<Integer>empty().gather(zip(List.of(1, 2, 3), Integer::sum))).isEmpty();
    }

    @Test
    void shouldZipWithEmpty() {
        assertThat(Stream.of(1, 2, 3).gather(zip(List.of(), Integer::sum))).isEmpty();
    }

    @Test
    void shouldZip() {
        assertThat(Stream.of(1, 2, 3).gather(zip(List.of("a", "b", "c", "d"), (i, s) -> i + s)))
          .containsExactly("1a", "2b", "3c");
    }
}
