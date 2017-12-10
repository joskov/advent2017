package com.joskov.advent2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 extends Helpers {
    private final static String INPUT = "resources/10.txt";
    private final static int MAX_VALUE = 255;

    public static void main(String[] args) {
        List<Integer> numbers = getNumbers();
        List<Integer> input = readInput();
        hashNumbers(numbers, input, 1);
        System.out.println("First two numbers multiplied: " + numbers.get(0) * numbers.get(1));

        numbers = getNumbers();
        input = readInputAdditional();
        hashNumbers(numbers, input, 64);
        String hash = compactHash(numbers);
        System.out.println("The complete hash is: " + hash);
    }

    public static List<Integer> getNumbers() {
        return IntStream.rangeClosed(0, MAX_VALUE).boxed().collect(Collectors.toList());
    }

    private static void hashNumbers(List<Integer> numbers, List<Integer> input, int passes) {
        int currentPosition = 0;
        int skipSize = 0;
        int numbersSize = numbers.size();
        for (int i = 0; i < passes; i++) {
            for (int length : input) {
                swapRange(numbers, length, currentPosition);
                currentPosition = (currentPosition + length + skipSize) % numbersSize;
                skipSize++;
            }
        }
    }

    private static void swapRange(List<Integer> numbers, int length, int currentPosition) {
        for (int i = 0; i < length / 2; i++) {
            swap(numbers, currentPosition + i, currentPosition + length - i - 1);
        }
    }

    private static void swap(List<Integer> numbers, int a, int b) {
        a = a % numbers.size();
        b = b % numbers.size();
        Integer store = numbers.get(a);
        numbers.set(a, numbers.get(b));
        numbers.set(b, store);
    }

    public static List<Integer> readInput() {
        try {
            String[] strings = Helpers.read(INPUT).trim().split(",\\s?");
            return Arrays.stream(strings).map(Integer::parseInt).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    public static List<Integer> readInputAdditional() {
        try {
            char[] chars = Helpers.read(INPUT).trim().toCharArray();
            List<Integer> result = new ArrayList<>();
            for (char c : chars) {
                result.add((int) c);
            }
            result.add(17);
            result.add(31);
            result.add(73);
            result.add(47);
            result.add(23);
            return result;
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    private static String compactHash(List<Integer> numbers) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int hex = 0;
            for (int j = 0; j < 16; j++) {
                int number = numbers.get(i * 16 + j);
                hex ^= number;
            }
            result.append(String.format("%02x", hex));
        }
        return result.toString();
    }

}
