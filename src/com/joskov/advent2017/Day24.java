package com.joskov.advent2017;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day24 extends Helpers {
    private final static String INPUT = "resources/24.txt";

    public static void main(String[] args) {
        List<Connection> input = readInput();
        partOne(input);
        partTwo(input);
    }

    private static void partOne(List<Connection> input) {
        int result = iterate(input, 0);
        System.out.println("Max strength is: " + result);
    }

    private static void partTwo(List<Connection> input) {
        BridgeStats result = maxLength(input, 0);
        System.out.println("The best bridge length: " + result.length);
        System.out.println("The best bridge strength: " + result.strength);
    }

    public static int iterate(List<Connection> left, int current) {
        List<Connection> connections = left.stream().filter(a -> a.canConnectTo(current)).collect(Collectors.toList());
        int max = 0;
        for (Connection connection : connections) {
            List<Connection> newLeft = new ArrayList<>(left);
            newLeft.remove(connection);
            int newCurrent = connection.other(current);
            int compare = iterate(newLeft, newCurrent) + connection.a + connection.b;
            if (compare > max) {
                max = compare;
            }
        }
        return max;
    }

    private static BridgeStats maxLength(List<Connection> left, int current) {
        List<Connection> connections = left.stream().filter(a -> a.canConnectTo(current)).collect(Collectors.toList());
        BridgeStats max = new BridgeStats(0, 0);
        for (Connection connection : connections) {
            List<Connection> newLeft = new ArrayList<>(left);
            newLeft.remove(connection);
            int newCurrent = connection.other(current);
            BridgeStats compare = maxLength(newLeft, newCurrent).add(1, connection.getStrength());
            if (compare.compareTo(max) > 0) {
                max = compare;
            }
        }
        return max;
    }

    public static List<Connection> readInput() {
        try {
            String[] strings = Helpers.read(INPUT).trim().split("[\\r\\n]+");
            return Arrays.stream(strings).map(Connection::parse).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    public static class Connection {
        private final int a;
        private final int b;

        public Connection(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public boolean canConnectTo(int pins) {
            return (a == pins) || (b == pins);
        }

        public int other(int pins) {
            if (a == pins) {
                return b;
            } else {
                return a;
            }
        }

        public static Connection parse(String string) {
            String[] parts = string.split("/");
            return new Connection(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        @Override
        public String toString() {
            return String.format("%d <-> %d", a, b);
        }

        public int getStrength() {
            return a + b;
        }
    }

    public static class BridgeStats implements Comparable<BridgeStats> {
        public final int length;
        public final int strength;

        public BridgeStats(int length, int strength) {
            this.length = length;
            this.strength = strength;
        }

        @Override
        public int compareTo(BridgeStats o) {
            int deltaLength = length - o.length;
            if (deltaLength != 0) {
                return deltaLength;
            } else {
                return strength - o.strength;
            }
        }

        public BridgeStats add(int length, int strength) {
            return new BridgeStats(this.length + length, this.strength + strength);
        }
    }
}
