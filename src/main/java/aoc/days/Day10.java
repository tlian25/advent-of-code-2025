package aoc.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import aoc.utils.IOUtils;

public class Day10 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(10);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    public static Machine1 setupMachine1(String line) {
        Machine1 machine = new Machine1();
        String[] parts = line.split(" ");
        for (String part : parts) {
            if (part.startsWith("[")) {
                machine.setTarget(part);
            } else if (part.startsWith("(")) {
                machine.addInstruction(part);
            }
        }
        return machine;
    }

    public static Machine2 setupMachine2(String line) {
        Machine2 machine = new Machine2();
        String[] parts = line.split(" ");
        for (String part : parts) {
            if (part.startsWith("{")) {
                machine.setTarget(part);
            } else if (part.startsWith("(")) {
                machine.addInstruction(part);
            }
        }
        return machine;
    }

    public static Long part1(List<String> lines) {
        Long total_steps = 0L;
        for (String line : lines) {
            Machine1 machine = setupMachine1(line);
            total_steps += machine.bfs();
        }
        return total_steps;
    }

    public static Long part2(List<String> lines) {
        Long total_steps = 0L;
        for (String line : lines) {
            Machine2 machine = setupMachine2(line);
            System.out.print(machine);
            total_steps += machine.bfs();
        }
        return total_steps;
    }
}

class Machine1 {
    private List<Integer> lights;
    private Integer target;
    private List<List<Integer>> instructions;

    public Machine1() {
        this.lights = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Machine\n");
        sb.append("Target: ");
        sb.append(target);
        sb.append("\n");
        sb.append("State:");
        for (Integer light : lights) {
            sb.append(light.toString());
        }
        sb.append("\nInstructions:\n");
        for (List<Integer> inst : instructions) {
            sb.append(inst.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public void setTarget(String targetStr) {
        String s = targetStr.replace("[", "").replace("]", "").trim();
        List<Integer> sb = new ArrayList<>();
        for (char c : s.toCharArray()) {
            if (c == '#')
                sb.add(1);
            else if (c == '.')
                sb.add(0);

            // Also set lights to initial state
            this.lights.add(0);
        }
        this.target = listToInt(sb);
    }

    public static Integer listToInt(List<Integer> lst) {
        Integer result = 0;
        for (Integer i : lst) {
            result = (result << 1) | i;
        }
        return result;
    }

    public void addInstruction(String instruction) {
        // (1,2,3)
        List<Integer> inst = Arrays.stream(instruction.replace("(", "").replace(")", "").split(","))
                .map(Integer::parseInt).toList();
        instructions.add(inst);
    }

    public Integer bfs() {
        HashMap<Integer, Integer> seen = new HashMap<>();
        List<Pair<List<Integer>, Integer>> queue = new ArrayList<>();
        queue.add(Pair.of(this.lights, 0));
        seen.put(0, 0);

        while (!queue.isEmpty()) {
            Pair<List<Integer>, Integer> pair = queue.removeFirst();
            List<Integer> state = pair.getLeft();
            Integer steps = pair.getRight();
            for (List<Integer> inst : instructions) {
                // apply instructions across bits in current stats
                List<Integer> newState = new ArrayList<>(state);
                for (Integer idx : inst) {
                    newState.set(idx, 1 - newState.get(idx)); // Toggle
                }
                Integer newStateInt = listToInt(newState);
                if (newStateInt.equals(target)) {
                    System.out.println("Reached target in " + (steps + 1) + " steps.");
                    return steps + 1;
                }
                if (!seen.containsKey(newStateInt)) {
                    seen.put(newStateInt, steps + 1);
                    queue.add(Pair.of(newState, steps + 1));
                }
            }
        }
        return -1; // Not found
    }
}

class Machine2 {
    private List<Integer> counters;
    private String targetStr;
    private List<Integer> target;
    private List<List<Integer>> instructions;

    public Machine2() {
        this.counters = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Machine\n");
        sb.append("Target: ");
        sb.append(target);
        sb.append("\n");
        sb.append("State:");
        for (Integer counter : counters) {
            sb.append(counter.toString());
        }
        sb.append("\nInstructions:\n");
        for (List<Integer> inst : instructions) {
            sb.append(inst.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String counterString(List<Integer> counters) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(counters.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(",")));
        sb.append("}");
        return sb.toString();
    }

    public void setTarget(String target) {
        System.err.println("Setting target to " + target);
        String s = target.replace("{", "").replace("}", "").trim();
        List<Integer> sb = new ArrayList<>();
        for (String c : s.split(",")) {
            sb.add(Integer.valueOf(c));
            this.counters.add(0);
        }
        this.targetStr = counterString(sb);
        this.target = sb;
    }

    public void addInstruction(String instruction) {
        // (1,2,3)
        List<Integer> inst = Arrays.stream(instruction.replace("(", "").replace(")", "").split(","))
                .map(Integer::parseInt).toList();
        instructions.add(inst);
    }

    public Integer bfs() {
        HashMap<String, Integer> seen = new HashMap<>();
        List<Pair<List<Integer>, Integer>> queue = new ArrayList<>();
        queue.add(Pair.of(this.counters, 0));
        seen.put(counterString(this.counters), 0);

        while (!queue.isEmpty()) {
            Pair<List<Integer>, Integer> pair = queue.removeFirst();
            List<Integer> state = pair.getLeft();
            Integer steps = pair.getRight();

            boolean valid = true;
            for (List<Integer> inst : instructions) {
                // apply instructions across bits in current stats

                List<Integer> newState = new ArrayList<>(state);
                for (Integer idx : inst) {
                    if (newState.get(idx) >= target.get(idx)) {
                        valid = false;
                        break;
                    }
                    newState.set(idx, newState.get(idx) + 1); // Toggle
                }

                if (!valid)
                    continue;

                String newStateString = counterString(newState);
                if (newStateString.equals(this.targetStr)) {
                    System.out.println("Reached target in " + (steps + 1) + " steps.");
                    return steps + 1;
                }
                if (!seen.containsKey(newStateString)) {
                    seen.put(newStateString, steps + 1);
                    queue.add(Pair.of(newState, steps + 1));
                }
            }
        }
        return -1; // Not found
    }
}