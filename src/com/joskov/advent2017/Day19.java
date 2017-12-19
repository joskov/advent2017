package com.joskov.advent2017;

import java.io.IOException;
import java.util.*;

public class Day19 extends Helpers {
    private final static String INPUT = "resources/19.txt";

    public static void main(String[] args) {
        List<Element> input = readInput();
        partOne(input);
    }

    private static void partOne(List<Element> input) {
        Element last = first(input);
        Element current = find(input, last.x, last.y + 1);
        int steps = 1;
        String result = "";
        while (current != null) {
            steps++;
            if (current.type == Element.Type.LETTER) {
                result += current.value;
            }
            Element next = next(input, current, last);
            last = current;
            current = next;
        }
        System.out.println("Letters are: " + result);
        System.out.println("Steps required: " + steps);
    }

    private static Element first(List<Element> input) {
        return input.stream().filter(a -> a.y == 0).findAny().get();
    }

    private static Element find(List<Element> input, int x, int y) {
        return input.stream().filter(a -> a.x == x && a.y == y).findFirst().orElse(null);
    }

    public static Element next(List<Element> input, Element current, Element last) {
        int dx = current.x - last.x;
        int dy = current.y - last.y;
        Element test;

        switch (current.type) {
            case CORNER:
                if (dx == 0) {
                    test = find(input, current.x + 1, current.y);
                    if (test != null) {
                        return test;
                    } else {
                        return find(input, current.x - 1, current.y);
                    }
                } else {
                    test = find(input, current.x, current.y + 1);
                    if (test != null) {
                        return test;
                    } else {
                        return find(input, current.x, current.y - 1);
                    }
                }
            case HORIZONTAL:
            case VERTICAL:
            case LETTER:
                return find(input, current.x + dx, current.y + dy);
        }
        return null;
    }

    public static List<Element> readInput() {
        try {
            List<Element> result = new ArrayList<>();
            String[] strings = Helpers.read(INPUT).split("[\\r\\n]+");
            for (int i = 0; i < strings.length; i++) {
                String[] elements = strings[i].split("(?!^)");
                for (int j = 0; j < elements.length; j++) {
                    if (elements[j].equals(" ")) {
                        continue;
                    }
                    result.add(new Element(elements[j], j, i));
                }
            }
            return result;
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    public static class Element {
        private enum Type {VERTICAL, HORIZONTAL, CORNER, LETTER}

        private final Type type;
        private final String value;
        private final int x;
        private final int y;

        public Element(String value, int x, int y) {
            this.value = value;
            this.x = x;
            this.y = y;

            switch (value) {
                case "|":
                    this.type = Type.VERTICAL;
                    break;
                case "-":
                    this.type = Type.HORIZONTAL;
                    break;
                case "+":
                    this.type = Type.CORNER;
                    break;
                default:
                    this.type = Type.LETTER;
            }
        }

        @Override
        public String toString() {
            return String.format("%s [%s] %d:%d", type, value, x, y);
        }
    }
}
