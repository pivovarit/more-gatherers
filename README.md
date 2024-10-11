# more-gatherers

Missing Stream API functionality you always longed for - provided via `Gatherers`

[![build](https://github.com/pivovarit/more-gatherers/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/pivovarit/more-gatherers/actions/workflows/build.yml)
[![pitest](https://github.com/pivovarit/more-gatherers/actions/workflows/pitest.yml/badge.svg?branch=main)](https://pivovarit.github.io/more-gatherers)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pivovarit/more-gatherers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.pivovarit/more-gatherers)

[![Stargazers over time](https://starchart.cc/pivovarit/more-gatherers.svg?variant=adaptive)](https://starchart.cc/pivovarit/more-gatherers)

## Project is under intense development and will be released alongside Java 24, when Stream Gatherers go GA (hopefully)

### Overview

Java's Stream API is a powerful tool for processing collections of data. However, it lacks some functionality that could make it even more powerful. This library aims to fill that gap by providing a set of `Gatherers` that can be used to collect data from a stream in a more flexible way.

Whenever possible, the library follows Project Reactor's naming conventions.

Provided `Gatherers`:
- `MoreGatherers.last(int)`
- `MoreGatherers.sample(int)`
- `MoreGatherers.zip(Iterator<T2>)`
- `MoreGatherers.zip(Iterator<T2>, BiFunction<T1,T2>)`
- `MoreGatherers.zip(Stream<T2>)`
- `MoreGatherers.zip(Stream<T2>, BiFunction<T1,T2>)`
- `MoreGatherers.zipWithIterable(Iterable<T2>)`
- `MoreGatherers.zipWithIterable(Iterable<T2>, BiFunction<T1,T2>)`
- `MoreGatherers.zipWithIndex()`
- `MoreGatherers.zipWithIndex(BiFunction<Long,T>)`
- `MoreGatherers.distinctBy(Function<T, R>)`
- `MoreGatherers.distinctByKeepLast(Function<T, R>)`
- `MoreGatherers.distinctUntilChanged()`
- `MoreGatherers.distinctUntilChanged(Function<T, R>)`
