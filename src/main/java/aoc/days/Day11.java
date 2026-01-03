package aoc.days;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

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
            String nodeName = parts[0].replace(":", "");
            Node node = new Node(nodeName);
            graph.addNode(node);
            for (int i = 1; i < parts.length; i++) {
                String neighborName = parts[i];
                if (!graph.hasNode(neighborName)) {
                    Node neighborNode = new Node(neighborName);
                    graph.addNode(neighborNode);
                }
                Node neighborNode = graph.getNode(neighborName);
                graph.addNode(neighborNode);
                graph.addEdge(nodeName, neighborName);
            }
        }
        return graph;
    }

    public static Long part1(List<String> lines) {
        Graph11 graph = parseLines(lines);
        return graph.bfs(Constant.YOU, Constant.OUT, new HashSet<>());
    }

    public static Long part2(List<String> lines) {
        Graph11 graph = parseLines(lines);
        // Count of SVR -> DAC
        Long c1 = graph.bfs(Constant.SVR, Constant.DAC, Set.of(Constant.FFT));
        // Count of DAC -> FFT
        Long c2 = graph.bfs(Constant.DAC, Constant.FFT, Set.of(Constant.SVR));
        // Count of FFT -> OUT
        Long c3 = graph.bfs(Constant.FFT, Constant.OUT, Set.of(Constant.DAC));

        // Count of SVR -> FFT
        Long c4 = graph.bfs(Constant.SVR, Constant.FFT, Set.of(Constant.DAC));
        // Count of FFT -> DAC
        Long c5 = graph.bfs(Constant.FFT, Constant.DAC, Set.of(Constant.SVR));
        // Count of DAC -> OUT
        Long c6 = graph.bfs(Constant.DAC, Constant.OUT, Set.of(Constant.FFT));

        // Total Count = SVR -> DAC -> FFT -> OUT + SVR -> FFT -> DAC -> OUT
        return (c1 * c2 * c3) + (c4 * c5 * c6);
        // return 0L;
    }
}

class Node {
    String name;
    List<Node> edges;

    public Node(String name) {
        this.name = name;
    }
}

class Graph11 {
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, Set<Node>> adjList = new HashMap<>();
    private Map<Pair<String, String>, Long> pathCount = new HashMap<>();

    public void addNode(Node node) {
        nodes.put(node.name, node);
    }

    public void addEdge(String from, String to) {
        Node toNode = nodes.get(to);
        adjList.computeIfAbsent(from, k -> new HashSet<>()).add(toNode);
    }

    public Set<Node> getNeighbors(String nodeName) {
        return adjList.getOrDefault(nodeName, new HashSet<>());
    }

    public boolean hasNode(String name) {
        return nodes.containsKey(name);
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    public Long bfs(String start, String target, Set<String> blocked) {
        Long count = 0L;
        List<String> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            if (current.equals(target)) {
                count++;
                continue;
            }
            for (Node neighbor : getNeighbors(current)) {
                if (!blocked.contains(neighbor.name)) {
                    queue.add(neighbor.name);
                    pathCount.put(Pair.of(start, neighbor.name),
                            pathCount.getOrDefault(Pair.of(start, neighbor.name), 0L) + 1);
                }
            }
        }
        System.out.println("Paths from " + start + " to " + target + ": " + count);
        return count;
    }

    public Long getPathCount(String from, String to) {
        return pathCount.getOrDefault(Pair.of(from, to), -1L);
    }
}
