package com.joskov.advent2017;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 extends Helpers {
    private final static String INPUT = "resources/09.txt";

    public static void main(String[] args) {
        String input = readInput();
        input = cleanup(input);
        calculateScore(input);
    }

    private static void calculateScore(String input) {
        String[] chars = input.split("(?!^)");
        int score = 0;
        int level = 0;
        for (int i = 0; i < input.length(); i++) {
            String ch = chars[i];
            if (ch.equals("{")) {
                level++;
            } else if (ch.equals("}")) {
                score += level;
                level--;
            }
        }
        System.out.println("The score is: " + score);
    }

    private static String cleanup(String input) {
        while (true) {
            String oldString = input;
            input = input.replaceFirst("!.", "");
            if (oldString.equals(input)) {
                break;
            }
        }

        Pattern garbage = Pattern.compile("<.*?>");
        Matcher matcher = garbage.matcher(input);
        int totalGarbage = 0;
        while (matcher.find()) {
            String garbageString = matcher.group();
            totalGarbage += garbageString.length() - 2;
        }
        System.out.println("Total garbage is: " + totalGarbage);

        input = input.replaceAll("<.*?>", "");
        return input;
    }

    public static String readInput() {
        try {
            return Helpers.read(INPUT).trim();
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return "";
        }
    }
}
