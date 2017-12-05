package com.joskov.advent2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 {
    public static final String INPUT = "resources/05.txt";

    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput();
        pass1(input);
        pass2(input);
    }

    private static List<Integer> readInput() throws IOException {
        String input = Helpers.read(INPUT);
        String[] rows = input.trim().split("\\r?\\n");
        return Arrays.stream(rows)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static void pass1(List<Integer> input) {
        input = new ArrayList<>(input);
        int size = input.size();
        int index = 0;
        int steps = 0;
        while (index < size) {
            int oldIndex = index;
            index = index + input.get(index);
            input.set(oldIndex, input.get(oldIndex) + 1);
            steps++;
        }
        System.out.println("Steps required: " + steps);
    }

    public static void pass2(List<Integer> input) {
        input = new ArrayList<>(input);
        int size = input.size();
        int index = 0;
        int steps = 0;
        while (index < size) {
            int oldIndex = index;
            index = index + input.get(index);
            updateIndex(input, oldIndex);
            steps++;
        }
        System.out.println("Steps required: " + steps);
    }

    public static void updateIndex(List<Integer> input, int index) {
        int oldValue = input.get(index);
        int delta = (oldValue >= 3 ? -1 : 1);
        input.set(index, oldValue + delta);
    }
}
