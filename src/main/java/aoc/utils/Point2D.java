package aoc.utils;

public class Point2D {
    long x;
    long y;

    public Point2D(int x, int y) {
        this.x = (long) x;
        this.y = (long) y;
    }

    public Point2D(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public static Long area(Point2D a, Point2D b) {
        return Long.valueOf(Math.abs(a.x - b.x) + 1) * Long.valueOf(Math.abs(a.y - b.y) + 1);
    }

    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    public long x() {
        return x;
    }

    public long y() {
        return y;
    }
}
