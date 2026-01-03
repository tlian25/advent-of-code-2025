package aoc.days;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc.utils.Constant;
import aoc.utils.IOUtils;

public class Day11 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(11);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    public static Graph11 parseLines(List<String> lines) {
        Graph11 graph = new Graph11();
        for (String line : lines) {
            String[] parts = line.split(" ");
            String node = parts[0].replace(":", "");
            graph.addNode(node);
            for (int i = 1; i < parts.length; i++) {
                String neighbor = parts[i];
                if (!graph.hasNode(neighbor)) {
                    graph.addNode(neighbor);
                }
                graph.addEdge(node, neighbor);
            }
        }
        return graph;
    }

    public static Long part1(List<String> lines) {
        Graph11 graph = parseLines(lines);
        return graph.getPathCountMap(Constant.OUT).get(Constant.YOU);
    }

    public static Long part2(List<String> lines) {
        Graph11 graph = parseLines(lines);

        // Count of SVR -> DAC
        // Long c1 = graph.bfs(Constant.SVR, Constant.DAC, Set.of(Constant.FFT));
        Long c1 = graph.getPathCountMap(Constant.DAC).get(Constant.SVR);
        // Count of DAC -> FFT
        Long c2 = graph.getPathCountMap(Constant.FFT).get(Constant.DAC);
        // Count of FFT -> OUT
        Long c3 = graph.getPathCountMap(Constant.OUT).get(Constant.FFT);

        // Count of SVR -> FFT
        Long c4 = graph.getPathCountMap(Constant.FFT).get(Constant.SVR);
        // Count of FFT -> DAC
        Long c5 = graph.getPathCountMap(Constant.DAC).get(Constant.FFT);
        // Count of DAC -> OUT
        Long c6 = graph.getPathCountMap(Constant.OUT).get(Constant.DAC);

        // Total Count = SVR -> DAC -> FFT -> OUT + SVR -> FFT -> DAC -> OUT
        return (c1 * c2 * c3) + (c4 * c5 * c6);
    }
}

class Graph11 {
    private Set<String> nodes = new HashSet<>();
    private Map<String, Set<String>> adjList = new HashMap<>();

    public void addNode(String node) {
        nodes.add(node);
    }

    public void addEdge(String from, String to) {
        adjList.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }

    public Set<String> getNeighbors(String nodeName) {
        return adjList.getOrDefault(nodeName, new HashSet<>());
    }

    public boolean hasNode(String name) {
        return nodes.contains(name);
    }

    public String getNode(String name) {
        return name;
    }

    public Map<String, Long> getPathCountMap(String target) {
        Map<String, Long> counts = new HashMap<>();
        Map<String, Long> newCounts = new HashMap<>();
        for (String node : nodes) {
            newCounts.put(node, node.equals(target) ? 1L : 0L);
        }

        while (!counts.equals(newCounts)) {
            counts = newCounts;
            newCounts = new HashMap<>();
            for (String node : nodes) {
                if (node.equals(target)) {
                    newCounts.put(node, 1L);
                    continue;
                }

                // Sum counts of neighbors
                Long sum = 0L;
                for (String neighbor : getNeighbors(node)) {
                    sum += counts.get(neighbor);
                }
                newCounts.put(node, sum);
            }
        }
        return counts;
    }

}
