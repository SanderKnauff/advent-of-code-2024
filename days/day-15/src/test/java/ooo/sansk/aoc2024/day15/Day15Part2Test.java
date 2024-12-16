package ooo.sansk.aoc2024.day15;

import ooo.sansk.aoc2024.grid.Direction;
import ooo.sansk.aoc2024.grid.Vec2d;
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

        assertEquals(9021, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.runPart(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("The sum of GPS Coordinates is %d".formatted(result));
    }

    @Test
    void pushUp() {
        final var warehouse = Day15Part2.fromMultiDimensionalString("""
                ####
                #..#
                #OO#
                #OO#
                #@.#
                ####\
                """);

        subject.simulateMovement(warehouse, new Vec2d(2, 4), Direction.NORTH);

        warehouse.print(warehouseItem -> switch (warehouseItem) {
            case BOX -> "O";
            case EMPTY -> ".";
            case WALL -> "#";
            case ROBOT -> "@";
        });
    }
}
