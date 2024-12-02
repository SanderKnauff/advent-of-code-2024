package ooo.sansk.aoc2024.day2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class Day2Test {
    private Day2 subject;

    @BeforeEach
    void setup() {
        subject = new Day2();
    }

    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/test-input-1").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(2, result);
    }

    @Test
    void runPartOne() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The amount of safe reports is %d".formatted(result));
    }
    
    @Test
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/test-input-1").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(4, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));
        // final var result = subject.day2Naive(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The amount of safe reports is %d".formatted(result));
    }

    @ParameterizedTest
    @CsvSource({
        "71 73 72 69 66, true",
        "51 50 51 52 54, true",
    })
    void a(String reportText, boolean expected) {
        final var report = Arrays.stream(reportText.split("\\s+")).mapToInt(Integer::parseInt).toArray();

        final var result = subject.isReportSafeWithProblemsDampend(report, -1);

        assertEquals(expected, result);

    }
}
