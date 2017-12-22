package com.joskov.advent2017;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day21 extends Helpers {
    private final static String INPUT = "resources/21.txt";
    private final static String START = ".#./..#/###";

    public static void main(String[] args) {
        List<Enhancement> input = readInput();
        execute(input, 5);
        execute(input, 18);
    }

    public static List<Enhancement> readInput() {
        try {
            String[] strings = Helpers.read(INPUT).split("[\\r\\n]+");
            return Arrays.stream(strings).map(Enhancement::parse).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    private static void execute(List<Enhancement> input, int iterations) {
        Image image = Image.parse(START);
        image = multipleTransform(input, image, iterations);
        System.out.printf("Number of pixels for %d iterations: %s%n", iterations, image.getPixels().size());
    }

    private static Image multipleTransform(List<Enhancement> input, Image image, int iterations) {
        for (int i = 0; i < iterations; i++) {
            image = transformImage(input, image);
            System.out.printf("Iteration %d has %s pixels.%n", i, image.getPixels().size());
        }
        return image;
    }

    private static Image enhance(List<Enhancement> input, Image image) {
        return input.stream().filter(a -> a.getLeft().contains(image)).map(Enhancement::getRight).findFirst().get();
    }

    public static Image transformImage(List<Enhancement> enhancements, Image left) {
        int splitSize = (left.getSize() % 2 == 0) ? 2 : 3;
        int destinationSize = (splitSize == 2) ? 3 : 4;
        int splits = left.getSize() / splitSize;

        Set<Pixel> newPixels = new HashSet<>();
        for (int i = 0; i < splits; i++) {
            for (int j = 0; j < splits; j++) {
                final int sourceX = i * splitSize;
                final int sourceY = j * splitSize;

                Image sourceImage = new Image(splitSize, left, sourceX, sourceY);

                final int destinationX = i * destinationSize;
                final int destinationY = j * destinationSize;

                Image destinationImage = enhance(enhancements, sourceImage);
                Set<Pixel> destinationPixels = destinationImage.getPixels().stream()
                        .map(a -> a.move(destinationX, destinationY))
                        .collect(Collectors.toSet());
                newPixels.addAll(destinationPixels);
            }
        }
        return new Image(destinationSize * splits, newPixels);
    }

    private static class Enhancement {
        private final Set<Image> left;
        private final Image right;

        public Enhancement(Set<Image> left, Image right) {
            this.left = left;
            this.right = right;
        }

        public static Enhancement parse(String string) {
            String[] data = string.split(" => ");
            Image left = Image.parse(data[0]);
            Image right = Image.parse(data[1]);
            return new Enhancement(left.transformations(), right);
        }

        public Set<Image> getLeft() {
            return left;
        }

        public Image getRight() {
            return right;
        }
    }

    private static class Image {
        private final int size;
        private final Set<Pixel> pixels;

        public Image(int size, Set<Pixel> pixels) {
            this.size = size;
            this.pixels = pixels;
            for (Pixel pixel : pixels) {
                if (pixel.isOutOfBounds(size)) {
                    throw new RuntimeException(String.format("Pixel %s out of bound of %d", pixel, size));
                }
            }
        }

        public Image(int size, Image source, int startX, int startY) {
            this(size, source.windowPixels(startX, startY, size));
        }

        public Set<Pixel> windowPixels(int startX, int startY, int size) {
            Set<Pixel> result = new HashSet<>();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (getPixels().contains(new Pixel(i + startX, j + startY))) {
                        result.add(new Pixel(i, j));
                    }
                }
            }
            return result;
        }

        public static Image parse(String string) {
            String[] rows = string.split("/");
            Set<Pixel> pixels = new HashSet<>();
            int size = rows.length;
            for (int i = 0; i < size; i++) {
                char[] chars = rows[i].toCharArray();
                for (int j = 0; j < size; j++) {
                    if (chars[j] == '#') {
                        pixels.add(new Pixel(j, i));
                    }
                }
            }
            return new Image(size, pixels);
        }

        public Set<Image> transformations() {
            Set<Image> result = new HashSet<>();
            Image current = this;
            for (int i = 0; i < 4; i++) {
                if (i > 0) {
                    current = current.rotate();
                }
                result.add(current);
                result.add(current.mirror());
            }
            return result;
        }

        public Image mirror() {
            return new Image(size, pixels.stream().map(a -> a.mirror(size)).collect(Collectors.toSet()));
        }

        public Image rotate() {
            return new Image(size, pixels.stream().map(a -> a.rotate(size)).collect(Collectors.toSet()));
        }

        @Override
        public String toString() {
            return imageString();
        }

        public String imageString() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    result.append(pixels.contains(new Pixel(j, i)) ? "#" : ".");
                }
                result.append("\n");
            }
            return result.toString();
        }

        public String listPixels() {
            return String.format("[%d] %s", size, pixels);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Image) {
                Image image = (Image) obj;
                return (image.size == size) && (image.pixels.equals(pixels));
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return pixels.hashCode() + size * 31;
        }

        public int getSize() {
            return size;
        }

        public Set<Pixel> getPixels() {
            return pixels;
        }
    }

    private static class Pixel {
        private final int x;
        private final int y;

        private Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return String.format("%d:%d", x, y);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Pixel) {
                Pixel pixel = (Pixel) obj;
                return (pixel.x == x) && (pixel.y == y);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return x + 31 * y;
        }

        public Pixel mirror(int size) {
            return new Pixel(size - 1 - x, y);
        }

        public Pixel rotate(int size) {
            return new Pixel(size - 1 - y, x);
        }

        public Pixel move(int x, int y) {
            return new Pixel(this.x + x, this.y + y);
        }

        public boolean isOutOfBounds(int size) {
            return x < 0 || y < 0 || x >= size || y >= size;
        }
    }

}
