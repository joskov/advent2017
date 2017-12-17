package com.joskov.advent2017;

import java.util.ArrayList;
import java.util.List;

public class Day17 extends Helpers {
    private final static int INPUT = 316;
    private final static int ITERATIONS_1 = 2017;
    private final static int ITERATIONS_2 = 50_000_000;

    public static void main(String[] args) {
        System.out.println("Part one: " + partOne());
        System.out.println("Part two: " + partTwo());
    }

    private static int partOne() {
        List<Integer> result = new ArrayList<>();
        result.add(0);

        int index = 0;
        for (int i = 1; i <= ITERATIONS_1; i++) {
            index = (index + INPUT) % result.size() + 1;
            result.add(index, i);
        }
        return result.get(index + 1);

    }

    private static int partTwo() {
        int index = searchIndex(ITERATIONS_2);
        return searchValue(ITERATIONS_2, index);
    }

    private static int searchIndex(int iterations) {
        int size = 1;
        int index = 0;
        for (int i = 1; i <= iterations; i++) {
            index = (index + INPUT) % size + 1;
            size++;
        }
        return index;
    }

    private static int searchValue(int iterations, int lastIndex) {
        int index = lastIndex;
        for (int i = iterations; i > 0; i--) {
            index = (index - INPUT - 1) % i;
            index = index < 0 ? index + i : index;
            if (index == 1) {
                return i - 1;
            }
        }
        return -1;
    }
}
