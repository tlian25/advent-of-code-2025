package aoc.days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;

import aoc.utils.Geometry;
import aoc.utils.IOUtils;
import aoc.utils.Point2D;

class GraphLines {
    protected List<List<Pair<Long, Long>>> horizontalLines;
    protected List<List<Pair<Long, Long>>> verticalLines;

    public GraphLines() {
        this.horizontalLines = new ArrayList<>();
        this.verticalLines = new ArrayList<>();
    }

    public void addLine(Point2D start, Point2D end) {
        if (start.y() == end.y()) {
            // Horizontal line
            long y = start.y();
            long x1 = Math.min(start.x(), end.x());
            long x2 = Math.max(start.x(), end.x());
            while (horizontalLines.size() <= y) {
                horizontalLines.add(new ArrayList<>());
            }
            horizontalLines.get((int) y).add(Pair.of(x1, x2));
        } else if (start.x() == end.x()) {
            // Vertical line
            long x = start.x();
            long y1 = Math.min(start.y(), end.y());
            long y2 = Math.max(start.y(), end.y());
            while (verticalLines.size() <= x) {
                verticalLines.add(new ArrayList<>());
            }
            verticalLines.get((int) x).add(Pair.of(y1, y2));
        }
    }

}

class FilledGrid {

    private final List<Point2D> vertices;
    private final Geometry geometry;

    public FilledGrid() {
        vertices = new ArrayList<>();
        geometry = new Geometry(vertices);
    }

    public void addVertex(Point2D p) {
        // record vertex (avoid consecutive duplicates)
        if (vertices.isEmpty()) {
            vertices.add(p);
        } else {
            Point2D last = vertices.get(vertices.size() - 1);
            if (last.x() != p.x() || last.y() != p.y()) {
                vertices.add(p);
            }
        }
    }

    private boolean isInside(Long x, Long y) {
        Pair<Long, Long> point = Pair.of(x, y);
        return geometry.pointInPolygon(point, true);
    }

    public boolean boxIsInside(Point2D a, Point2D b) {
        long y1 = Math.min(a.y(), b.y());
        long y2 = Math.max(a.y(), b.y());
        long x1 = Math.min(a.x(), b.x());
        long x2 = Math.max(a.x(), b.x());

        // Check horizontal borders
        for (long y = y1; y <= y2; y++) {
            if (!isInside(x1, y) || !isInside(x2, y)) {
                return false;
            }
        }

        // Check vertical borders
        for (long x = x1; x <= x2; x++) {
            if (!isInside(x, y1) || !isInside(x, y2)) {
                return false;
            }
        }

        return true;
    }
}

public class Day09 {

    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(9);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    private static List<Point2D> parseInput(List<String> lines) {
        return lines.stream().map(line -> {
            String[] parts = line.split(",");
            return new Point2D(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }).toList();
    }

    public static Long part1(List<String> lines) {
        List<Point2D> points = parseInput(lines);

        Long maxArea = 0L;

        for (Set<Point2D> pointPair : Sets.combinations(new HashSet<>(points), 2)) {
            Iterator<Point2D> it = pointPair.iterator();
            Point2D a = it.next();
            Point2D b = it.next();
            maxArea = Math.max(maxArea, Point2D.area(a, b));
        }
        return maxArea;

    }

    public static Long part2(List<String> lines) {
        List<Point2D> points = parseInput(lines);

        FilledGrid grid = new FilledGrid();
        for (Point2D p : points) {
            grid.addVertex(p);
        }

        // Find largest rectangle that is completely inside the filled area
        long maxArea = 0L;
        for (Set<Point2D> pointPair : Sets.combinations(new HashSet<>(points), 2)) {
            Iterator<Point2D> it = pointPair.iterator();
            Point2D a = it.next();
            Point2D b = it.next();
            long area = Point2D.area(a, b);
            if (area > maxArea && grid.boxIsInside(a, b)) {
                System.out.println("Updating max area: " + area);
                maxArea = area;
            }
        }
        return maxArea;
    }
}
