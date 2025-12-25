package aoc.days;

import java.util.List;
import aoc.utils.IOUtils;

public class Day07 {
    private static final char EMPTY = '.';
    private static final char START = 'S';
    private static final char SPLITTER = '^';
    private static final char BEAM = '|';

    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(7);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    private static List<List<Character>> parseInput(List<String> lines) {
        return lines.stream()
                .map(line -> line.chars().mapToObj(c -> (char) c)
                        .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new)))
                .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
    }

    public static Long part1(List<String> lines) {
        List<List<Character>> grid = parseInput(lines);

        Long splitCount = 0L;

        for (int l = 1; l < grid.size(); l++) {
            for (int c = 0; c < grid.get(0).size(); c++) {
                char current = grid.get(l).get(c);
                char above = grid.get(l - 1).get(c);

                if (above == BEAM || above == START) {
                    if (current == EMPTY) {
                        grid.get(l).set(c, BEAM);
                    } else if (current == SPLITTER) {
                        splitCount += 1;
                        grid.get(l).set(c - 1, BEAM);
                        grid.get(l).set(c + 1, BEAM);
                    }
                }
            }
        }
        return splitCount;
    }

    public static Long part2(List<String> lines) {
        List<List<Character>> grid = parseInput(lines);

        // Create a second grid to keep track of counts of split possibilities at each
        // grid cell
        List<List<Long>> countGrid = grid.stream()
                .map(row -> row.stream().map(cell -> 0L)
                        .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new)))
                .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));

        for (int l = 1; l < grid.size(); l++) {
            for (int c = 0; c < grid.get(0).size(); c++) {
                char current = grid.get(l).get(c);

                char above = grid.get(l - 1).get(c);

                // This is only needed once but we but here to bootstrap
                if (above == START) {
                    countGrid.get(l - 1).set(c, 1L);
                }
                Long countAbove = countGrid.get(l - 1).get(c);

                if (above == BEAM || above == START) {
                    if (current == SPLITTER) {
                        grid.get(l).set(c - 1, BEAM);
                        grid.get(l).set(c + 1, BEAM);
                        // Add count to both sides of the splitter
                        countGrid.get(l).set(c - 1, countGrid.get(l).get(c - 1) + countAbove);
                        countGrid.get(l).set(c + 1, countGrid.get(l).get(c + 1) + countAbove);
                    } else {
                        grid.get(l).set(c, BEAM);
                        // Add count straight down
                        countGrid.get(l).set(c, countGrid.get(l).get(c) + countAbove);
                    }
                }

            }
        }
        return Long.valueOf(countGrid.get(grid.size() - 1).stream().reduce(0L, Long::sum));
    }
}
