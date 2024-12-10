package ooo.sansk.aoc2024.day10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {
    private Day10 subject;

    @BeforeEach
    void setup() {
        subject = new Day10();
    }

    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(36, result);
    }

    @Test
    void part1_withDualTrailheadExample_ReturnsBothSides() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/dual-trailhead-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(2, result);
    }

    @Test
    void runPartOne() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The sum of trail scores is %d".formatted(result));
    }

    @Test
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(81, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The sum of trail ratings is %d".formatted(result));
    }
}
