/*
 * Copyright 2014-2026 Grzegorz Piwowarek, https://4comprehension.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pivovarit.gatherers.blackbox;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

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
    void shouldZipWithShorter() {
        assertThat(Stream.of(1, 2, 3).gather(zip(Stream.of("a", "b"), (i, s) -> i + s)))
          .containsExactly("1a", "2b");
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
