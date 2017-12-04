package com.joskov.advent2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day04 {
    public static final String INPUT = "resources/04.txt";

    public static void main(String[] args) throws IOException {
        List<List<String>> input = readWords();
        pass1(input);
        pass2(input);
    }

    public static void pass1(List<List<String>> input) {
        long result = input.stream().filter(Day04::checkWord1).count();
        System.out.println("Valid pass phrases: " + result);
    }

    private static boolean checkWord1(List<String> words) {
        long distinct = words.stream().distinct().count();
        return words.size() == distinct;
    }

    private static void pass2(List<List<String>> input) {
        long result = input.stream().filter(Day04::checkWord2).count();
        System.out.println("Valid pass phrases: " + result);
    }

    private static boolean checkWord2(List<String> words) {
        long distinct = words.stream().map(Day04::convertString).distinct().count();
        return words.size() == distinct;
    }

    private static String convertString(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public static List<List<String>> readWords() throws IOException {
        String input = Helpers.read(INPUT);
        String[] rows = input.split("\\r?\\n");
        return Arrays.stream(rows)
                .map(a -> Arrays.asList(a.split("\\s+")))
                .collect(Collectors.toList());
    }
}
