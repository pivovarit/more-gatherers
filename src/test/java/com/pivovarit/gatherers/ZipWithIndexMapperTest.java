package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zipWithIndex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipWithIndexMapperTest {

    @Test
    void shouldRejectNullMapper() {
        assertThatThrownBy(() -> zipWithIndex(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldZipEmptyStream() {
        assertThat(Stream.empty().gather(zipWithIndex((idx, i) -> "" + idx + i))).isEmpty();
    }

    @Test
    void shouldZipWithIndex() {
        assertThat(Stream.of("a", "b", "c")
          .gather(zipWithIndex((idx, i) -> idx + i)))
          .containsExactly("0a", "1b", "2c");
    }
}
