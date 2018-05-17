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

import com.codahale.fastuuid.UUIDGenerator;
import java.util.Random;
import org.junit.jupiter.api.Test;

class UUIDGeneratorTest {

  @Test
  void generatingUUIDs() {
    final UUIDGenerator generator = new UUIDGenerator(1, 2);
    assertThat(generator.generate().toString()).isEqualTo("da96ccf7-6644-4e2c-8217-4f943f318ee3");
    assertThat(generator.generate().toString()).isEqualTo("20a46683-c7e7-4d29-8733-6ee650f5ba2c");
    assertThat(generator.generate().version()).isEqualTo(4);
    assertThat(generator.generate().variant()).isEqualTo(2);
  }

  @Test
  void randomSeeding() {
    final Random random = new Random(100);
    final UUIDGenerator generator = new UUIDGenerator(random);
    assertThat(generator.generate().toString()).isEqualTo("91305a0b-1dd1-480c-8edc-e02c5ac6a728");
    assertThat(generator.generate().toString()).isEqualTo("ede04e9f-139d-43ce-bf17-fa760d1a0cfa");
  }
}
