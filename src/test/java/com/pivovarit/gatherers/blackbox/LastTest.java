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

import static com.pivovarit.gatherers.MoreGatherers.last;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LastTest {

    @Test
    void shouldRejectInvalidSize() {
        assertThatThrownBy(() -> last(0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("number of elements can't be lower than one");
    }

    @Test
    void shouldLastEmpty() {
        assertThat(Stream.of().gather(last(42))).isEmpty();
    }

    @Test
    void shouldTakeLastElement() {
        assertThat(Stream.of(1, 2, 3).gather(last(1))).containsExactly(3);
    }

    @Test
    void shouldTakeLastNElements() {
        assertThat(Stream.of(1, 2, 3).gather(last(2))).containsExactly(2, 3);
    }

    @Test
    void shouldTakeLastAllElements() {
        assertThat(Stream.of(1, 2, 3).gather(last(42))).containsExactly(1, 2, 3);
    }
}
