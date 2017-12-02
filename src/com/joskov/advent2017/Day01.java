package com.joskov.advent2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 {
    public static final String INPUT = "resources/01.txt";

    public static void main(String[] args) throws IOException {
        String input = Helpers.read(INPUT);
        List<Integer> data = getData(input);

        int sum1 = 0;
        int sum2 = 0;
        int size = data.size();
        for (int i = 0; i < size; i++) {
            int n = data.get(i);
            if (n == data.get((i + 1) % size)) {
                sum1 += n;
            }
            if (n == data.get((i + size / 2) % size)) {
                sum2 += n;
            }
        }

        System.out.println("Sum 1 is " + sum1);
        System.out.println("Sum 2 is " + sum2);
    }

    public static List<Integer> getData(String input) {
        String[] elements = input.trim().split("");

        return Arrays.stream(elements).map(Integer::parseInt).collect(Collectors.toList());
    }
}
