package ooo.sansk.aoc2024.day17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {
    private Day17 subject;

    @BeforeEach
    void setup() {
        subject = new Day17();
    }

    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/test-input-1").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals("4,6,3,5,6,3,5,2,1,0", result);
    }

    @Test
    void runPartOne() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The output of the program is [%s]".formatted(result));
    }

    @Test
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/test-input-2").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(117440, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The lowest non corrupt value for register A is %d".formatted(result));
    }
}
