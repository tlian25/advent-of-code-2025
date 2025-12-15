package aoc.days;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import aoc.utils.IOUtils;

public class Day05 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(5);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    private static List<Pair<Long, Long>> parseRanges(List<String> lines) {
        List<Pair<Long, Long>> ranges = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                break;
            }
            String[] parts = line.split("-");
            Pair<Long, Long> r = Pair.of(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
            ranges.add(r);
        }
        return ranges;
    }

    private static List<Long> parseIngredients(List<String> lines) {
        List<Long> ingredients = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty() || line.contains("-")) {
                continue;
            }
            ingredients.add(Long.parseLong(line));
        }
        return ingredients;
    }

    public static Long part1(List<String> lines) {
        List<Pair<Long, Long>> ranges = parseRanges(lines);
        List<Long> ingredients = parseIngredients(lines);

        Long count = 0L;
        for (Long ingredient : ingredients) {
            for (Pair<Long, Long> range : ranges) {
                if (ingredient >= range.getLeft() && ingredient <= range.getRight()) {
                    // System.err.println("Ingredient " + ingredient + " fits in range " + range);
                    count += 1;
                    break;
                }
            }
        }
        return count;
    }

    public static Long part2(List<String> lines) {
        List<Pair<Long, Long>> ranges = parseRanges(lines);
        ranges.sort((a, b) -> {
            int cmp = a.getLeft().compareTo(b.getLeft());
            return (cmp != 0) ? cmp : a.getRight().compareTo(b.getRight());
        });

        List<Pair<Long, Long>> mergedRanges = new ArrayList<>();
        for (Pair<Long, Long> range : ranges) {
            if (mergedRanges.isEmpty()) {
                mergedRanges.add(range);
                continue;
            }
            Pair<Long, Long> lastRange = mergedRanges.getLast();
            if (range.getLeft() <= lastRange.getRight() + 1) {
                // Overlap or contiguous, merge
                Long newRight = Math.max(lastRange.getRight(), range.getRight());
                mergedRanges.set(mergedRanges.size() - 1, Pair.of(lastRange.getLeft(), newRight));
            } else {
                mergedRanges.add(range);
            }
        }

        Long totalIngredients = 0L;
        for (Pair<Long, Long> range : mergedRanges) {
            totalIngredients += (range.getRight() - range.getLeft() + 1);
        }
        return totalIngredients;
    }
}
