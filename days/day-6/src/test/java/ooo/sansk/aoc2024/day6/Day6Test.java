package ooo.sansk.aoc2024.day6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day6Test {
    private Day6 subject;

    @BeforeEach
    void setup() {
        subject = new Day6();
    }

    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(41, result);
    }

    @Test
    void runPartOne() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The guard visited %d unique positions".formatted(result));
    }

    @Test
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.bruteForcePartTwo(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(6, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.bruteForcePartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("There are %d places that could cause the guard to walk in circles".formatted(result));
    }
}
