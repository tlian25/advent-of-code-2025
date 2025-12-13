package aoc.days;

import java.util.List;
import aoc.utils.IOUtils;

/**
 * Advent of Code Java starter template.
 *
 * Usage:
 *  - Compile: javac -d out src/main/java/aoc/DayTemplate.java
 *  - Run with file: java -cp out aoc.DayTemplate input/day01.txt
 *  - Or pipe input: cat input/day01.txt | java -cp out aoc.DayTemplate
 */
public class Day01 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(1);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    // Implement Part 1 here. Return a number or string as needed.
    public static long part1(List<String> lines) {
        // Example stub: count non-empty lines
        return lines.stream().filter(s -> !s.isBlank()).count();
    }

    // Implement Part 2 here.
    public static long part2(List<String> lines) {
        // Example stub: return 0
        return 0L;
    }
}
