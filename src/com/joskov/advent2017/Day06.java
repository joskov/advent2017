package com.joskov.advent2017;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day06 extends Helpers {
    private final static String INPUT = "resources/06.txt";

    public static void main(String[] args) throws IOException {
        List<MemoryBank> input = readInput();
        partOne(input);
        partOne(input);
    }

    public static void partOne(List<MemoryBank> input) {
        int operations = 0;
        List<List<Integer>> history = new ArrayList<>();
        List<Integer> inputState = getState(input);
        while (!history.contains(inputState)) {
            history.add(inputState);
            operate(input);
            inputState = getState(input);
            operations++;
        }

        System.out.println("Operations: " + operations);
    }

    private static void operate(List<MemoryBank> input) {
        List<Integer> state = getState(input);
        int max = state.stream().mapToInt(Integer::intValue).max().getAsInt();
        int index = state.indexOf(max);
        int size = state.size();
        int memory = input.get(index).reset();

        while (memory > 0) {
            index = (index + 1) % size;
            input.get(index).increment();
            memory--;
        }
    }

    public static List<MemoryBank> readInput() throws IOException {
        String[] result = Helpers.read(INPUT).trim().split("\\s+");
        return Arrays.stream(result).map(MemoryBank::parse).collect(Collectors.toList());
    }

    public static List<Integer> getState(List<MemoryBank> input) {
        return input.stream().map(MemoryBank::getVal).collect(Collectors.toList());
    }

    public static class MemoryBank {
        private int val = 0;

        public MemoryBank(int val) {
            this.val = val;
        }

        public static MemoryBank parse(String s) {
           return new MemoryBank(Integer.parseInt(s));
        }

        public int reset() {
            int result = val;
            this.val = 0;
            return result;
        }

        public void increment() {
            this.val += 1;
        }

        public int getVal() {
            return val;
        }
    }
}
