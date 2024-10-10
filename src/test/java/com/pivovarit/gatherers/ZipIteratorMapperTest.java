package com.pivovarit.gatherers;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static com.pivovarit.gatherers.MoreGatherers.zip;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipIteratorMapperTest {

    @Test
    void shouldZipEmpty() {
        assertThat(Stream.<Integer>empty().gather(zip(List.of(1, 2, 3).iterator(), Integer::sum))).isEmpty();
    }

    @Test
    void shouldZipWithEmpty() {
        assertThat(Stream.of(1, 2, 3).gather(zip(List.<Integer>of().iterator(), Integer::sum))).isEmpty();
    }

    @Test
    void shouldZip() {
        assertThat(Stream.of(1, 2, 3).gather(zip(List.of("a", "b", "c", "d").iterator(), (i, s) -> i + s)))
          .containsExactly("1a", "2b", "3c");
    }

    @Test
    void shouldRejectNullIterator() {
        assertThatThrownBy(() -> zip((Iterator<Object>) null, (i, _) -> i)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullMapper() {
        assertThatThrownBy(() -> zip(List.of(1).iterator(), null)).isInstanceOf(NullPointerException.class);
    }
}
