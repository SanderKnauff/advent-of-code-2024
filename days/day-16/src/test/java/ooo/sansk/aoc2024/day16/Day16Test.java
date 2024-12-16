package ooo.sansk.aoc2024.day16;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test {
    private Day16 subject;

    @BeforeEach
    void setup() {
        subject = new Day16();
    }

    @Test
    void part1_WithFirstExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/small-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(7036, result);
    }

    @Test
    void part1_WithSecondExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/large-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(11048, result);
    }

    @Test
    void runPart1() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("<TODO> %d".formatted(result));
    }

    @Test
    void part2_WithFirstExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/small-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(7036, result);
    }

    @Test
    void part2_WithSecondExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/large-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(11048, result);
    }

    @Test
    void runPart2() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("<TODO> %d".formatted(result));
    }
}
