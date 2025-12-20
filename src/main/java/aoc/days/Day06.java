package aoc.days;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.IOUtils;

public class Day06 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(6);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    private static String[] parseOperations(List<String> lines) {
        for (String line : lines) {
            if (line.contains("+") || line.contains("*")) {
                return line.trim().split("\\s+");
            }
        }
        return new String[0];
    }

    private static List<Integer> parseOperationIdx(List<String> lines) {
        List<Integer> idxs = new ArrayList<>();
        int maxlength = 0;
        for (String line : lines) {
            maxlength = Math.max(maxlength, line.length());

            if (line.contains("+") || line.contains("*")) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) != ' ') {
                        idxs.add(i);
                    }
                }
            }
        }
        idxs.add(maxlength + 1);
        return idxs;
    }

    private static List<List<Long>> parseInput(List<String> lines) {
        List<List<Long>> numbers = new ArrayList<>();
        for (String line : lines) {
            if (line.contains("+")) {
                continue;
            }
            int i = 0;
            for (String num : line.trim().split("\\s+")) {
                if (numbers.size() <= i) {
                    numbers.add(new ArrayList<>());
                }
                numbers.get(i).add(Long.parseLong(num));
                i += 1;
            }
        }
        return numbers;
    }

    private static List<List<Long>> parseInput2(List<String> lines, List<Integer> operationIdx) {
        List<List<Long>> numbers = new ArrayList<>();
        for (int i = 0; i < operationIdx.size() - 1; i++) {
            int start = operationIdx.get(i);
            int end = operationIdx.get(i + 1) - 1;

            if (numbers.size() <= i) {
                numbers.add(new ArrayList<>());
            }

            for (int slice = start; slice < end; slice++) {
                long num = 0;
                for (String line : lines) {
                    if (line.length() <= slice) {
                        continue;
                    }

                    char n = line.charAt(slice);
                    if (n != ' ' && n != '+' && n != '*') {
                        num = num * 10 + Character.getNumericValue(n);
                    }
                }
                numbers.get(i).add(num);
            }
        }
        return numbers;
    }

    private static Long performOperations(List<List<Long>> numbers, String[] operations) {
        List<Long> solutions = new ArrayList<>();

        for (int i = 0; i < operations.length; i++) {
            String op = operations[i];
            List<Long> col = numbers.get(i);

            Long result = 0L;
            switch (op) {
                case "+" :
                    result = col.stream().reduce(0L, (a, b) -> a + b);
                    break;
                case "*" :
                    result = col.stream().reduce(1L, (a, b) -> a * b);
                    break;
            }
            solutions.add(result);
        }

        // Sum solutions
        Long finalResult = 0L;
        for (Long sol : solutions) {
            finalResult += sol;
        }
        return finalResult;
    }

    public static Long part1(List<String> lines) {
        List<List<Long>> numbers = parseInput(lines);
        String[] operations = parseOperations(lines);
        return performOperations(numbers, operations);

    }

    public static Long part2(List<String> lines) {
        String[] operations = parseOperations(lines);
        List<Integer> operationIdx = parseOperationIdx(lines);
        List<List<Long>> numbers = parseInput2(lines, operationIdx);
        return performOperations(numbers, operations);
    }
}
