package ooo.sansk.aoc2024.day3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3Test {
    private Day3 subject;

    @BeforeEach
    void setup() {
        subject = new Day3();
    }

    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/test-input-1").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(161, result);
    }

    @Test
    void runPartOne() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The sum of the result of all multiply instructions is %d".formatted(result));
    }
    
    @Test
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/test-input-2").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(48, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The sum of the result of all active multiply instructions is %d".formatted(result));
    }

}
