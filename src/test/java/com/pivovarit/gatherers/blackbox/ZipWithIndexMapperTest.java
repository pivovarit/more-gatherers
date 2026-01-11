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

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

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
