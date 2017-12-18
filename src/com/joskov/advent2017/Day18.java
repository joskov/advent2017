package com.joskov.advent2017;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 extends Helpers {
    private final static String INPUT = "resources/18.txt";

    public static void main(String[] args) {
        List<Command> input = readInput();
        partOne(input);
//        partTwo(input);
    }

    private static void partOne(List<Command> input) {
        Application app = new Application(input);
        while (app.isIndexValid()) {
//            app.display();
            app.execute();
        }
    }

    private static void partTwo(List<Command> input) {
        Threaded first = new Threaded(input, 0);
        Threaded second = new Threaded(input, 0);
        while (first.isIndexValid() || second.isIndexValid()) {
            first.display();
            first.execute(second);
            second.display();
            second.execute(first);
        }
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
        protected long index;

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
    }

    public static class Threaded extends BaseApp {
        public Threaded(List<Command> commands, long id) {
            super(commands);
            set("p", id);
        }

        public void execute(Threaded other) {
            if (!isIndexValid()) {
                return;
            }

//            System.out.println("Index is: " + index);
            Command command = commands.get((int) index);
            apply(command, other);
        }

        public void apply(Command command, BaseApp other) {
//            System.out.printf("Execute %s with %s=%s, %s%n", command.type, command.a, get(command.a), get(command.b));
            switch (command.type) {
                case SND:
                    System.out.println("Sending value: " + get(command.a));
                    moveIndex(1);
                    break;
                case SET:
                    set(command.a, get(command.b));
                    moveIndex(1);
                    break;
                case ADD:
                    set(command.a, get(command.a) + get(command.b));
                    moveIndex(1);
                    break;
                case MULTIPLY:
                    set(command.a, get(command.a) * get(command.b));
                    moveIndex(1);
                    break;
                case MOD:
                    set(command.a, get(command.a) % get(command.b));
                    moveIndex(1);
                    break;
                case RCV:
                    System.out.println("Receive value: " + command.a);
                case JUMP:
                    if (get(command.a) > 0) {
                        moveIndex(get(command.b));
                    } else {
                        moveIndex(1);
                    }
                    break;
            }
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
    }

    public static class Application extends BaseApp {
        private Long sound;

        public Application(List<Command> commands) {
            super(commands);
        }

        public void execute() {
//            System.out.println("Index is: " + index);
            Command command = commands.get((int) index);
            apply(command);
        }

        public void apply(Command command) {
//            System.out.printf("Execute %s with %s=%s, %s%n", command.type, command.a, get(command.a), get(command.b));
            switch (command.type) {
                case SND:
                    setSound(get(command.a));
//                    System.out.println("Setting sound as: " + getSound());
                    moveIndex(1);
                    break;
                case SET:
                    set(command.a, get(command.b));
                    moveIndex(1);
                    break;
                case ADD:
                    set(command.a, get(command.a) + get(command.b));
                    moveIndex(1);
                    break;
                case MULTIPLY:
                    set(command.a, get(command.a) * get(command.b));
                    moveIndex(1);
                    break;
                case MOD:
                    set(command.a, get(command.a) % get(command.b));
                    moveIndex(1);
                    break;
                case RCV:
                    if (get(command.a) != 0) {
                        System.out.println("Sound was: " + getSound());
                        moveIndex(10000);
                    } else {
                        moveIndex(1);
                    }
                    break;
                case JUMP:
                    if (get(command.a) > 0) {
                        moveIndex(get(command.b));
                    } else {
                        moveIndex(1);
                    }
                    break;
            }
        }

        public void setSound(long sound) {
            this.sound = sound;
        }

        public long getSound() {
            return sound;
        }
    }

    public static class Command {
        private enum Type {
            SND("snd"),
            SET("set"),
            ADD("add"),
            MULTIPLY("mul"),
            MOD("mod"),
            RCV("rcv"),
            JUMP("jgz");

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
            String b = data.length == 3 ? data[2] : null;
            return new Command(type, a, b);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", type, a, b);
        }
    }
}
