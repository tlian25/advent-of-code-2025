package aoc;

import aoc.days.*;

public class AocRunner {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please provide the day number as the first argument.");
        }

        int day = Integer.parseInt(args[0]);

        System.out.println("Running Day " + day);

        switch (day) {
            case 1 -> {
                Day01.run();
            }
            case 2 -> {
                Day02.run();
            }
            case 3 -> {
                Day03.run();
            }
            case 4 -> {
                Day04.run();
            }
            case 5 -> {
                Day05.run();
            }
            case 6 -> {
                Day06.run();
            }
            case 7 -> {
                Day07.run();
            }
            case 8 -> {
                Day08.run();
            }
            // Add more days here as needed
            default -> System.out.println("Day not implemented yet.");
        }
    }
}
