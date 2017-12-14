package com.joskov.advent2017;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Day14 extends Helpers {
    private final static String INPUT = "jxqlasbh";
    // private final static String INPUT = "flqrgnkx";

    public static void main(String[] args) {
        Set<Square> squares = calculateSquares();
        System.out.println("Count is: " + squares.size());

        int groups = calculateGroupsCount(squares);
        System.out.println("Groups count is: " + groups);
    }

    public static int calculateGroupsCount(Set<Square> squares) {
        int groups = 0;
        while (squares.size() > 0) {
            Set<Square> check = new HashSet<>();
            Set<Square> group = new HashSet<>();

            Square first = squares.iterator().next();
            squares.remove(first);
            group.add(first);
            check.addAll(first.neighbors());

            while (true) {
                Set<Square> found = check.stream().filter(a -> squares.contains(a)).collect(Collectors.toSet());
                squares.removeAll(found);
                check = found.stream().flatMap(a -> a.neighbors().stream()).collect(Collectors.toSet());
                group.addAll(found);

                if (found.size() == 0) {
                    break;
                }
            }

            groups += 1;
        }
        return groups;
    }

    public static Set<Square> calculateSquares() {
        Set<Square> result = new HashSet<>();
        for (int i = 0; i < 128; i++) {
            String input = INPUT + "-" + i;
            String hash = Day10.hash(input);
            BigInteger row = new BigInteger(hash, 16);
            String binary = row.toString(2);
            char[] chars = binary.toCharArray();

            int padLeft = 128 - chars.length;
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '1') {
                    result.add(new Square(j + padLeft, i));
                }
            }
        }
        return result;
    }

    public static class Square {
        public final int x;
        public final int y;

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() == Square.class) {
                Square square = (Square) obj;
                return (x == square.x) && (y == square.y);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return x * 31 + y;
        }

        public Square(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Set<Square> neighbors() {
            Set<Square> result = new HashSet<>();
            result.add(new Square(x - 1, y));
            result.add(new Square(x + 1, y));
            result.add(new Square(x, y - 1));
            result.add(new Square(x, y + 1));
            return result;
        }
    }
}
