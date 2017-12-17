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
        int size = 1;
        int index = 0;
        int result = -1;
        for (int i = 1; i <= ITERATIONS_2; i++) {
            index = (index + INPUT) % size + 1;
            if (index == 1) {
                result = i;
            }
            size++;
        }
        return result;
    }
}
