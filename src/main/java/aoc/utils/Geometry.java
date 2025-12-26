package aoc.utils;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Point-in-polygon utilities. Uses ray-casting (even-odd) rule and a robust
 * on-segment check for boundary handling.
 */
public final class Geometry {
    private final List<Pair<Long, Long>> vertices;
    private final HashMap<Pair<Long, Long>, Boolean> cache;

    public Geometry(List<Point2D> vertices) {
        this.vertices = vertices.stream().map((p) -> Pair.of(p.x(), p.y()))
                .collect(java.util.stream.Collectors.toList());
        this.cache = new HashMap<>();
    }

    /**
     * Return true if point p is inside polygon defined by vertices (in order). If
     * includeBoundary is true, points on edges/vertices are considered inside.
     */
    public boolean pointInPolygon(Pair<Long, Long> p, boolean includeBoundary) {
        if (cache.containsKey(p)) {
            return cache.get(p);
        }

        int n = vertices.size();
        if (n < 3)
            return false;

        double px = p.getLeft(), py = p.getRight();
        boolean inside = false;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            Pair<Long, Long> vi = vertices.get(i);
            Pair<Long, Long> vj = vertices.get(j);
            double xi = vi.getLeft(), yi = vi.getRight();
            double xj = vj.getLeft(), yj = vj.getRight();

            // boundary check: if point lies exactly on edge (vj -> vi)
            if (onSegment(vj, vi, p)) {
                return includeBoundary;
            }

            // ray-casting test: does horizontal ray to +inf cross edge?
            boolean intersect = ((yi > py) != (yj > py)) && (px < (xj - xi) * (py - yi) / (yj - yi) + xi);
            if (intersect)
                inside = !inside;
        }
        cache.put(p, inside);
        return inside;
    }

    // Check if p lies on segment ab (collinear + within bounding box)
    private static boolean onSegment(Pair<Long, Long> a, Pair<Long, Long> b, Pair<Long, Long> p) {
        double cross = (p.getLeft() - a.getLeft()) * (b.getRight() - a.getRight())
                - (p.getRight() - a.getRight()) * (b.getLeft() - a.getLeft());
        if (Math.abs(cross) > 1e-9)
            return false; // not collinear
        double dot = (p.getLeft() - a.getLeft()) * (p.getLeft() - b.getLeft())
                + (p.getRight() - a.getRight()) * (p.getRight() - b.getRight());
        return dot <= 0.0; // p is between a and b (inclusive)
    }
}