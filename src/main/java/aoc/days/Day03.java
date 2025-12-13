package aoc.days;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import aoc.utils.IOUtils;

public class Day03 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(3);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }
    /*
     * Need exactly two batteries from each line Joltage = number formed by digits
     * on the batteries you've turned on Cannot rearrange the batteries. Find
     * largest Joltage possible.
     */

    private static List<Integer> parseLine(String line) {
        List<Integer> batteries = new ArrayList<>();
        for (char c : line.toCharArray()) {
            batteries.add(Character.getNumericValue(c));
        }
        return batteries;
    }

    private static Integer maxJoltage(List<Integer> batteries) {
        Integer max_joltage = 0;
        Integer current_first_num = -1;
        for (int b : batteries) {
            // First check if can form a larger joltage using b as second num
            Integer potential_joltage = current_first_num * 10 + b;
            max_joltage = Math.max(max_joltage, potential_joltage);

            // Then check if b can be the first num
            current_first_num = Math.max(current_first_num, b);
        }
        return max_joltage;
    }

    private static Long maxJoltage2(List<Integer> batteries) {
        int remove_count = batteries.size() - 12;
        RemoveSmallestDigits remover = new RemoveSmallestDigits(batteries);
        remover.removeSmallestDigits(remove_count);
        return remover.getMaxJoltage();
    }

    public static Long part1(List<String> lines) {
        Long total_joltage = 0L;
        for (String line : lines) {
            List<Integer> batteries = parseLine(line);
            Integer line_joltage = maxJoltage(batteries);
            System.out.println(String.format("Line Joltage: %d", line_joltage));
            total_joltage += line_joltage;
        }
        return total_joltage;
    }

    public static Long part2(List<String> lines) {
        // DFS approach
        Long total_joltage = 0L;
        for (String line : lines) {
            List<Integer> batteries = parseLine(line);
            Long line_joltage = maxJoltage2(batteries);
            System.out.println(String.format("Line Joltage: %d", line_joltage));
            total_joltage += line_joltage;
        }
        return total_joltage;
    }
}

class DFS {
    private List<Integer> taken = new ArrayList<>();
    private Long max_joltage = 0L;

    public void search(List<Integer> batteries, int index, int count) {
        if (index + count > batteries.size()) {
            return;
        }

        // Take index
        taken.add(batteries.get(index));
        count -= 1;

        if (count == 0) {
            Long joltage = calculateJoltage(taken);
            max_joltage = Math.max(max_joltage, joltage);
        } else {
            search(batteries, index + 1, count);
        }

        // Reset state
        taken.removeLast();
        count += 1;

        // Not take index
        search(batteries, index + 1, count);
    }

    public Long getMaxJoltage() {
        return max_joltage;
    }

    private Long calculateJoltage(List<Integer> taken) {
        Long joltage = 0L;
        for (int i = 0; i < taken.size(); i++) {
            joltage = joltage * 10 + taken.get(i);
        }
        return joltage;
    }
}

class RemoveSmallestDigits {
    private List<Integer> digits;
    private HashSet<Integer> removedIndices;

    public RemoveSmallestDigits(List<Integer> digits) {
        this.digits = new ArrayList<>(digits);
        this.removedIndices = new HashSet<>();
    }

    public void removeSmallestDigits(int removeCount) {
        while (removeCount > 0) {
            int curr_min = 10;
            int curr_min_index = -1;

            for (int i = 0; i < digits.size(); i++) {
                if (removedIndices.contains(i)) {
                    continue;
                }

                // If we see a digit larger than curr_min, we remove curr_min to maximize final
                // number
                if (digits.get(i) > curr_min) {
                    break;
                } else if (digits.get(i) < curr_min) {
                    curr_min = digits.get(i);
                    curr_min_index = i;
                }
            }
            // If nothing removed, then we remove min index
            this.removedIndices.add(curr_min_index);
            removeCount -= 1;
        }
    }

    public long getMaxJoltage() {
        long joltage = 0L;
        for (int i = 0; i < digits.size(); i++) {
            if (removedIndices.contains(i)) {
                continue;
            }
            joltage = joltage * 10 + digits.get(i);
        }
        return joltage;
    }

}
