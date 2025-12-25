package aoc.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;

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

    private long maxX;
    private long maxY;
    private HashMap<Long, List<Pair<Long, Long>>> rows;
    private HashMap<Long, List<Pair<Long, Long>>> cols;

    public FilledGrid(long maxX, long maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        rows = new HashMap<>();
        cols = new HashMap<>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Long key : rows.keySet()) {
            sb.append("Row " + key + ": ");
            for (Pair<Long, Long> line : rows.get(key)) {
                sb.append("[" + line.getLeft() + "," + line.getRight() + "] ");
            }
            sb.append("\n");
        }

        for (Long key : cols.keySet()) {
            sb.append("Col " + key + ": ");
            for (Pair<Long, Long> line : cols.get(key)) {
                sb.append("[" + line.getLeft() + "," + line.getRight() + "] ");
            }
            sb.append("\n");
        }
        return sb.toString();

    }

    public void addLine(Point2D start, Point2D end) {
        if (start.y() == end.y()) {
            // Horizontal line
            long y = start.y();
            long x1 = Math.min(start.x(), end.x());
            long x2 = Math.max(start.x(), end.x());
            rows.putIfAbsent(y, new ArrayList<>());
            rows.get(y).add(Pair.of(x1, x2));
        } else if (start.x() == end.x()) {
            // Vertical line
            long x = start.x();
            long y1 = Math.min(start.y(), end.y());
            long y2 = Math.max(start.y(), end.y());
            cols.putIfAbsent(x, new ArrayList<>());
            cols.get(x).add(Pair.of(y1, y2));
        }
    }

    public boolean isInside(Point2D p) {
        // TODO: implement more efficient search
        return false;
    }

    public long insideArea(Point2D a, Point2D b) {
        long y1 = Math.min(a.y(), b.y());
        long y2 = Math.max(a.y(), b.y());
        long x1 = Math.min(a.x(), b.x());
        long x2 = Math.max(a.x(), b.x());

        System.out.println("Checking area between " + a + " and " + b);

        // Check horizontal borders
        for (long y = y1; y <= y2; y++) {
            System.out.println("Checking horizontal border at %s, %s".formatted(x1, y));
            if (!isInside(new Point2D(x1, y))) {
                System.out.println("Failed at " + new Point2D(x1, y));
                return 0L;
            } else if (!isInside(new Point2D(x2, y))) {
                System.out.println("Failed at " + new Point2D(x2, y));
                return 0L;
            }
        }

        // Check vertical borders
        for (long x = x1; x <= x2; x++) {
            System.out.println("Checking horizontal border at %s, %s".formatted(x, y1));
            if (!isInside(new Point2D(x, y1))) {
                System.out.println("Failed at " + new Point2D(x, y1));
                return 0L;
            } else if (!isInside(new Point2D(x, y2))) {
                System.out.println("Failed at " + new Point2D(x, y2));
                return 0L;
            }
        }

        return Point2D.area(a, b);
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

        // Find max X and Y to size the grid
        long maxX = points.stream().mapToLong(Point2D::x).max().orElse(0L);
        long minX = points.stream().mapToLong(Point2D::x).min().orElse(0L);
        long maxY = points.stream().mapToLong(Point2D::y).max().orElse(0L);
        long minY = points.stream().mapToLong(Point2D::y).min().orElse(0L);

        System.out.println("Point bounds: X[" + minX + "," + maxX + "], Y[" + minY + "," + maxY + "]");
        System.out.println("Creating grid of size " + (maxX + 1) + " x " + (maxY + 1));

        FilledGrid grid = new FilledGrid(maxX, maxY);

        for (int i = 1; i < points.size(); i++) {
            Point2D a = points.get(i);
            Point2D b = points.get(i - 1);
            grid.addLine(a, b);
        }
        grid.addLine(points.get(0), points.get(points.size() - 1));
        System.out.println("Constructed grid:\n" + grid.toString());

        // Find largest rectangle that is completely inside the filled area
        long maxArea = 0L;
        for (Set<Point2D> pointPair : Sets.combinations(new HashSet<>(points), 2)) {
            Iterator<Point2D> it = pointPair.iterator();
            Point2D a = it.next();
            Point2D b = it.next();
            long area = grid.insideArea(a, b);
            System.out.println("Checked area between " + a + " and " + b + ": " + area);
            maxArea = Math.max(maxArea, area);
        }
        return maxArea;
    }
}
