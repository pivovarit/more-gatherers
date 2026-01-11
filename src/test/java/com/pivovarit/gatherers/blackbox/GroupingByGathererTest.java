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

import com.pivovarit.gatherers.MoreGatherers;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupingByGathererTest {

    @Test
    void shouldRejectNullClassifier() {
        assertThatThrownBy(() -> MoreGatherers.groupingBy(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("classifier");
    }

    @Test
    void shouldRejectNullCollector() {
        assertThatThrownBy(() -> MoreGatherers.groupingBy(i -> i, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("collector");
    }

    @Test
    void shouldGroupEmpty() {
        assertThat(List.<String>of().stream().gather(MoreGatherers.groupingBy(i -> i))).isEmpty();
    }

    @Test
    void shouldGroupEmptyWithCustomCollector() {
        assertThat(List.<String>of().stream().gather(MoreGatherers.groupingBy(i -> i, Collectors.toSet()))).isEmpty();
    }

    @Test
    void shouldGroupToList() {
        List<Map.Entry<Integer, List<String>>> results = Stream.of("a", "bb", "cc", "ddd", "ee", "fff")
          .gather(MoreGatherers.groupingBy(String::length))
          .toList();

        assertThat(results)
          .hasSize(3)
          .containsExactlyInAnyOrder(
            Map.entry(1, List.of("a")),
            Map.entry(2, List.of("bb", "cc", "ee")),
            Map.entry(3, List.of("ddd", "fff"))
          );
    }

    @Test
    void shouldGroupUsingCustomCollector() {
        List<Map.Entry<Integer, Set<String>>> results = Stream.of("a", "bb", "cc", "ddd", "ee", "fff")
          .gather(MoreGatherers.groupingBy(String::length, Collectors.toSet()))
          .toList();

        assertThat(results)
          .hasSize(3)
          .containsExactlyInAnyOrder(
            Map.entry(1, Set.of("a")),
            Map.entry(2, Set.of("bb", "cc", "ee")),
            Map.entry(3, Set.of("ddd", "fff"))
          );
    }
}
