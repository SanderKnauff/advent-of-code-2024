package ooo.sansk.aoc2024.day14;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import ooo.sansk.aoc2024.grid.Vec2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Day14Test {
    private Day14 subject;

    @BeforeEach
    void setup() {
        subject = new Day14();
    }

    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(
                new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8),
                11,
                7
        );

        assertEquals(12, result);
    }

    @Test
    void runPartOne() throws Exception {
        final var result = subject.runPartOne(
                new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8),
                101,
                103
        );

        System.out.println("After 100 seconds, the security factor is %d".formatted(result));
    }

    @Test
    @Disabled("Use renderer")
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(
                new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8),
                11,
                7
        );

        assertEquals(12, result);
    }

    @Test
    @Disabled("Use renderer")
    void runPartTwo() throws Exception {
        final var result = subject.runPartTwo(
                new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8),
                101,
                103
        );

        System.out.println("After 100 seconds, the security factor is %d".formatted(result));
    }

    @Test
    void render() throws Exception {
        Application.launch(
                TreeViewer.class,
                new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8),
                "101",
                "103"
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "p=0,4 v=3,-3;3;1",
            "p=6,3 v=-1,-3;5;0",
            "p=10,3 v=-1,2;9;5",
            "p=2,0 v=2,-1;4;6",
            "p=0,0 v=1,3;1;3",
            "p=3,0 v=-2,-2;1;5", // Wrapping
            "p=7,6 v=-1,-3;6;3",
            "p=3,0 v=-1,-2;2;5", // Wrapping
            "p=9,3 v=2,3;0;6", // Wrapping
            "p=7,3 v=-1,2;6;5",
            "p=2,4 v=2,-3;4;1",
            "p=9,5 v=-3,-3;6;2",
    }, delimiter = ';')
    void robot_Move1Time_MovesCorrectDistance(String input, int expectedX, int expectedY) {
        final var robot = subject.readRobot(input);

        final var result = robot.move(1, 11, 7);

        assertEquals(new Vec2d(expectedX, expectedY), result.position());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "p=0,4 v=3,-3;0;0",
            "p=6,3 v=-1,-3;0;0",
            "p=10,3 v=-1,2;0;0",
            "p=2,0 v=2,-1;0;0",
            "p=0,0 v=1,3;0;0",
            "p=3,0 v=-2,-2;0;0",
            "p=7,6 v=-1,-3;0;0",
            "p=3,0 v=-1,-2;0;0",
            "p=9,3 v=2,3;0;0",
            "p=7,3 v=-1,2;0;0",
            "p=2,4 v=2,-3;0;0",
            "p=9,5 v=-3,-3;0;0",
    }, delimiter = ';')
    void robot_Move100Times_MovesCorrectDistance(String input, int expectedX, int expectedY) {
        final var robot = subject.readRobot(input);

        final var result = robot.move(100, 11, 7);

        assertEquals(new Vec2d(expectedX, expectedY), result.position());
    }

    @Test
    void sector_isInside_calculatesRightResult() {
        final var sector = new Day14.Sector(new Vec2d(0, 0), new Vec2d(10, 10));

        assertTrue(sector.isInside(new Vec2d(0, 0)));
        assertTrue(sector.isInside(new Vec2d(5, 5)));
        assertFalse(sector.isInside(new Vec2d(10, 10)));
    }
}
