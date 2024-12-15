package ooo.sansk.aoc2024.day15;

import ooo.sansk.aoc2024.grid.Direction;
import ooo.sansk.aoc2024.grid.Grid2d;
import ooo.sansk.aoc2024.grid.Vec2d;

import java.util.List;

class Day15Part2 {
    long runPart(String input) {
        final var inputParts = input.split("\\r?\\n\\r?\\n");
        final var map = inputParts[0];
        final var movementText = inputParts[1];

        Grid2d<WarehouseItem> warehouse = Grid2d.fromMultiDimensionalString(WarehouseItem.class, map, "|", text ->
           switch (text.charAt(0)) {
               case '#' -> WarehouseItem.WALL;
               case '.' -> WarehouseItem.EMPTY;
               case 'O' -> WarehouseItem.BOX;
               case '@' -> WarehouseItem.ROBOT;
               default -> throw new IllegalArgumentException("Invalid input " + text);
           }
        );

        List<Direction> movements = readMovements(movementText);

        simulate(warehouse, movements);

        warehouse.print(warehouseItem -> switch (warehouseItem) {
            case BOX -> "O";
            case EMPTY -> ".";
            case WALL -> "#";
            case ROBOT -> "@";
        });

        return calculateGpsSum(warehouse);
    }

    long calculateGpsSum(Grid2d<WarehouseItem> warehouse) {
        final var boxes = warehouse.findAll(warehouseItem -> warehouseItem.equals(WarehouseItem.BOX));

        long result = 0;
        for (final var box : boxes) {
            result += ((box.y() * 100L) + box.x());
        }

        return result;
    }

    List<Direction> readMovements(String movements) {
        String oneLine = movements.replaceAll("\\r?\\n", "");
        return oneLine.chars().mapToObj(operand -> switch ((char) operand) {
            case '^' -> Direction.NORTH;
            case '>' -> Direction.EAST;
            case 'v' -> Direction.SOUTH;
            case '<' -> Direction.WEST;
            default -> throw new IllegalArgumentException("Invalid movement [%c]".formatted((char) operand));
        }).toList();
    }

    void simulate(Grid2d<WarehouseItem> warehouse, List<Direction> movements) {
        Vec2d robot = warehouse.findLocationOfFirst(WarehouseItem.ROBOT).orElseThrow(() -> new IllegalArgumentException("No robot found in warehouse"));
        for (Direction movement : movements) {
            robot = simulateMovement(warehouse, robot, movement);
        }
    }

    Vec2d simulateMovement(Grid2d<WarehouseItem> warehouse, Vec2d initalRobotLocation, Direction movement) {
        final var moveResult = movement.moveTo(initalRobotLocation, warehouse);
        if (!(moveResult instanceof Direction.MoveResult.Moved result)) {
            return initalRobotLocation;
        }

        final var target = warehouse.get(result.asVector());
        final var pushResult = switch (target) {
            case WALL -> new PushResult.Failure();
            case EMPTY -> new PushResult.Success(result.asVector());
            case BOX -> {
                final var boxPushResult = tryPush(warehouse, initalRobotLocation, movement);
                yield switch (boxPushResult) {
                    case PushResult.Failure f -> new PushResult.Failure();
                    case PushResult.Success s -> new PushResult.Success(s.newLocation());
                };
            }
            case ROBOT -> throw new IllegalStateException("Robot tried moving into itself. Robot: [%s], target: [%s]".formatted(initalRobotLocation, target));
        };

        if (pushResult instanceof PushResult.Success success) {
            warehouse.set(initalRobotLocation, WarehouseItem.EMPTY);
            warehouse.set(success.newLocation(), WarehouseItem.ROBOT);
            return success.newLocation();
        } else {
            return initalRobotLocation;
        }
    }

    PushResult tryPush(Grid2d<WarehouseItem> warehouse, Vec2d box, Direction movement) {
        final var moveResult = movement.moveTo(box, warehouse);
        if (!(moveResult instanceof Direction.MoveResult.Moved result)) {
            return new PushResult.Failure();
        }

        final var target = warehouse.get(result.asVector());
        final var pushResult = switch (target) {
            case WALL -> new PushResult.Failure();
            case EMPTY -> new PushResult.Success(result.asVector());
            case BOX -> {
                final var boxPushResult = tryPush(warehouse, result.asVector(), movement);
                yield switch (boxPushResult) {
                    case PushResult.Failure f -> f;
                    case PushResult.Success s -> new PushResult.Success(result.asVector());
                };
            }
            case ROBOT -> throw new IllegalStateException("Robot tried pushing into itself");
        };

        if (pushResult instanceof PushResult.Success success) {
            warehouse.set(box, WarehouseItem.EMPTY);
            warehouse.set(success.newLocation(), WarehouseItem.BOX);
        }

        return pushResult;
    }

    sealed interface PushResult {
        record Success(Vec2d newLocation) implements PushResult {}
        record Failure() implements PushResult {}
    }

    enum WarehouseItem {
        EMPTY,
        WALL,
        BOX,
        ROBOT
    }
}
