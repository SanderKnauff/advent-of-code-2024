package ooo.sansk.aoc2024.day11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {
    private Day11 subject;

    @BeforeEach
    void setup() {
        subject = new Day11();
    }

    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(55312, result);
    }

    @Test
    void runPartOne() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("After blinking 25 times there are %d stones".formatted(result));
    }

    @Test
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(-1, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("After blinking 75 times, there are %d stones".formatted(result));
    }

    @Test
    void blink_WithIncrementalExamples_HasExpectedResults() {
        Map<Long, Long> stones = Map.ofEntries(
                Map.entry(125L, 1L),
                Map.entry(17L, 1L)
        );

        stones = subject.blink(stones);
        assertEquals(Map.ofEntries(
                Map.entry(253000L, 1L),
                Map.entry(1L, 1L),
                Map.entry(7L, 1L)
        ), stones);

        stones = subject.blink(stones);
        assertEquals(Map.ofEntries(
                Map.entry(253L, 1L),
                Map.entry(0L, 1L),
                Map.entry(2024L, 1L),
                Map.entry(14168L, 1L)
        ), stones);

        stones = subject.blink(stones);
        assertEquals(Map.ofEntries(
                Map.entry(512072L, 1L),
                Map.entry(1L, 1L),
                Map.entry(20L, 1L),
                Map.entry(24L, 1L),
                Map.entry(28676032L, 1L)
        ), stones);

        stones = subject.blink(stones);
        assertEquals(Map.ofEntries(
                Map.entry(512L, 1L),
                Map.entry(72L, 1L),
                Map.entry(2024L, 1L),
                Map.entry(2L, 2L),
                Map.entry(0L, 1L),
                Map.entry(4L, 1L),
                Map.entry(2867L, 1L),
                Map.entry(6032L, 1L)
        ), stones);
    }
}
