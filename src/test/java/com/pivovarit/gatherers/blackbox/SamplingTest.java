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

import static com.pivovarit.gatherers.MoreGatherers.sampling;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SamplingTest {

    @Test
    void shouldRejectInvalidSampleSize() {
        assertThatThrownBy(() -> sampling(0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("sampling frequency can't be lower than 1");
    }

    @Test
    void shouldSampleEmpty() throws Exception {
        assertThat(Stream.empty().gather(sampling(42))).isEmpty();
    }

    @Test
    void shouldSampleEvery() {
        assertThat(Stream.of(1,2,3).gather(sampling(1))).containsExactly(1,2,3);
    }

    @Test
    void shouldSampleEveryOther() {
        assertThat(Stream.of(1,2,3).gather(sampling(2))).containsExactly(1,3);
    }
}
