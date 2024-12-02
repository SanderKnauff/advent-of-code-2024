package ooo.sansk.aoc2024.day1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

class Day1Test {
    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var day1 = new Day1();

        final var result = day1.runPartOne(new String(getClass().getResourceAsStream("/test-input-1").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(11, result);
    }

    @Test
    void runPartOne() throws Exception {
        final var day1 = new Day1();

        final var result = day1.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The total difference between the two lists is %d".formatted(result));
    }
    
    @Test
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var day1 = new Day1();

        final var result = day1.runPartTwo(new String(getClass().getResourceAsStream("/test-input-1").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(31, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var day1 = new Day1();

        final var result = day1.runPartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The similarity between the two lists is %d".formatted(result));
    }
}
