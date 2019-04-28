# fast-uuid

[![CircleCI](https://circleci.com/gh/codahale/fast-uuid.svg?style=svg)](https://circleci.com/gh/codahale/fast-uuid)

**N.B.: This is a cool design, but is only an order of magnitude faster than the Java 12 standard library. Just use that instead.**

I dunno, man. It generates v4 UUIDs really quickly.

## How quickly

```
Benchmark          Mode  Cnt     Score    Error  Units
Benchmarks.fast    avgt   25    48.554 ±  0.568  ns/op
Benchmarks.stdLib  avgt   25  1440.938 ± 30.051  ns/op
```

## Ok but how tho

Instead of `SecureRandom`, `UUIDGenerator` uses
[SipHash-2-4](https://131002.net/siphash/siphash.pdf) in a
[fast-key-erasure](https://blog.cr.yp.to/20170723-random.html) CSPRNG. 

For each UUID, it uses SipHash-2-4 to hash four single-byte values selected for their high Hamming
distances from each other. The first two results are used to re-key the hash; the second two are
used to produce the UUID.

For UUIDs, a few key properties are desirable:

1. They should be uniformly distributed to reduce the probability of collisions.
2. Anyone who collects a set of UUIDs should be unable to determine which UUIDs were generated and
   which UUIDs will be generated.
3. Anyone who gets privileged access to the computer generating UUIDs should be unable to determine
   which UUIDs were previously generated.

This library attempts to provide those properties.

**N.B.: I'm not a cryptographer, so no cryptographer evaluated this design. Do not use this without
hiring a cryptographer to evaluate it.**

For the first property, SipHash is a cryptographically strong PRF, which should make it
indistinguishable from a uniform random function. The UUIDs generated from its output, therefore,
should also be uniformly distributed.

For the second property, consider the information an attacker might collect: `{h(k, C), h(k, D)}`.
In order to calculate future values, an attacker would need to learn information about `k`. SipHash
is a strong MAC, however, which means key recovery attacks should be as difficult as brute force.
Without the ability to recover `k`, the attacker would find it doubly difficult to calculate past
values (e.g. `h(k, C)` given `h(h(k, A) . h(k, B), C)`).

For the third property, consider the information an attacker might collect: `h(h(k, A) . h(k, B),
A)`. Again, because SipHash is a strong MAC, an attacker should be unable to recover information
about `k` and therefore unable to calculate past values (e.g. `h(k, C)`). Of course, they *will* be
able to calculate future values, so don't let attackers look at your memory.

## Fascinating

What can I say. I got bored.

## License

Copyright © 2018 Coda Hale

Distributed under the Apache License 2.0.
