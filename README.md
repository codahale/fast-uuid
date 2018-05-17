# fast-uuid

[![Build Status](https://secure.travis-ci.org/codahale/fast-uuid.svg)](http://travis-ci.org/codahale/fast-uuid)

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

## Fascinating

What can I say. I got bored.

## License

Copyright © 2018 Coda Hale

Distributed under the Apache License 2.0.
