package com.joskov.advent2017;

import java.util.ArrayList;
import java.util.List;

public class Day03 {
    public final static int input = 277678;

    public static void main(String[] args) {
        spiral1();
        spiral2();
    }

    public static void spiral1() {
        int distance = 1;
        Element element = new Element(new Position(0, 0), 1);

        while (true) {
            List<Movement> movements = new ArrayList<>();
            movements.add(new Movement(Direction.EAST, distance));
            movements.add(new Movement(Direction.NORTH, distance));
            movements.add(new Movement(Direction.WEST, distance + 1));
            movements.add(new Movement(Direction.SOUTH, distance + 1));
            for (Movement m : movements) {
                for (int j = 0; j < m.distance; j++) {
                    element = new Element(element.position.move(m.direction), element.n + 1);
                    if (element.n == input) {
                        System.out.println("Distance is " + element.getDistance());
                        return;
                    }

                }
            }
            distance += 2;
        }
    }

    public static void spiral2() {
        int distance = 1;
        Element element = new Element(new Position(0, 0), 1);
        List<Element> elements = new ArrayList<>();
        elements.add(element);

        while (true) {
            List<Movement> movements = new ArrayList<>();
            movements.add(new Movement(Direction.EAST, distance));
            movements.add(new Movement(Direction.NORTH, distance));
            movements.add(new Movement(Direction.WEST, distance + 1));
            movements.add(new Movement(Direction.SOUTH, distance + 1));
            for (Movement m : movements) {
                for (int j = 0; j < m.distance; j++) {
                    Position position = element.position.move(m.direction);
                    int n = findNeighborSum(elements, position);
                    element = new Element(position, n);
                    elements.add(element);
                    if (element.n > input) {
                        System.out.println("Element is " + element.n);
                        return;
                    }

                }
            }
            distance += 2;
        }
    }

    public static int findNeighborSum(List<Element> elements, Position position) {
        return elements.stream()
                .filter(a -> (Math.abs(a.position.x - position.x) <= 1) && (Math.abs(a.position.y - position.y) <= 1))
                .mapToInt(a -> a.n)
                .sum();
    }

    public enum Direction {
        EAST,
        NORTH,
        WEST,
        SOUTH
    }

    public static class Movement {
        private final Direction direction;
        private final int distance;

        public Movement(Direction direction, int distance) {
            this.direction = direction;
            this.distance = distance;
        }

        public String toString() {
            return String.format("Moving %s, distance %d", direction, distance);
        }
    }

    public static class Position {
        public final int x;
        public final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position move(Direction direction) {
            switch (direction) {
                case EAST:
                    return new Position(x + 1, y);
                case NORTH:
                    return new Position(x, y - 1);
                case WEST:
                    return new Position(x - 1, y);
                case SOUTH:
                    return new Position(x, y + 1);
            }
            return null;
        }
    }

    public static class Element {
        public Position position;
        public final int n;

        public Element(Position position, int n) {
            this.position = position;
            this.n = n;
        }

        public int getDistance() {
            return Math.abs(position.x) + Math.abs(position.y);
        }

        public String toString() {
            return String.format("X: %d, Y: %d, N: %d, Distance: %d", position.x, position.y, n, getDistance());
        }
    }


}
