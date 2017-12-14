package com.joskov.advent2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day12 extends Helpers {
    private final static String INPUT = "resources/12.txt";

    public static void main(String[] args) {
        Map<Integer, Program> programs = readInput();
        for (Program program : programs.values()) {
            program.connect(programs, program.id);
        }

        Map<Integer, Long> groups = programs.values().stream().collect(Collectors.groupingBy(Program::getGroup, Collectors.counting()));
        System.out.println("In group 0 count: " + groups.get(0));
        System.out.println("Total groups count: " + groups.size());
    }

    public static Map<Integer, Program> readInput() {
        try {
            String[] strings = Helpers.read(INPUT).trim().split("\\n|\\r");
            return Arrays.stream(strings).map(Program::parse).collect(Collectors.toMap(Program::getId, Function.identity()));
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    private static class Program {
        private final int id;
        private final List<Integer> connections;
        private Integer group;

        public Program(int id, List<Integer> connections) {
            this.id = id;
            this.connections = connections;
        }

        public static Program parse(String string) {
            String[] data = string.split("<->");
            List<Integer> connections = Arrays.stream(data[1].trim().split(", ")).map(Integer::parseInt).collect(Collectors.toList());
            return new Program(Integer.parseInt(data[0].trim()), connections);
        }

        @Override
        public String toString() {
            return String.format("ID: %d, Connections: %s, Group: %d", id, connections, group);
        }

        public void connect(Map<Integer, Program> programs, int newGroup) {
            if (this.group != null) {
                return;
            }

            this.group = newGroup;

            for (int connection : connections) {
                Program program = programs.get(connection);
                program.connect(programs, group);
            }
        }

        public int getId() {
            return id;
        }

        public int getGroup() {
            return group;
        }
    }

}
