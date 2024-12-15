package ooo.sansk.aoc2024.day15;

import ooo.sansk.aoc2024.grid.Direction;
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
        var location = warehouse.findLocationOfFirst(Day15Part2.WarehouseItem.ROBOT).orElseThrow(() -> new IllegalArgumentException("No robot found in warehouse"));

        subject.simulateMovement(warehouse, location, Direction.NORTH);

        warehouse.print(warehouseItem -> switch (warehouseItem) {
            case BOX -> "O";
            case EMPTY -> ".";
            case WALL -> "#";
            case ROBOT -> "@";
        });
    }

    @Test
    void pushUp_Multiple() {
        final var warehouse = Day15Part2.fromMultiDimensionalString("""
                #####
                #...#
                #OO.#
                #.O.#
                #..@#
                #####\
                """);
        var location = warehouse.findLocationOfFirst(Day15Part2.WarehouseItem.ROBOT).orElseThrow(() -> new IllegalArgumentException("No robot found in warehouse"));

        location = subject.simulateMovement(warehouse, location, Direction.NORTH);
        location = subject.simulateMovement(warehouse, location, Direction.WEST);
        location = subject.simulateMovement(warehouse, location, Direction.SOUTH);
        location = subject.simulateMovement(warehouse, location, Direction.WEST);
        location = subject.simulateMovement(warehouse, location, Direction.NORTH);

        warehouse.print(warehouseItem -> switch (warehouseItem) {
            case BOX -> "O";
            case EMPTY -> ".";
            case WALL -> "#";
            case ROBOT -> "@";
        });
    }

    @Test
    void pushDown() {
        final var warehouse = Day15Part2.fromMultiDimensionalString("""
                ####
                #@.#
                #OO#
                #OO#
                #..#
                ####\
                """);
        var location = warehouse.findLocationOfFirst(Day15Part2.WarehouseItem.ROBOT).orElseThrow(() -> new IllegalArgumentException("No robot found in warehouse"));

        subject.simulateMovement(warehouse, location, Direction.SOUTH);

        warehouse.print(warehouseItem -> switch (warehouseItem) {
            case BOX -> "O";
            case EMPTY -> ".";
            case WALL -> "#";
            case ROBOT -> "@";
        });
    }
}
