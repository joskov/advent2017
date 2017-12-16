package com.joskov.advent2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 extends Helpers {
    private final static String INPUT = "resources/16.txt";
    private final static String START = "abcdefghijklmnop";

    public static void main(String[] args) {
        List<Move> input = readInput();
        System.out.println("String is now: " + partOne(input, START));
        System.out.println("String is now: " + partTwo(input, START));
    }

    public static String partOne(List<Move> input, String string) {
        for (Move move : input) {
            string = move.applyTo(string);
        }
        return string;
    }

    public static String partTwo(List<Move> input, String string) {
        int repeat = 0;
        while (true) {
            repeat++;
            string = partOne(input, string);
            if (string.equals(START)) {
                break;
            }
        }

        int remainder = 1_000_000_000 % repeat;
        for (int i = 0; i < remainder; i++) {
            string = partOne(input, string);
        }
        return string;
    }

    public static List<Move> readInput() {
        try {
            String[] strings = Helpers.read(INPUT).trim().split(",");
            return Arrays.stream(strings).map(Move::parse).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    private abstract static class Move {
        public static Move parse(String string) {
            String type = string.substring(0, 1);
            String[] data = string.substring(1).split("/");
            switch (type) {
                case "s":
                    return new Spin(Integer.parseInt(data[0]));
                case "x":
                    return new Exchange(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
                case "p":
                    return new Partner(data[0].charAt(0), data[1].charAt(0));
            }
            return null;
        }

        abstract public String applyTo(String string);
    }

    private static class Spin extends Move {
        private final int a;

        public Spin(int a) {
            this.a = a;
        }

        public String applyTo(String string) {
            int cutPoint = string.length() - a;
            return string.substring(cutPoint) + string.substring(0, cutPoint);
        }
    }

    private static class Exchange extends Move {
        private final int a;
        private final int b;

        public Exchange(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public String applyTo(String string) {
            return new Partner(string.charAt(a), string.charAt(b)).applyTo(string);
        }
    }

    private static class Partner extends Move {
        private final char a;
        private final char b;

        public Partner(char a, char b) {
            this.a = a;
            this.b = b;
        }

        public String applyTo(String string) {
            string = string.replace(a, '_');
            string = string.replace(b, a);
            string = string.replace('_', b);
            return string;
        }
    }


}
