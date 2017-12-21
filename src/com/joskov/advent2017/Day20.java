package com.joskov.advent2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day20 extends Helpers {
    private final static String INPUT = "resources/20.txt";

    public static void main(String[] args) {
        List<Particle> input = readInput();
        partOne(input);
        partTwo(input);
    }

    private static void partOne(List<Particle> input) {
        int minId = 0;
        Long minDistance = null;
        for (int j = 0; j < input.size(); j++) {
            Particle particle = input.get(j);
            for (int i = 0; i < 1_000; i++) {
                particle = particle.move();
            }
            if (minDistance == null || particle.distance() < minDistance) {
                minId = j;
                minDistance = particle.distance();
            }
        }

        System.out.println("Min distance id is: " + minId);
    }

    private static void partTwo(List<Particle> input) {
        List<Particle> newList = new ArrayList<>(input);

        for (int i = 0; i < 1_000; i++) {
            Map<Point, Long> groups = newList.stream().collect(Collectors.groupingBy(a -> a.position, Collectors.counting()));
            List<Point> colliding = groups.entrySet().stream().filter(a -> a.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
            newList = newList.stream().filter(a -> !colliding.contains(a.position)).map(Particle::move).collect(Collectors.toList());
        }

        System.out.println("Particles left: " + newList.size());
    }

    public static List<Particle> readInput() {
        try {
            String[] strings = Helpers.read(INPUT).trim().split("[\\r\\n]+");
            return Arrays.stream(strings).map(Particle::parse).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    private static class Particle {
        private final Point position;
        private final Point velocity;
        private final Point acceleration;

        public Particle(Point position, Point velocity, Point acceleration) {
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
        }

        public Particle move() {
            Point newVelocity = velocity.add(acceleration);
            Point newPosition = position.add(newVelocity);
            return new Particle(newPosition, newVelocity, acceleration);
        }

        public long distance() {
            return position.distance();
        }

        public static Particle parse(String particle) {
            particle = particle.replaceAll("[^0-9-,]", "");
            String[] values = particle.split(",");

            Point position = new Point(values[0], values[1], values[2]);
            Point velocity = new Point(values[3], values[4], values[5]);
            Point acceleration = new Point(values[6], values[7], values[8]);
            return new Particle(position, velocity, acceleration);
        }

        @Override
        public String toString() {
            return String.format("Position: %s, Speed: %s, Acceleration: %s.", position, velocity, acceleration);
        }
    }

    private static class Point {
        private final long x;
        private final long y;
        private final long z;

        public Point(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point(String x, String y, String z) {
            this(Long.parseLong(x), Long.parseLong(y), Long.parseLong(z));
        }

        public Point add(Point other) {
            return new Point(x + other.x, y + other.y, z + other.z);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Point) {
                Point point = (Point) obj;
                return (x == point.x) && (y == point.y) && (z == point.z);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return String.format("%d:%d:%d", x, y, z);
        }

        @Override
        public int hashCode() {
            return (int) (x + y * 31 + z * 31 * 31);
        }

        public long distance() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }
    }
}
