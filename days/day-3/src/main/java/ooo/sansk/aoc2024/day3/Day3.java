package ooo.sansk.aoc2024.day3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

class Day3 {
    private static final Pattern MULTIPLY_PATTERN = Pattern.compile("mul\\((?<first>\\d{1,3}),(?<second>\\d{1,3})\\)");
    private static final Pattern MULTIPLY_OR_DO_DONT_PATTERN = Pattern.compile("(?<do>do\\(\\))|(?<dont>don't\\(\\))|(?<mul>mul\\((?<first>\\d{1,3}),(?<second>\\d{1,3})\\))");

    int runPartOne(String input) {
        final var instructions = readMultiplyInstructions(input);

        return instructions.stream()
                .reduce(0, Integer::sum);
    }

    int runPartTwo(String input) {
        final var instructions = readMultiplyAndDoDontInstructions(input);

        return instructions.stream()
                .reduce(0, Integer::sum);
    }

    Collection<Integer> readMultiplyInstructions(String input) {
        final var instructions = new ArrayList<Integer>();

        final var matcher = MULTIPLY_PATTERN.matcher(input);
        while (matcher.find()) {
            instructions.add(Integer.parseInt(matcher.group("first")) * Integer.parseInt(matcher.group("second")));
        }

        return instructions;
    }

    Collection<Integer> readMultiplyAndDoDontInstructions(String input) {
        final var instructions = new ArrayList<Integer>();

        final var matcher = MULTIPLY_OR_DO_DONT_PATTERN.matcher(input);
        boolean multiplyActive = true;
        while (matcher.find()) {
            final var isDo = matcher.group("do") != null;
            final var isDont = matcher.group("dont") != null;

            if (isDo) {
                multiplyActive = true;
            }

            if (isDont) {
                multiplyActive = false;
            }

            if (multiplyActive && matcher.group("mul") != null) {
                instructions.add(Integer.parseInt(matcher.group("first")) * Integer.parseInt(matcher.group("second")));
            }
        }

        return instructions;
    }
}
