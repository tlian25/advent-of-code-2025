package aoc;

import aoc.days.Day01;

/**
 * Advent of Code Java starter template.
 *
 * <p>
 * Usage: - Compile: javac -d out src/main/java/aoc/DayTemplate.java - Run with
 * file: java -cp out aoc.DayTemplate input/day01.txt - Or pipe input: cat
 * input/day01.txt | java -cp out aoc.DayTemplate
 */
public class AocRunner {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please provide the day number as the first argument.");
        }

        int day = Integer.parseInt(args[0]);

        System.out.println("Running Day " + day);

        switch (day) {
            case 1 -> {
                Day01.run();
            }
            // Add more days here as needed
            default -> System.out.println("Day not implemented yet.");
        }
    }
}
