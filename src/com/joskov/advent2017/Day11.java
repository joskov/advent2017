package com.joskov.advent2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 extends Helpers {
    private final static String INPUT = "resources/11.txt";

    public static void main(String[] args) throws IOException {
        List<Move> moves = readInput();
        final Position position = new Position();
        moves.forEach(move -> position.apply(move));
        System.out.println("Distance is: " + position.distance());

        Position newPosition = new Position();
        int maxDistance = 0;
        for (Move move : moves) {
            newPosition.apply(move);
            maxDistance = Math.max(maxDistance, newPosition.distance());
        }

        System.out.println("Max distance was: " + maxDistance);
    }

    public static List<Move> readInput() throws IOException {
        String[] result = Helpers.read(INPUT).trim().split(",");
        return Arrays.stream(result).map(Move::parse).collect(Collectors.toList());
    }

    private static class Position {
        int x = 0;
        int y = 0;

        public void apply(Move move) {
            if (move == null) {
                System.out.println("Wrong movement!");
            }
            switch (move) {
                case N:
                    y -= 2;
                    break;
                case S:
                    y += 2;
                    break;
                case NE:
                    x += 1;
                    y -= 1;
                    break;
                case NW:
                    x -= 1;
                    y -= 1;
                    break;
                case SE:
                    x += 1;
                    y += 1;
                    break;
                case SW:
                    x -= 1;
                    y += 1;
                    break;
            }
        }

        public int distance() {
            int absX = Math.abs(x);
            int absY = Math.abs(y);
            if (absY > absX) {
                return (absY - absX) / 2 + absX;
            } else {
                return absX;
            }
        }

        public String toString() {
            return String.format("X: %d, Y: %d", x, y);
        }
    }

    private enum Move {
        N("n"),
        S("s"),
        NE("ne"),
        NW("nw"),
        SE("se"),
        SW("sw");

        private final String direction;

        Move(String direction) {
            this.direction = direction;
        }

        public String toString() {
            return direction;
        }

        public static Move parse(String direction) {
            for (Move value : values()) {
                if (value.direction.equals(direction)) {
                    return value;
                }
            }
            return null;
        }
    }
}
