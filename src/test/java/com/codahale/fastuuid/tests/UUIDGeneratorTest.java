/*
 * Copyright © 2018 Coda Hale (coda.hale@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codahale.fastuuid.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codahale.fastuuid.UUIDGenerator;
import java.security.SecureRandom;
import org.junit.jupiter.api.Test;

class UUIDGeneratorTest {
  @Test
  void generating() {
    final SecureRandom random = mock(SecureRandom.class);
    when(random.nextLong()).thenReturn(0xb8d59fd5bc12dbb4L, 0x31e9f344b73ee369L,
        0xb8d59fd5bc12dbb4L, 0x31e9f344b73ee369L);

    final UUIDGenerator generator = new UUIDGenerator(random);
    assertThat(generator.generate().toString()).isEqualTo("f46add7b-083b-48a4-bdb0-9fa3c92f2bc2");
    assertThat(generator.generate().toString()).isEqualTo("07b145c8-a28b-4067-9891-9843ed61e7a2");
    assertThat(generator.generate().version()).isEqualTo(4);
    assertThat(generator.generate().variant()).isEqualTo(2);

    generator.reseed();
    assertThat(generator.generate().toString()).isEqualTo("f46add7b-083b-48a4-bdb0-9fa3c92f2bc2");
  }
}
