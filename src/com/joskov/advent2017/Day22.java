package com.joskov.advent2017;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.joskov.advent2017.Day21.*;

public class Day22 extends Helpers {
    private final static String INPUT = "resources/22.txt";

    public static void main(String[] args) {
        Image image = readInput();
        partOne(image);
        partTwo(image);
    }

    private static void partOne(Image image) {
        int middle = (image.getSize() - 1) / 2;
        Pixel currentPosition = new Pixel(middle, middle);
        bursts(image.getPixels(), currentPosition, 10000);
    }

    private static void partTwo(Image image) {
        int middle = (image.getSize() - 1) / 2;
        Pixel currentPosition = new Pixel(middle, middle);
        advanced(image.getPixels(), currentPosition, 10_000_000);
    }

    public static void bursts(Set<Pixel> pixels, Pixel position, int iterations) {
        int infections = 0;
        Set<Pixel> result = new HashSet<>(pixels);
        Direction direction = Direction.NORTH;
        for (int i = 0; i < iterations; i++) {
            if (result.contains(position)) {
                direction = direction.right();
                result.remove(position);
            } else {
                direction = direction.left();
                result.add(position);
                infections++;
            }
            position = move(position, direction);
        }
        System.out.println("Number of infections: " + infections);
    }

    public static void advanced(Set<Pixel> pixels, Pixel position, int iterations) {
        int infections = 0;
        Map<Pixel, State> result = pixels.stream().collect(Collectors.toMap(Function.identity(), a -> State.INFECTED));
        Direction direction = Direction.NORTH;
        for (int i = 0; i < iterations; i++) {
            State state = result.getOrDefault(position, State.CLEAN);
            switch (state) {
                case CLEAN:
                    direction = direction.left();
                    result.put(position, State.WEAKENED);
                    break;
                case WEAKENED:
                    result.put(position, State.INFECTED);
                    infections++;
                    break;
                case INFECTED:
                    direction = direction.right();
                    result.put(position, State.FLAGGED);
                    break;
                case FLAGGED:
                    direction = direction.right().right();
                    result.remove(position);
                    break;
            }
            position = move(position, direction);
        }
        System.out.println("Number of infections: " + infections);
    }

    public static Image makeImage(Map<Pixel, State> pixelStateMap) {
        Set<Pixel> pixels = pixelStateMap.entrySet().stream().filter(a -> a.getValue() == State.INFECTED).map(Map.Entry::getKey).collect(Collectors.toSet());
        return makeImage(pixels);
    }

    public static Image makeImage(Set<Pixel> pixels) {
        final int border = 1;
        int xMin = pixels.stream().mapToInt(Pixel::getX).min().getAsInt();
        int yMin = pixels.stream().mapToInt(Pixel::getY).min().getAsInt();
        pixels = pixels.stream().map(a -> a.move(-xMin + border, -yMin + border)).collect(Collectors.toSet());
        int width = pixels.stream().mapToInt(Pixel::getX).max().getAsInt() + 1 + border;
        int height = pixels.stream().mapToInt(Pixel::getY).max().getAsInt() + 1 + border;
        return new Image(Math.max(width, height), pixels);
    }

    public static Pixel move(Pixel pixel, Direction direction) {
        return new Pixel(pixel.getX() + direction.x, pixel.getY() + direction.y);
    }

    public static Image readInput() {
        try {
            String[] strings = Helpers.read(INPUT).split("[\\r\\n]+");
            int size = strings.length;
            Set<Pixel> pixels = new HashSet<>();
            for (int i = 0; i < strings.length; i++) {
                char[] chars = strings[i].toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    if (chars[j] == '#') {
                        pixels.add(new Pixel(j, i));
                    }
                }
            }
            return new Image(size, pixels);
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    public enum State { CLEAN, WEAKENED, INFECTED, FLAGGED }

    public enum Direction {
        NORTH(0, -1),
        EAST(1, 0),
        SOUTH(0, 1),
        WEST(-1, 0);

        public final int y;
        public final int x;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Direction left() {
            switch (this) {
                case EAST:
                    return NORTH;
                case NORTH:
                    return WEST;
                case WEST:
                    return SOUTH;
                case SOUTH:
                    return EAST;
                default:
                    return null;
            }
        }

        public Direction right() {
            switch (this) {
                case EAST:
                    return SOUTH;
                case SOUTH:
                    return WEST;
                case WEST:
                    return NORTH;
                case NORTH:
                    return EAST;
                default:
                    return null;
            }
        }
    }

}
