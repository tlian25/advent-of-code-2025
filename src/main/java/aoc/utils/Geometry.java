package aoc.utils;

import java.util.List;

/**
 * Point-in-polygon utilities. Uses ray-casting (even-odd) rule and a robust
 * on-segment check for boundary handling.
 */
public final class Geometry {
    private Geometry() {
    }

    /**
     * Return true if point p is inside polygon defined by vertices (in order). If
     * includeBoundary is true, points on edges/vertices are considered inside.
     */
    public static boolean pointInPolygon(List<Point2D> vertices, Point2D p, boolean includeBoundary) {
        int n = vertices.size();
        if (n < 3)
            return false;

        double px = p.x(), py = p.y();
        boolean inside = false;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            Point2D vi = vertices.get(i);
            Point2D vj = vertices.get(j);
            double xi = vi.x(), yi = vi.y();
            double xj = vj.x(), yj = vj.y();

            // boundary check: if point lies exactly on edge (vj -> vi)
            if (onSegment(vj, vi, p)) {
                return includeBoundary;
            }

            // ray-casting test: does horizontal ray to +inf cross edge?
            boolean intersect = ((yi > py) != (yj > py)) && (px < (xj - xi) * (py - yi) / (yj - yi) + xi);
            if (intersect)
                inside = !inside;
        }
        return inside;
    }

    // Check if p lies on segment ab (collinear + within bounding box)
    private static boolean onSegment(Point2D a, Point2D b, Point2D p) {
        double cross = (p.x() - a.x()) * (b.y() - a.y()) - (p.y() - a.y()) * (b.x() - a.x());
        if (Math.abs(cross) > 1e-9)
            return false; // not collinear
        double dot = (p.x() - a.x()) * (p.x() - b.x()) + (p.y() - a.y()) * (p.y() - b.y());
        return dot <= 0.0; // p is between a and b (inclusive)
    }
}