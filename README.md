# more-gatherers

Missing Stream API functionality you always longed for - provided via `Gatherers`

[![ci](https://github.com/pivovarit/more-gatherers/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/pivovarit/more-gatherers/actions/workflows/ci.yml)
[![pitest](https://github.com/pivovarit/more-gatherers/actions/workflows/pitest.yml/badge.svg?branch=main)](https://pivovarit.github.io/more-gatherers)
[![Maven Central Version](https://img.shields.io/maven-central/v/com.pivovarit/more-gatherers)](https://central.sonatype.com/artifact/com.pivovarit/more-gatherers/versions)
[![libs.tech recommends](https://libs.tech/project/869106466/badge.svg)](https://libs.tech/project/869106466/more-gatherers)

[![Stargazers over time](https://starchart.cc/pivovarit/more-gatherers.svg?variant=adaptive)](https://starchart.cc/pivovarit/more-gatherers)

### Overview

Java's Stream API is a powerful tool for processing collections of data. However, it lacks some functionality that could make it even more powerful. This library aims to fill that gap by providing a set of `Gatherers` that can be used to collect data from a stream more flexibly.

Whenever possible, the library follows Project Reactor's naming conventions.

Provided `Gatherers`:
- `MoreGatherers.last()`
  - takes the last element from the stream
- `MoreGatherers.last(int)`
  - takes last `n` elements from the stream
- `MoreGatherers.sampling(int)`
  - takes every `n`-th element from the stream
- `MoreGatherers.zip(Iterator<T2>)`
  - zips `Stream` elements with elements from the provided `Iterator`
- `MoreGatherers.zip(Iterator<T2>, BiFunction<T1,T2>)`
  - zips `Stream` elements with elements from the provided `Iterator` using a custom zipper function
- `MoreGatherers.zip(Stream<T2>)`
  - zips `Stream` elements with elements from the provided `Stream`
- `MoreGatherers.zip(Stream<T2>, BiFunction<T1,T2>)`
  - zips `Stream` elements with elements from the provided `Stream` using a custom zipper function
- `MoreGatherers.zipWithIterable(Iterable<T2>)`
  - zips `Stream` elements with elements from the provided `Iterable`
- `MoreGatherers.zipWithIterable(Iterable<T2>, BiFunction<T1,T2>)`
  - zips elements with elements from the provided `Iterable` using a custom zipper function
- `MoreGatherers.zipWithIndex()`
  - zips `Stream` elements with their index
- `MoreGatherers.zipWithIndex(BiFunction<Long,T>)`
  - zips `Stream` elements with their index using a custom zipper function
- `MoreGatherers.distinctBy(Function<T, R>)`
  - takes distinct elements based on a key extractor function
- `MoreGatherers.distinctByKeepLast(Function<T, R>)`
  - takes distinct elements based on a key extractor function, keeping the last occurrence
- `MoreGatherers.distinctUntilChanged()`
  - takes elements until a change is detected
- `MoreGatherers.distinctUntilChanged(Function<T, R>)`
  - takes elements until a change is detected based on a key extractor function
- `MoreGatherers.windowSliding(int, int)`
  - creates a sliding window of a fixed size with a fixed step, extends `Gatherers.windowSliding(int)` by adding a step parameter
- `MoreGatherers.filteringByIndex(BiPredicate<Long, T>)`
  - filters elements based on their index and value
- `MoreGatherers.groupingBy(Function<T, K>, Collector<T, ?, R>)`
  - groups elements by a key extractor function and applies a custom collector

### Philosophy

The primary goal of this library is to complement the existing Stream API by providing functionality that's currently missing without duplicating features already available. While it is technically possible to create numerous custom Gatherers, this library focuses on offering only those that cannot be easily achieved using standard Stream API operations.

The library is designed to be as lightweight as possible, with no external dependencies. It's implemented using core Java libraries and follows the same conventions as the standard Stream API, drawing inspiration from Project Reactor's method names.
