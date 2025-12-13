package aoc.days;

import java.util.ArrayList;
import java.util.List;
import aoc.utils.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import java.lang.Math;
import java.math.BigInteger;

public class Day02 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(2);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    private static List<String> splitLine(String line) {
        return new ArrayList<>(List.of(line.split(",")));
    }

    private static Pair<Long, Long> splitIds(String line) {
        String[] parts = line.split("-");
        Long start = Long.parseLong(parts[0]);
        Long end = Long.parseLong(parts[1]);
        return Pair.of(start, end);
    }

    private static boolean isRepeated(String num, int chunk_size) {
        // First check if is divisible
        if (num.length() % chunk_size != 0) {
            return false;
        }

        String chunk = num.substring(0, chunk_size);

        for (int i = chunk_size; i < num.length(); i += chunk_size) {
            if (!num.substring(i, i + chunk_size).equals(chunk)) {
                return false;
            }
        }
        return true;
    }

    public static Long part1(List<String> lines) {
        Long sum_ids = 0L;
        List<String> id_pairs = splitLine(lines.get(0));

        for (String id_pair : id_pairs) {
            Pair<Long, Long> id_range = splitIds(id_pair);
            Long start = id_range.getLeft();
            Long end = id_range.getRight();

            for (long n = start; n <= end; n++) {
                String num = String.valueOf(n);

                if (num.length() % 2 != 0) {
                    continue;
                }

                if (isRepeated(num, num.length() / 2)) {
                    System.out.println(String.format("Invalid ID: %s", num));
                    sum_ids += n;
                }
            }
        }
        return sum_ids;
    }

    public static long part2(List<String> lines) {
        Long sum_ids = 0L;
        List<String> id_pairs = splitLine(lines.get(0));

        for (String id_pair : id_pairs) {
            Pair<Long, Long> id_range = splitIds(id_pair);
            Long start = id_range.getLeft();
            Long end = id_range.getRight();

            for (long n = start; n <= end; n++) {
                String num = String.valueOf(n);
                for (int chunk_size = 1; chunk_size <= num.length() / 2; chunk_size++) {
                    if (isRepeated(num, chunk_size)) {
                        // System.out.println(String.format("Invalid ID: %s", num));
                        sum_ids += n;
                        break;
                    }
                }
            }
        }
        return sum_ids;
    }
}
