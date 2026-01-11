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

import static com.pivovarit.gatherers.MoreGatherers.distinctByKeepLast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistinctByKeepLastTest {

    @Test
    void shouldDistinctByEmptyStream() {
        assertThat(Stream.empty().gather(distinctByKeepLast(i -> i))).isEmpty();
    }

    @Test
    void shouldDistinctBy() {
        assertThat(Stream.of("a", "bb", "cc", "ddd")
          .gather(distinctByKeepLast(String::length)))
          .containsExactly("a", "cc", "ddd");
    }

    @Test
    void shouldDistinctByAndKeepLastOrder() {
        assertThat(Stream.of("a", "bb", "ddd", "cc")
          .gather(distinctByKeepLast(String::length)))
          .containsExactly("a", "ddd", "cc");
    }

    @Test
    void shouldRejectNullExtractor() {
        assertThatThrownBy(() -> distinctByKeepLast(null)).isInstanceOf(NullPointerException.class);
    }
}
