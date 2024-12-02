package ooo.sansk.aoc2024.day1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NonNull;

class Day1 {
    int runPartOne(@NonNull String input) {
        final var leftList = new ArrayList<Integer>();
        final var rightList = new ArrayList<Integer>();
        
        for (final var line : input.split("\n", -1)) {
            final var parts = line.split("\\s+", -1);
            if (parts.length != 2) {
                throw new IllegalArgumentException("[%s] did not contain exactly 2 parts".formatted(line));
            }

            leftList.add(Integer.parseInt(parts[0]));
            rightList.add(Integer.parseInt(parts[1]));
        }

        return calculateTotalDistance(leftList, rightList);
    }

    int runPartTwo(@NonNull String input) {
        final var locations = new ArrayList<Integer>();
        final var occurences = new HashMap<Integer, Integer>();
        
        for (final var line : input.split("\n", -1)) {
            final var parts = line.split("\\s+", -1);
            if (parts.length != 2) {
                throw new IllegalArgumentException("[%s] did not contain exactly 2 parts".formatted(line));
            }

            locations.add(Integer.parseInt(parts[0]));
            occurences.merge(Integer.parseInt(parts[1]), 1, Integer::sum);
        }

        return calculateSimilarity(locations, occurences);
    }
    
    int calculateTotalDistance(@NonNull List<Integer> list1, @NonNull List<Integer> list2) {
        if (list1.size() != list2.size()) {
            throw new IllegalArgumentException("Input lists are not the same lenght (1: %d, 2: %d)".formatted(list1.size(), list2.size()));
        }

        final var mutableList1 = new ArrayList<>(list1);
        final var mutableList2 = new ArrayList<>(list2);

        mutableList1.sort(Comparator.naturalOrder());
        mutableList2.sort(Comparator.naturalOrder());

        int result = 0;
        for (int i = 0; i < list1.size(); i++) {
            final var left = mutableList1.get(i);
            final var right = mutableList2.get(i);
            result += Math.abs(left - right);
        }

        return result;
    }
    
    int calculateSimilarity(@NonNull List<Integer> locations, @NonNull Map<Integer, Integer> occurences) {
        int result = 0;
        for (final var location : locations) {
            result += occurences.getOrDefault(location, 0) * location;
        }

        return result;
    }
}
