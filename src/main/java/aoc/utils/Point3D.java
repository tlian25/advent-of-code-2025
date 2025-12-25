package aoc.utils;

public class Point3D {
    int x;
    int y;
    int z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Double distance(Point3D a, Point3D b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
    }

    public String toString() {
        return String.format("(%d, %d, %d)", x, y, z);
    }

    public int getX() {
        return this.x;
    }
}