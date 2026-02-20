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
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WindowSlidingTest {

    @Test
    void shouldRejectInvalidWindowSize() {
        assertThatThrownBy(() -> MoreGatherers.windowSliding(0, 1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("'windowSize' must be greater than zero");
    }

    @Test
    void shouldRejectZeroStep() {
        assertThatThrownBy(() -> MoreGatherers.windowSliding(3, 0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("'step' must be greater than zero");
    }

    @Test
    void shouldRejectNegativeStep() {
        assertThatThrownBy(() -> MoreGatherers.windowSliding(1, -1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("'step' must be greater than zero");
    }

    @Test
    void shouldRejectInvalidWindowSizeAndStep() {
        assertThatThrownBy(() -> MoreGatherers.windowSliding(3, 4))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("'step' must be less than or equal to 'windowSize'");
    }

    @Test
    void shouldWindowSlidingEmpty() {
        assertThat(Stream.empty().gather(MoreGatherers.windowSliding(2, 1))).isEmpty();
    }

    @Test
    void shouldWindowSlidingWithWindowSizeGreaterThanStreamSize() {
        assertThat(Stream.of(1, 2, 3).gather(MoreGatherers.windowSliding(4, 1)))
          .containsExactly(of(1, 2, 3));
    }

    @Test
    void shouldWindowSlidingWithStep1() {
        assertThat(Stream.of(1, 2, 3, 4, 5).gather(MoreGatherers.windowSliding(2, 1)))
          .containsExactly(of(1, 2), of(2, 3), of(3, 4), of(4, 5));
    }

    @Test
    void shouldWindowSlidingWithStep2() {
        assertThat(Stream.of(1, 2, 3, 4, 5).gather(MoreGatherers.windowSliding(2, 2)))
          .containsExactly(of(1, 2), of(3, 4), of(5));
    }
}
