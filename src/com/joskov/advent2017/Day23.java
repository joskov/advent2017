package com.joskov.advent2017;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day23 extends Helpers {
    private final static String INPUT = "resources/23.txt";

    public static void main(String[] args) {
        List<Command> input = readInput();
        partOne(input);
        partTwo(input);
    }

    private static void partOne(List<Command> input) {
        Application app = new Application(input);
        while (app.isIndexValid()) {
            app.execute();
        }
        System.out.println("Multiply was used: " + app.getMultiplyUsed());
    }

    private static void partTwo(List<Command> input) {
        int rangeFrom = Integer.parseInt(input.get(0).b);
        rangeFrom *= Integer.parseInt(input.get(4).b);
        rangeFrom -= Integer.parseInt(input.get(5).b);
        int rangeTo = rangeFrom - Integer.parseInt(input.get(7).b);
        int count = 0;
        for (int i = rangeFrom; i <= rangeTo; i += 17) {
            if (!isPrime(i)) {
                count++;
            }
        }
        System.out.printf("Non primes count from %d to %d: %d%n", rangeFrom, rangeTo, count);
    }

    private static boolean isPrime(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static List<Command> readInput() {
        try {
            String[] strings = Helpers.read(INPUT).trim().split("[\\r\\n]+");
            return Arrays.stream(strings).map(Command::parse).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    public static abstract class BaseApp {
        protected Map<String, Long> registers;
        protected List<Command> commands;

        protected int index;

        public BaseApp(List<Command> commands) {
            this.commands = commands;
            registers = new HashMap<>();
            index = 0;
        }

        public boolean isIndexValid() {
            return (index >= 0) && (index < commands.size());
        }

        public long get(String register) {
            if (register != null && register.matches("-?\\d+")) {
                return Integer.parseInt(register);
            } else {
                return registers.getOrDefault(register, 0l);
            }
        }

        public void set(String register, long value) {
            registers.put(register, value);
        }

        public void moveIndex(long delta) {
            index += delta;
        }

        public void display() {
            registers.entrySet().forEach(a -> System.out.printf("%s:%d ", a.getKey(), a.getValue()));
            System.out.println();
        }

        public Command getCurrentCommand() {
            return commands.get(index);
        }

        public int getIndex() {
            return index;
        }
    }

    public static class Application extends BaseApp {
        private int multiplyUsed = 0;

        public Application(List<Command> commands) {
            super(commands);
        }

        public void execute() {
            apply(getCurrentCommand());
        }

        public void apply(Command command) {
            switch (command.type) {
                case SET:
                    set(command.a, get(command.b));
                    moveIndex(1);
                    break;
                case SUBTRACT:
                    set(command.a, get(command.a) - get(command.b));
                    moveIndex(1);
                    break;
                case MULTIPLY:
                    set(command.a, get(command.a) * get(command.b));
                    moveIndex(1);
                    multiplyUsed++;
                    break;
                case JUMP:
                    if (get(command.a) == 0) {
                        moveIndex(1);
                    } else {
                        moveIndex(get(command.b));
                    }
                    break;
            }
        }

        public int getMultiplyUsed() {
            return multiplyUsed;
        }
    }

    public static class Command {
        private enum Type {
            SET("set"),
            SUBTRACT("sub"),
            MULTIPLY("mul"),
            JUMP("jnz");

            private final String value;

            Type(String value) {
                this.value = value;
            }

            public static Type fromString(String value) {
                for (Type type : values()) {
                    if (type.value.equals(value)) {
                        return type;
                    }
                }
                return null;
            }
        }

        private final Type type;
        private final String a;
        private final String b;

        public Command(Type type, String a, String b) {
            this.type = type;
            this.a = a;
            this.b = b;
        }

        public static Command parse(String string) {
            String[] data = string.split("\\s");
            Type type = Type.fromString(data[0]);
            String a = data[1];
            String b = data.length > 2 ? data[2] : null;
            return new Command(type, a, b);
        }

        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", type, a, b);
        }
    }
}
