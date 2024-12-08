package ooo.sansk.aoc2024.day8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day8Test {
    private Day8 subject;

    @BeforeEach
    void setup() {
        subject = new Day8();
    }

    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(14, result);
    }

    @Test
    void runPartOne() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("There are %d antinodes in the area".formatted(result));
    }

    @Test
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(34, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("There are %d resonating antinodes in the area".formatted(result));
    }

    @Test
    void test_Vec2d_Difference() {
        final var point1 = new Day8.Vec2d(5, 8);
        final var point2 = new Day8.Vec2d(7, 3);

        final var difference = point1.difference(point2);

        assertEquals(new Day8.Vec2d(-2, 5), difference);
    }

    @Test
    void test_Vec2d_Add() {
        final var point1 = new Day8.Vec2d(5, 8);
        final var point2 = new Day8.Vec2d(7, 3);
        final var difference = point1.difference(point2);

        final var result = point1.add(difference);

        assertEquals(new Day8.Vec2d(3, 13), result);
    }

    @Test
    void test_Vec2d_Subtract() {
        final var point1 = new Day8.Vec2d(5, 8);
        final var point2 = new Day8.Vec2d(7, 3);
        final var difference = point1.difference(point2);

        final var result = point2.subtract(difference);

        assertEquals(new Day8.Vec2d(9, -2), result);
    }
}
