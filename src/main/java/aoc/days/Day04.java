package aoc.days;

import java.util.List;

import aoc.utils.Constant;
import aoc.utils.IOUtils;

class Grid {
    private final char[][] grid;
    private final int rows;
    private final int cols;

    public Grid(List<String> lines) {
        this.rows = lines.size();
        this.cols = lines.get(0).length();
        this.grid = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < cols; c++) {
                grid[r][c] = line.charAt(c);
            }
        }
    }

    public char getCell(int r, int c) {
        return grid[r][c];
    }

    public void setCell(int r, int c, char value) {
        grid[r][c] = value;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getAdjacentRolls(int r, int c) {
        int roll_count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue; // Skip self
                }
                int nr = r + dr;
                int nc = c + dc;
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    if (grid[nr][nc] == Constant.ROLL) {
                        roll_count += 1;
                    }
                }
            }
        }
        return roll_count;
    }
}

public class Day04 {
    public static void run() throws Exception {
        List<String> lines = IOUtils.readLinesForDay(4);
        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    public static Long part1(List<String> lines) {
        Grid grid = new Grid(lines);

        Long can_access = 0L;
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                char cell = grid.getCell(r, c);
                if (cell == Constant.ROLL) {
                    int adjacent_rolls = grid.getAdjacentRolls(r, c);
                    // System.out.println(String.format("Roll at (%d, %d) has %d adjacent rolls", r,
                    // c, adjacent_rolls));
                    if (adjacent_rolls < 4) {
                        can_access += 1;
                    }
                }
            }
        }
        return can_access;
    }

    public static Long part2(List<String> lines) {
        Grid grid = new Grid(lines);

        Long can_access = 0L;
        boolean removed = true;

        while (removed) {
            removed = false;

            for (int r = 0; r < grid.getRows(); r++) {
                for (int c = 0; c < grid.getCols(); c++) {
                    char cell = grid.getCell(r, c);
                    if (cell == Constant.ROLL) {
                        int adjacent_rolls = grid.getAdjacentRolls(r, c);
                        // System.out.println(String.format("Roll at (%d, %d) has %d adjacent rolls", r,
                        // c, adjacent_rolls));
                        if (adjacent_rolls < 4) {
                            grid.setCell(r, c, Constant.SPACE);
                            can_access += 1;
                            removed = true;
                        }
                    }
                }
            }

        }
        return can_access;
    }
}
