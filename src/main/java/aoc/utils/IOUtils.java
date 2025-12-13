package aoc.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Small IO helper for reading puzzle input. */
public final class IOUtils {
    /**
     * Read input from the first command-line argument (filename) or stdin if no
     * args are provided. Returns the input as a list of lines.
     */
    public static List<String> readLinesFromArgsOrStdin(String[] args) throws IOException {
        String input;
        if (args != null && args.length > 0) {
            input = Files.readString(Path.of(args[0]));
        } else {
            input = new String(System.in.readAllBytes());
        }
        return input.lines().toList();
    }

    public static List<String> readLinesForDay(int day) throws IOException {
        String filepath = String.format("input/day%02d.txt", day);
        String input = Files.readString(Path.of(filepath));
        return input.lines().toList();
    }
}
