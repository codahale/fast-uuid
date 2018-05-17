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
package com.codahale.fastuuid;

import java.util.Random;
import java.util.UUID;
import javax.annotation.CheckReturnValue;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * A class which generates {@link UUID} instances using SipHash-2-4 in a fast-key-erasure CSPRNG.
 */
@NotThreadSafe
public class UUIDGenerator {
  // four bytes selected for their relatively high Hamming distances
  private static final byte A = 0x06;
  private static final byte B = 0x7F;
  private static final byte C = (byte) 0xB8;
  private static final byte D = (byte) 0xC1;

  // SipHash state
  private long v0, v1, v2, v3;

  /**
   * Creates a new {@link UUIDGenerator} seeded from the given PRNG.
   *
   * @param random a PRNG to use for a seed
   */
  public UUIDGenerator(Random random) {
    this(random.nextLong(), random.nextLong());
  }

  /**
   * Creates a new {@link UUIDGenerator} with the given seed values.
   *
   * @param k0 the first seed value
   * @param k1 the second seed value
   */
  public UUIDGenerator(long k0, long k1) {
    reset(k0, k1);
  }

  private void reset(long k0, long k1) {
    // SipHash magic constants
    this.v0 = k0 ^ 0x736F6D6570736575L;
    this.v1 = k1 ^ 0x646F72616E646F6DL;
    this.v2 = k0 ^ 0x6C7967656E657261L;
    this.v3 = k1 ^ 0x7465646279746573L;
  }

  /**
   * Generates a random {@link UUID}.
   *
   * @return a random {@link UUID}
   */
  @CheckReturnValue
  public UUID generate() {
    final long k0 = sipHash24(v0, v1, v2, v3, A);
    final long k1 = sipHash24(v0, v1, v2, v3, B);
    final long msb = (sipHash24(v0, v1, v2, v3, C) & ~0xF000L) | 0x4000L;
    final long lsb = ((sipHash24(v0, v1, v2, v3, D) << 2) >>> 2) | 0x8000000000000000L;
    reset(k0, k1);
    return new UUID(msb, lsb);
  }

  // a very slimmed-down version of SipHash-2-4 which operates on a single byte
  @SuppressWarnings("Duplicates")
  private static long sipHash24(long v0, long v1, long v2, long v3, byte data) {
    final long m = data | 0x100000000000000L; // simplify the masking

    v3 ^= m;
    for (int i = 0; i < 2; i++) { // put the 2 in SipHash-2-4
      v0 += v1;
      v2 += v3;
      v1 = Long.rotateLeft(v1, 13);
      v3 = Long.rotateLeft(v3, 16);

      v1 ^= v0;
      v3 ^= v2;
      v0 = Long.rotateLeft(v0, 32);

      v2 += v1;
      v0 += v3;
      v1 = Long.rotateLeft(v1, 17);
      v3 = Long.rotateLeft(v3, 21);

      v1 ^= v2;
      v3 ^= v0;
      v2 = Long.rotateLeft(v2, 32);
    }
    v0 ^= m;

    v2 ^= 0xFF;
    for (int i = 0; i < 4; i++) { // put the 4 in SipHash-2-4
      v0 += v1;
      v2 += v3;
      v1 = Long.rotateLeft(v1, 13);
      v3 = Long.rotateLeft(v3, 16);

      v1 ^= v0;
      v3 ^= v2;
      v0 = Long.rotateLeft(v0, 32);

      v2 += v1;
      v0 += v3;
      v1 = Long.rotateLeft(v1, 17);
      v3 = Long.rotateLeft(v3, 21);

      v1 ^= v2;
      v3 ^= v0;
      v2 = Long.rotateLeft(v2, 32);
    }
    return v0 ^ v1 ^ v2 ^ v3;
  }
}
