package com.joskov.advent2017;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day07 extends Helpers {
    private final static String INPUT = "resources/07.txt";

    public static void main(String[] args) throws IOException {
        Map<String, Tower> towers = readInput();
        findRoot(towers);
        findImbalanced(towers);
    }

    public static void findRoot(Map<String, Tower> towers) {
        Map.Entry<String, Tower> entity = towers.entrySet().stream().findFirst().get();
        String name = entity.getValue().getName();
        while (true) {
            final String search = name;
            Optional<Map.Entry<String, Tower>> found = towers.entrySet().stream().filter(a -> a.getValue().getConnections().contains(search)).findAny();
            if (found.isPresent()) {
                name = found.get().getKey();
            } else {
                break;
            }
        }

        System.out.println("The root is: " + name);
    }

    private static void findImbalanced(Map<String, Tower> towers) {
        while (true) {
            boolean shouldBreak = true;
            for (Map.Entry<String, Tower> entry : towers.entrySet()) {
                boolean res = entry.getValue().updateTotalWeight(towers);
                if (res) {
                    shouldBreak = false;
                }
            }

            if (shouldBreak) {
                break;
            }
        }
    }

    public static Map<String, Tower> readInput() throws IOException {
        String[] result = Helpers.read(INPUT).trim().split("\\r?\\n");
        return Arrays.stream(result).map(Tower::parse).collect(Collectors.toMap(Tower::getName, Function.identity()));
    }

    public static class Tower {
        private final String name;
        private final int weight;
        private final Set<String> connections;

        private boolean totalWeightCalculated = false;
        private int totalWeight = 0;

        public Tower(String name, int weight, Set<String> connections) {
            this.name = name;
            this.weight = weight;
            this.connections = connections;
        }

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        public Set<String> getConnections() {
            return connections;
        }

        public Set<Tower> getConnections(Map<String, Tower> towers) {
            return getConnections().stream().map(a -> towers.get(a)).collect(Collectors.toSet());
        }

        public String toString() {
            return String.format("%s (%d) -> %s", getName(), getWeight(), getConnections());
        }

        public static Tower parse(String data) {
            Set<String> connections = new HashSet<>();
            String[] split = data.split(" -> ");
            Matcher matches = Pattern.compile("(\\w+) .(\\d+).").matcher(split[0]);
            matches.find();
            String name = matches.group(1);
            int weight = Integer.parseInt(matches.group(2));
            matches.find();
            if (split.length > 1) {
                connections = Arrays.stream(split[1].split(", ")).collect(Collectors.toSet());
            }
            return new Tower(name, weight, connections);
        }

        public boolean updateTotalWeight(Map<String, Tower> towers) {
            if (totalWeightCalculated) {
                return false;
            }

            totalWeight = weight;
            Map<Integer, List<String>> weights = new HashMap<>();
            for (Tower tower : getConnections(towers)) {
                if (!tower.isTotalWeightCalculated()) {
                    return false;
                }
                int connectionWeight = tower.getTotalWeight();
                weights.putIfAbsent(connectionWeight, new ArrayList<>());
                weights.get(connectionWeight).add(tower.getName());
                totalWeight += connectionWeight;
            }

            if (weights.size() > 1) {
                Integer expected = weights.entrySet().stream().filter(a -> a.getValue().size() > 1).findAny().get().getKey();
                String key = weights.entrySet().stream().filter(a -> a.getValue().size() == 1).findAny().get().getValue().get(0);
                Tower wrongTower = towers.get(key);
                int actual = wrongTower.getTotalWeight();

                System.out.printf("The problem is with: %s; Expected %d and got %d%n", key, expected, actual);
                System.out.printf("Difference is %d.%n", expected - actual);
                System.out.printf("Base should be %d.%n", wrongTower.getWeight() + expected - actual);
            }

            totalWeightCalculated = true;
//            System.out.printf("Total weight for [%s] is: %d. Base weight is: %d%n", getName(), getTotalWeight(), getWeight());

            return true;
        }

        public boolean isTotalWeightCalculated() {
            return totalWeightCalculated;
        }

        public int getTotalWeight() {
            return totalWeight;
        }
    }
}
