package aoc.days;

import java.util.List;
import aoc.utils.IOUtils;

/**
 * Advent of Code Java starter template.
 *
 * Usage: - Compile: javac -d out src/main/java/aoc/DayTemplate.java - Run with
 * file: java -cp out aoc.DayTemplate input/day01.txt - Or pipe input: cat
 * input/day01.txt | java -cp out aoc.DayTemplate
 */
public class Day01 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(1);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    public static long part1(List<String> lines) {
        int current_num = 50;
        int new_num;
        int zero_count = 0;

        for (String line : lines) {
            int direction = (line.charAt(0) == 'R') ? 1 : -1;
            int turns = Integer.parseInt(line.substring(1));
            new_num = (current_num + direction * turns);
            while (new_num < 0) {
                new_num += 100;
            }
            new_num %= 100;

            current_num = new_num;
            if (current_num == 0) {
                zero_count += 1;
            }
        }
        return zero_count;

    }

    public static long part2(List<String> lines) {
        // Brute force. Revisit with a more elegant solution later.
        int current_num = 50;
        int zero_count = 0;

        for (String line : lines) {
            int direction = (line.charAt(0) == 'R') ? 1 : -1;
            int turns = Integer.parseInt(line.substring(1));

            while (turns > 0) {
                current_num += direction;
                System.out.println("Current num: " + current_num);
                if (current_num == 0) {
                    zero_count += 1;
                } else if (current_num < 0) {
                    current_num += 100;
                } else if (current_num == 100) {
                    current_num -= 100;
                    zero_count += 1;
                }
                turns -= 1;
            }
        }
        return zero_count;
    }
}
