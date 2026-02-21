# more-gatherers

Missing Stream API functionality you always longed for - provided via `Gatherers`

[![ci](https://github.com/pivovarit/more-gatherers/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/pivovarit/more-gatherers/actions/workflows/ci.yml)
[![pitest](https://github.com/pivovarit/more-gatherers/actions/workflows/pitest.yml/badge.svg?branch=main)](https://pivovarit.github.io/more-gatherers/pitest/)
[![Maven Central Version](https://img.shields.io/maven-central/v/com.pivovarit/more-gatherers)](https://central.sonatype.com/artifact/com.pivovarit/more-gatherers/versions)
[![libs.tech recommends](https://libs.tech/project/869106466/badge.svg)](https://libs.tech/project/869106466/more-gatherers)

[![Stargazers over time](https://starchart.cc/pivovarit/more-gatherers.svg?variant=adaptive)](https://starchart.cc/pivovarit/more-gatherers)

## Requirements

Java 24+

## Getting Started

### Maven

```xml
<dependency>
    <groupId>com.pivovarit</groupId>
    <artifactId>more-gatherers</artifactId>
    <version>0.2.1</version>
</dependency>
```

### Gradle

```kotlin
implementation("com.pivovarit:more-gatherers:0.2.1")
```

All examples below assume a static import:

```java
import static com.pivovarit.gatherers.MoreGatherers.*;
```

## Overview

Java's Stream API is a powerful tool for processing collections of data. However, it lacks some functionality that could make it even more powerful. This library fills that gap by providing a set of `Gatherers` that extend the Stream API.

Whenever possible, the library follows Project Reactor's naming conventions.

## Philosophy

The primary goal of this library is to complement the existing Stream API by providing functionality that's currently missing without duplicating features already available. While it is technically possible to create numerous custom Gatherers, this library focuses on offering only those that cannot be easily achieved using standard Stream API operations.

The library is designed to be as lightweight as possible, with no external dependencies. It's implemented using core Java libraries and follows the same conventions as the standard Stream API.

## Gatherers

### `last()`

Takes the last element from the stream.

```java
Stream.of(1, 2, 3)
  .gather(last())
  .toList(); // [3]
```

### `last(int n)`

Takes the last `n` elements from the stream.

```java
Stream.of(1, 2, 3, 4, 5)
  .gather(last(3))
  .toList(); // [3, 4, 5]
```

### `sampling(int n)`

Takes every `n`-th element from the stream.

```java
Stream.of(1, 2, 3, 4, 5, 6)
  .gather(sampling(2))
  .toList(); // [1, 3, 5]
```

### `distinctBy(Function<T, R>)`

Takes distinct elements based on a key extractor, keeping the **first** occurrence of each key.

```java
Stream.of("a", "bb", "cc", "ddd")
  .gather(distinctBy(String::length))
  .toList(); // ["a", "bb", "ddd"]
```

### `distinctByKeepLast(Function<T, R>)`

Takes distinct elements based on a key extractor, keeping the **last** occurrence of each key.

```java
Stream.of("a", "bb", "cc", "ddd")
  .gather(distinctByKeepLast(String::length))
  .toList(); // ["a", "cc", "ddd"]
```

### `distinctUntilChanged()`

Suppresses consecutive duplicate elements (deduplicates runs).

```java
Stream.of(1, 1, 2, 2, 3, 1, 1)
  .gather(distinctUntilChanged())
  .toList(); // [1, 2, 3, 1]
```

### `distinctUntilChanged(Function<T, R>)`

Suppresses consecutive elements where the extracted key is unchanged.

```java
Stream.of("a", "aa", "b", "bb", "bbb", "c")
  .gather(distinctUntilChanged(String::length))
  .toList(); // ["a", "b", "c"]
```

### `zip(Stream<T2>)`

Zips stream elements with elements from another stream, producing `Map.Entry` pairs. Stops at the shorter stream.

```java
Stream.of(1, 2, 3)
  .gather(zip(Stream.of("a", "b", "c")))
  .toList(); // [1="a", 2="b", 3="c"]
```

### `zip(Stream<T2>, BiFunction<T1, T2, R>)`

Zips stream elements with elements from another stream using a custom combiner.

```java
Stream.of(1, 2, 3)
  .gather(zip(Stream.of("a", "b", "c"), (n, s) -> n + s))
  .toList(); // ["1a", "2b", "3c"]
```

### `zip(Iterator<T2>)`

Zips stream elements with elements from an iterator, producing `Map.Entry` pairs.

```java
Stream.of(1, 2, 3)
  .gather(zip(List.of("a", "b", "c").iterator()))
  .toList(); // [1="a", 2="b", 3="c"]
```

### `zip(Iterator<T2>, BiFunction<T1, T2, R>)`

Zips stream elements with elements from an iterator using a custom combiner.

### `zipWithIterable(Iterable<T2>)`

Zips stream elements with elements from an iterable, producing `Map.Entry` pairs.

```java
Stream.of(1, 2, 3)
  .gather(zipWithIterable(List.of("a", "b", "c")))
  .toList(); // [1="a", 2="b", 3="c"]
```

### `zipWithIterable(Iterable<T2>, BiFunction<T1, T2, R>)`

Zips stream elements with elements from an iterable using a custom combiner.

### `zipWithIndex()`

Zips each element with its 0-based index, producing `Map.Entry<T, Long>` pairs.

```java
Stream.of("a", "b", "c")
  .gather(zipWithIndex())
  .toList(); // ["a"=0, "b"=1, "c"=2]
```

### `zipWithIndex(BiFunction<Long, T, R>)`

Zips each element with its index using a custom combiner.

```java
Stream.of("a", "b", "c")
  .gather(zipWithIndex((index, element) -> index + ":" + element))
  .toList(); // ["0:a", "1:b", "2:c"]
```

### `windowSliding(int windowSize, int step)`

Creates sliding windows of a fixed size, advancing by `step` elements each time. Extends `Gatherers.windowSliding(int)` with a configurable step.

```java
Stream.of(1, 2, 3, 4, 5)
  .gather(windowSliding(3, 1))
  .toList(); // [[1,2,3], [2,3,4], [3,4,5]]

Stream.of(1, 2, 3, 4, 5)
  .gather(windowSliding(2, 2))
  .toList(); // [[1,2], [3,4], [5]]
```

### `filteringByIndex(BiPredicate<Long, T>)`

Filters elements based on their 0-based index and value.

```java
Stream.of("a", "b", "c", "d", "e")
  .gather(filteringByIndex((index, element) -> index % 2 == 0))
  .toList(); // ["a", "c", "e"]
```

### `groupingBy(Function<T, K>)`

Groups consecutive elements by a key, emitting `Map.Entry<K, List<T>>` for each group encountered.

```java
Stream.of("a", "bb", "cc", "ddd")
  .gather(groupingBy(String::length))
  .toList(); // [1=["a"], 2=["bb","cc"], 3=["ddd"]]
```

### `groupingBy(Function<T, K>, Collector<T, ?, R>)`

Groups consecutive elements by a key using a custom downstream collector.

```java
Stream.of("a", "bb", "cc", "ddd")
  .gather(groupingBy(String::length, Collectors.counting()))
  .toList(); // [1=1, 2=2, 3=1]
```
