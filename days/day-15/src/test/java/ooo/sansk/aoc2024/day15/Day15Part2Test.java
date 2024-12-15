package ooo.sansk.aoc2024.day15;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Part2Test {
    private Day15Part2 subject;

    @BeforeEach
    void setup() {
        subject = new Day15Part2();
    }

    @Test
    void part2_WithSmallExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPart(new String(getClass().getResourceAsStream("/small-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(2028, result);
    }

    @Test
    void part2_WithLargeExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPart(new String(getClass().getResourceAsStream("/large-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(10092, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.runPart(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The sum of GPS Coordinates is %d".formatted(result));
    }
}
