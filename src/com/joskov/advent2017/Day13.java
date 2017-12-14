package com.joskov.advent2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 extends Helpers {
    private final static String INPUT = "resources/13.txt";

    public static void main(String[] args) {
        List<Scanner> input = readInput();
        partOne(input);
        partTwo(input);
    }

    public static void partOne(List<Scanner> input) {
        int sum = input.stream().filter(a -> a.isActiveAt(a.getDepth())).mapToInt(Scanner::getSeverity).sum();
        System.out.println("Scanner severity is: " + sum);
    }

    public static void partTwo(List<Scanner> input) {
        int delay = 0;
        while (true) {
            final int testWith = delay;
            boolean passed = input.stream().noneMatch(a -> a.isActiveAt(a.getDepth() + testWith));
            if (passed) {
                break;
            }
            delay++;
        }
        System.out.println("Passed with delay: " + delay);
    }

    public static List<Scanner> readInput() {
        try {
            String[] strings = Helpers.read(INPUT).trim().split("\\n|\\r");
            return Arrays.stream(strings).map(Scanner::parse).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    private static class Scanner {
        private final int depth;
        private final int range;

        public Scanner(int depth, int range) {
            this.depth = depth;
            this.range = range;
        }

        public boolean isActiveAt(int time) {
            int cycle = ((range - 1) * 2);
            return time % cycle == 0;
        }

        public static Scanner parse(String string) {
            String[] data = string.split(":\\s");
            return new Scanner(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
        }

        @Override
        public String toString() {
            return String.format("Depth: %d, Range: %d", depth, range);
        }

        public int getDepth() {
            return depth;
        }

        public int getSeverity() {
            return depth * range;
        }
    }

}
