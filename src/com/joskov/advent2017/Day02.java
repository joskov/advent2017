package com.joskov.advent2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 {
    public static final String INPUT = "resources/02.txt";

    public static void main(String[] args) throws IOException {
        String input = Helpers.read(INPUT);
        List<List<Integer>> data = getData(input);

        int checksum1 = data.stream().mapToInt(Day02::findMinMaxDiff).sum();
        int checksum2 = data.stream().mapToInt(Day02::findDividers).sum();

        System.out.println("Checksum 1 is: " + checksum1);
        System.out.println("Checksum 2 is: " + checksum2);
    }

    public static List<List<Integer>> getData(String input) {
        String[] rows = input.split("\\r?\\n");

        return Arrays.stream(rows)
                .map(row -> Arrays.stream(row.split("\\s+"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static int findMinMaxDiff(List<Integer> row) {
        IntSummaryStatistics summary = row.stream().mapToInt(Integer::intValue).summaryStatistics();
        return summary.getMax() - summary.getMin();
    }

    public static int findDividers(List<Integer> row) {
        int size = row.size();
        for (int i = 0; i < size; i++) {
            Integer a = row.get(i);
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    continue;
                }
                Integer b = row.get(j);
                if (a % b == 0) {
                    return a / b;
                }
            }
        }
        return 0;
    }
}
