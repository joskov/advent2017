package com.joskov.advent2017;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Day25 extends Helpers {
    private final static String INPUT = "resources/25.txt";

    private final static Pattern STARTING_STATE = Pattern.compile("Begin in state (\\w)\\.");
    private final static Pattern ITERATION = Pattern.compile("Perform a diagnostic checksum after (\\d+) steps\\.");
    private final static Pattern IN_STATE = Pattern.compile("In state (\\w):");
    private final static Pattern ACTION_VALUE = Pattern.compile("If the current value is (\\d):");
    private final static Pattern ACTION_WRITE = Pattern.compile("Write the value (\\d)\\.");
    private final static Pattern ACTION_MOVE = Pattern.compile("Move one slot to the (\\w+)\\.");
    private final static Pattern ACTION_NEXT = Pattern.compile("Continue with state (\\w)\\.");

    public static void main(String[] args) {
        Blueprint input = readInput();
        partOne(input);
    }

    private static void partOne(Blueprint input) {
        Memory memory = new Memory(input.getStart());
        for (int i = 0; i < input.getSteps(); i++) {
            memory.execute(input.getStates());
        }
        System.out.println("Checksum is: " + memory.checksum());
    }

    public static Blueprint readInput() {
        try {
            String[] strings = Helpers.read(INPUT).trim().split("\\r|\\n");
            String start = Helpers.match(STARTING_STATE, strings[0]).get(0);
            String iterations = Helpers.match(ITERATION, strings[1]).get(0);

            int row = 3;
            Map<String, State> states = new HashMap<>();
            while (row < strings.length) {
                String stateName = Helpers.match(IN_STATE, strings[row++]).get(0);

                HashMap<Integer, Action> actions = new HashMap<>();
                for (int i = 0; i < 2; i++) {
                    String value = Helpers.match(ACTION_VALUE, strings[row++]).get(0);
                    String write = Helpers.match(ACTION_WRITE, strings[row++]).get(0);
                    String move = Helpers.match(ACTION_MOVE, strings[row++]).get(0);
                    String next = Helpers.match(ACTION_NEXT, strings[row++]).get(0);
                    Action action = new Action(value, write, move, next);
                    actions.put(action.getInput(), action);
                }

                State state = new State(stateName, actions);
                states.put(state.getName(), state);
                row++;
            }

            return new Blueprint(start, Integer.parseInt(iterations), states);
        } catch (IOException e) {
            System.out.println("Cannot load input file.");
            return null;
        }
    }

    private static class Memory {
        private final Map<Integer, Integer> memory = new HashMap<>();
        private int position = 0;
        private String state;

        private Memory(String start) {
            this.state = start;
        }

        private void execute(Map<String, State> states) {
            State currentState = states.get(state);
            Action action = currentState.getAction(getValue());
            setValue(action.getValue());
            this.position += action.getMove();
            this.state = action.getNext();
        }

        public int getValue() {
            return memory.getOrDefault(position, 0);
        }

        public void setValue(int value) {
            if (value == 0) {
                memory.remove(position);
            } else {
                memory.put(position, value);
            }
        }

        public int checksum() {
            return memory.size();
        }
    }

    private static class Blueprint {
        private final String start;
        private final int steps;
        private final Map<String, State> states;

        private Blueprint(String start, int steps, Map<String, State> states) {
            this.start = start;
            this.steps = steps;
            this.states = states;
        }

        public int getSteps() {
            return steps;
        }

        public String getStart() {
            return start;
        }

        public Map<String, State> getStates() {
            return states;
        }
    }

    private static class State {
        private final String name;
        private final Map<Integer, Action> actions;

        private State(String name, Map<Integer, Action> actions) {
            this.name = name;
            this.actions = actions;
        }

        public String getName() {
            return name;
        }

        public Action getAction(int value) {
            return actions.get(value);
        }
    }

    private static class Action {
        private final int input;
        private final int value;
        private final int move;
        private final String next;

        private Action(int input, int value, int move, String next) {
            this.input = input;
            this.value = value;
            this.move = move;
            this.next = next;
        }

        public Action(String value, String write, String move, String next) {
            this(Integer.parseInt(value), Integer.parseInt(write), move.equals("right") ? 1 : -1, next);
        }

        @Override
        public String toString() {
            return String.format("(%d) -> (%d), move: %d, next: %s", input, value, move, next);
        }

        public int getInput() {
            return input;
        }

        public int getMove() {
            return move;
        }

        public int getValue() {
            return value;
        }

        public String getNext() {
            return next;
        }
    }
}
