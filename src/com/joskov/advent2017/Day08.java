package com.joskov.advent2017;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day08 extends Helpers {
    private final static String INPUT = "resources/08.txt";

    public static void main(String[] args) throws IOException {
        List<Command> commands = readInput();
        Map<String, Integer> registers = new HashMap<>();

        int max = Integer.MIN_VALUE;
        for (Command command : commands) {
            command.applyTo(registers);
            int currentMax = getMax(registers);
            if (currentMax > max) {
                max = currentMax;
            }
        }

        System.out.println("Max value in the end is is: " + getMax(registers));
        System.out.println("Total max value was: " + max);
    }

    public static int getMax(Map<String, Integer> registers) {
        return registers.entrySet().stream().mapToInt(a -> a.getValue()).max().getAsInt();
    }

    public static List<Command> readInput() throws IOException {
        String[] result = Helpers.read(INPUT).trim().split("\\r?\\n");
        return Arrays.stream(result).map(Command::parse).collect(Collectors.toList());
    }

    private static class Command {
        private final String var;
        private final int sign;
        private final int value;
        private final String check;
        private final Operation op;
        private final int compare;

        private Command(String var, int sign, int value, String check, Operation op, int compare) {
            this.var = var;
            this.sign = sign;
            this.value = value;
            this.check = check;
            this.op = op;
            this.compare = compare;
        }

        public static Command parse(String data) {
            String[] params = data.split("\\s");
            String var = params[0];
            int sign = params[1].equals("inc") ? 1 : -1;
            int value = Integer.parseInt(params[2]);
            // we skip params[3] because it's always "if"
            String check = params[4];
            Operation op = Operation.parse(params[5]);
            int compare = Integer.parseInt(params[6]);

            return new Command(var, sign, value, check, op, compare);
        }

        public String toString() {
            return String.format("[%s] + %d * %d if [%s] %s %s", var, sign, value, check, op, compare);
        }

        public void applyTo(Map<String, Integer> registers) {
            int oldValue = registers.getOrDefault(var, 0);
            int checkVal = registers.getOrDefault(check, 0);

            if (!op.check(checkVal, compare)) {
                return;
            }

            int newValue = oldValue + sign * value;

            registers.put(var, newValue);
        }
    }

    private enum Operation {
        EQUALS("=="),
        NOT_EQUALS("!="),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUALS(">="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUALS("<=");

        private final String op;

        Operation(String op) {
            this.op = op;
        }

        public boolean check(int a, int b) {
            switch (this) {
                case EQUALS:
                    return a == b;
                case NOT_EQUALS:
                    return a != b;
                case GREATER_THAN:
                    return a > b;
                case GREATER_THAN_OR_EQUALS:
                    return a >= b;
                case LESS_THAN:
                    return a < b;
                case LESS_THAN_OR_EQUALS:
                    return a <= b;
                default:
                    return false;
            }
        }

        public String toString() {
            return op;
        }

        public static Operation parse(String op) {
            for (Operation value : values()) {
                if (value.op.equals(op)) {
                    return value;
                }
            }
            return null;
        }
    }
}
