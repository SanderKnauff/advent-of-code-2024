package ooo.sansk.aoc2024.day11;

import java.util.HashMap;
import java.util.Map;

class Day11 {
    long runPartOne(String input) {
        Map<Long, Long> stones = readStones(input);

        for (int i = 0; i < 25; i++) {
            stones = blink(stones);
        }

        return stones.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    long runPartTwo(String input) {
        Map<Long, Long> stones = readStones(input);

        for (int i = 0; i < 75; i++) {
            stones = blink(stones);
        }

        return stones.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    Map<Long, Long> blink(Map<Long, Long> stones) {
        Map<Long, Long> newMap = new HashMap<>();

        for (var entry : stones.entrySet()) {
            final var stone = entry.getKey();
            final var count = entry.getValue();

            if (stone == 0L) {
                newMap.merge(1L, count, Long::sum);
                continue;
            }

            long digits = ((long) Math.log10(stone) + 1);
            if (((int) digits) % 2 == 0) {
                final var high = (long) (stone / Math.pow(10, digits / 2));
                final var low = (long) (stone - (high * Math.pow(10, digits / 2)));
                newMap.merge(high, count, Long::sum);
                newMap.merge(low, count, Long::sum);
                continue;
            }

            newMap.merge(stone * 2024, count, Long::sum);
        }

        return newMap;
    }

    Map<Long, Long> readStones(String input) {
        final var map = new HashMap<Long, Long>();

        final var numbers = input.split("\\s+", -1);
        for (String numberText : numbers) {
            long number = Long.parseLong(numberText);
            map.merge(number, 1L, Long::sum);
        }

        return map;
    }
}
