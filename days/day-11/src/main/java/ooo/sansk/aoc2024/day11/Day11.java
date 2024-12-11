package ooo.sansk.aoc2024.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Day11 {
    private static final List<QuantumRule> RULES = List.of(
        new QuantumRule((stone) -> stone == 0, (stone) -> List.of(1L)),
        new QuantumRule((stone) -> stone.toString().length() % 2 == 0, (stone) -> {
            String stoneDigits = stone.toString();

            return List.of(
                stoneDigits.substring(0, stoneDigits.length() / 2),
                stoneDigits.substring(stoneDigits.length() / 2)
            )
                .stream()
                .map(Long::parseLong)
                .toList();
        }),
        new QuantumRule((stone) -> true, (stone) -> List.of(stone * 2048))
    );

    long runPartOne(String input) {
        List<Long> stones = readStones(input);

        for (int i = 0; i < 25; i++) {
            stones = blink(stones);
        }

        return stones.size();
    }

    long runPartTwo(String input) {
        return 0;
    }

    List<Long> blink(List<Long> stones) {
        final var list = new ArrayList<Long>();
        for (Long stone : stones) {
            for (QuantumRule rule : RULES) {
                if (rule.condition.test(stone)) {
                    list.addAll(rule.action.apply(stone));
                    break;
                }
            }
        }
        return list;
    }

    List<Long> readStones(String input) {
        return Arrays.stream(input.split("\\s+", -1))
            .map(Long::parseLong)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    record QuantumRule(Predicate<Long> condition, Function<Long, List<Long>> action) {}
}
