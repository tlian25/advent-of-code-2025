package aoc.days;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;
import com.google.common.collect.Sets;

import org.apache.commons.lang3.tuple.Triple;

import aoc.utils.IOUtils;
import aoc.utils.Point3D;

public class Day08 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(8);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    private static List<Point3D> parseInput(List<String> lines) {
        return lines.stream().map(line -> {
            String[] parts = line.split(",");
            return new Point3D(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }).collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
    }

    private static Queue<Triple<Double, Integer, Integer>> getAllDistances(List<Point3D> points) {
        // Heap of (distance, point1_index, point2_index) ordered by distance
        Queue<Triple<Double, Integer, Integer>> queue = new PriorityQueue<>(
                (a, b) -> Double.compare(a.getLeft(), b.getLeft()));

        // Use LinkedHashSet to preserve insertion order from the list
        Set<Integer> indexSet = new HashSet<>();

        // Map points to their original indices to avoid indexOf calls
        for (int i = 0; i < points.size(); i++) {
            indexSet.add(i);
        }

        for (Set<Integer> pair : Sets.combinations(indexSet, 2)) {
            Iterator<Integer> it = pair.iterator();
            int i = it.next();
            int j = it.next();
            Point3D a = points.get(i);
            Point3D b = points.get(j);
            double d = Point3D.distance(a, b);
            queue.add(Triple.of(d, i, j));
        }

        return queue;
    }

    public static Long part1(List<String> lines) {
        List<Point3D> points = parseInput(lines);
        Queue<Triple<Double, Integer, Integer>> distanceQueue = getAllDistances(points);
        Graph graph = new Graph(points.size());

        for (int connections = 0; connections < 1000; connections++) {
            Triple<Double, Integer, Integer> entry = distanceQueue.poll();
            graph.addEdge(entry.getMiddle(), entry.getRight());
        }

        // Multiply sizes of three largest components
        List<Set<Integer>> components = graph.getConnectedComponents();
        List<Long> componentSizes = components.stream().map(Set::size).map(Long::valueOf)
                .sorted(java.util.Collections.reverseOrder()).toList();

        return componentSizes.stream().limit(3).reduce(1L, (a, b) -> a * b);
    }

    public static Long part2(List<String> lines) {
        List<Point3D> points = parseInput(lines);
        Queue<Triple<Double, Integer, Integer>> distanceQueue = getAllDistances(points);
        Graph graph = new Graph(points.size());

        Triple<Double, Integer, Integer> lastSeen = Triple.of(0.0, -1, -1);
        while (!graph.seenAll() && !distanceQueue.isEmpty()) {
            Triple<Double, Integer, Integer> entry = distanceQueue.poll();
            graph.addEdge(entry.getMiddle(), entry.getRight());
            lastSeen = entry;
        }

        // Multiply X coordinates of last two points to be connected
        Point3D a = points.get(lastSeen.getMiddle());
        Point3D b = points.get(lastSeen.getRight());

        return Long.valueOf(a.getX()) * Long.valueOf(b.getX());
    }
}

class Graph {
    private final Map<Integer, Set<Integer>> adjList = new LinkedHashMap<>();
    private Set<Integer> notSeen = new HashSet<>();

    public Graph(int numNodes) {
        this.notSeen = new HashSet<>();
        for (int i = 0; i < numNodes; i++) {
            this.notSeen.add(i);
        }
    }

    public boolean seenAll() {
        return this.notSeen.isEmpty();
    }

    public void addEdge(int u, int v) {
        this.adjList.putIfAbsent(u, new HashSet<>());
        this.adjList.putIfAbsent(v, new HashSet<>());
        this.adjList.get(u).add(v);
        this.adjList.get(v).add(u);

        this.notSeen.remove(u);
        this.notSeen.remove(v);
    }

    public Set<Integer> getNeighbors(int u) {
        return this.adjList.getOrDefault(u, new HashSet<>());
    }

    public List<Set<Integer>> getConnectedComponents() {
        Set<Integer> visited = new HashSet<>();
        List<Set<Integer>> components = new java.util.ArrayList<>();

        for (Integer node : this.adjList.keySet()) {
            if (!visited.contains(node)) {
                Set<Integer> component = new HashSet<>();
                exploreComponent(node, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private void exploreComponent(Integer node, Set<Integer> visited, Set<Integer> component) {
        visited.add(node);
        component.add(node);

        for (Integer neighbor : getNeighbors(node)) {
            if (!visited.contains(neighbor)) {
                exploreComponent(neighbor, visited, component);
            }
        }
    }
}