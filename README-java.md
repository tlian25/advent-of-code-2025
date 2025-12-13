# Java starter for Advent of Code

This repository contains a minimal Java template for solving Advent of Code puzzles.

Files added
- `src/main/java/aoc/DayTemplate.java`: a small template with `part1`/`part2` stubs and input reading.

Quick start (macOS / Linux / any system with a JDK >= 11):

Compile:
```zsh
javac -d out src/main/java/aoc/DayTemplate.java
```

Run with an input file:
```zsh
java -cp out aoc.DayTemplate input/day01.txt
```

Or pipe input:
```zsh
cat input/day01.txt | java -cp out aoc.DayTemplate
```

Notes
- The template reads from a filename passed as the first arg, or from stdin if no args are given.
- Edit `part1` and `part2` to implement puzzle logic. Return `long` values (or change the signatures if you prefer strings).
- A Gradle build file is included to build and run the template.

Using Gradle (system Gradle or wrapper):

Generate a wrapper (optional but recommended):
```zsh
gradle wrapper
```

Build and run with system Gradle:
```zsh
gradle build
gradle run --args="input/day01.txt"
```

Or with the wrapper (after running `gradle wrapper`):
```zsh
./gradlew build
./gradlew run --args="input/day01.txt"
```

The `application` plugin is configured with main class `aoc.DayTemplate`.
