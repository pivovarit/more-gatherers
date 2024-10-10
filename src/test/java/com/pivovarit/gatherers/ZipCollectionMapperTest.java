package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void shouldRejectNullCollection() {
        assertThatThrownBy(() -> zip((Collection<Object>) null, (i, _) -> i)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullMapper() {
        assertThatThrownBy(() -> zip(List.of(1), null)).isInstanceOf(NullPointerException.class);
    }
}
