package com.joskov.advent2017;

public class Day15 extends Helpers {
    private final static int INPUT_A = 116;
    private final static int INPUT_B = 299;

//    private final static long INPUT_A = 65;
//    private final static long INPUT_B = 8921;

    public static void main(String[] args) {
        Pair pair = new Pair(INPUT_A, INPUT_B);
        System.out.println("Count of matching pairs: " + count(pair, 40_000_000, false));
        System.out.println("Count of matching pairs: " + count(pair, 5_000_000, true));
    }

    public static int count(Pair pair, int iterations, boolean advanced) {
        int count = 0;
        for (int i = 0; i < iterations; i++) {
            pair = pair.nextPair(advanced);
            if (pair.matches()) {
                count++;
            }
        }
        return count;
    }

    public static class Pair {
        private final static long MULTIPLIER_A = 16807;
        private final static long MULTIPLIER_B = 48271;

        private final static long VALUE_MODULUS = 2147483647;

        private final static long MATCH_CHECK = Long.parseLong("1111111111111111", 2);

        public final long a;
        public final long b;

        public Pair(long a, long b) {
            this.a = a;
            this.b = b;
        }

        public Pair nextPair(boolean advanced) {
            long a = nextValue(this.a, MULTIPLIER_A, advanced, 4);
            long b = nextValue(this.b, MULTIPLIER_B, advanced, 8);
            return new Pair(a, b);
        }

        public static long nextValue(long value, long multiplier, boolean advanced, int check) {
            while (true) {
                value = (value * multiplier) % VALUE_MODULUS;
                if (!advanced || value % check == 0) {
                    break;
                }
            }
            return value;
        }

        public boolean matches() {
            return (a & MATCH_CHECK) == (b & MATCH_CHECK);
        }

        @Override
        public String toString() {
            return String.format("[ %5s ] %12d <-> %12d", matches(), a, b);
        }
    }
}
