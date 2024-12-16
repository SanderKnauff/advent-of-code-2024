package ooo.sansk.aoc2024.day15;

import ooo.sansk.aoc2024.grid.Direction;
import ooo.sansk.aoc2024.grid.Grid2d;
import ooo.sansk.aoc2024.grid.Vec2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


class Day15Part2 {
    long runPart(String input) {
        final var inputParts = input.split("\\r?\\n\\r?\\n");
        final var map = inputParts[0];
        final var movementText = inputParts[1];

        Grid2d<WarehouseItem> warehouse = fromMultiDimensionalString(map);

        warehouse.print(warehouseItem -> switch (warehouseItem) {
            case BOX -> "O";
            case EMPTY -> ".";
            case WALL -> "#";
            case ROBOT -> "@";
        });

        List<Direction> movements = readMovements(movementText);

        simulate(warehouse, movements);

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
            System.out.println(movement);
            warehouse.print(warehouseItem -> switch (warehouseItem) {
                case BOX -> "O";
                case EMPTY -> ".";
                case WALL -> "#";
                case ROBOT -> "@";
            });
        }
    }

    Vec2d simulateMovement(Grid2d<WarehouseItem> warehouse, Vec2d initialRobotLocation, Direction movement) {
        final var moveResult = movement.moveTo(initialRobotLocation, warehouse);
        if (!(moveResult instanceof Direction.MoveResult.Moved result)) {
            return initialRobotLocation;
        }

        final var target = warehouse.get(result.asVector());
        final var pushResult = switch (target) {
            case WALL -> new PushResult.Failure();
            case EMPTY -> new PushResult.Success(Set.of());
            case BOX -> {
                final var boxPushResult = tryPush(warehouse, initialRobotLocation, movement);
                yield switch (boxPushResult) {
                    case PushResult.Failure f -> new PushResult.Failure();
                    case PushResult.Success s -> new PushResult.Success(s.locationsToPush());
                };
            }
            case ROBOT -> throw new IllegalStateException("Robot tried moving into itself. Robot: [%s], target: [%s]".formatted(initialRobotLocation, target));
        };

        if (!(pushResult instanceof PushResult.Success success)) {
            return initialRobotLocation;
        }

        for (Vec2d location : success.locationsToPush()) {
            warehouse.set(location.x(), location.y(), WarehouseItem.EMPTY);
        }
        for (Vec2d location : success.locationsToPush()) {
            final var moveBoxResult = movement.moveTo(location, warehouse);
            if (moveBoxResult instanceof Direction.MoveResult.Moved moved) {
                warehouse.set(moved.asVector().x(), moved.asVector().y(), WarehouseItem.BOX);
            } else {
                throw new IllegalStateException("Box moved out of bounds");
            }
        }

        warehouse.set(initialRobotLocation, WarehouseItem.EMPTY);
        warehouse.set(result.asVector(), WarehouseItem.ROBOT);
        return result.asVector();
    }

    PushResult tryPush(Grid2d<WarehouseItem> warehouse, Vec2d box, Direction movement) {
        final var boxLeft = findLeftPartOfBox(warehouse, moved.asVector());

        final PushResult pushResult;
        if (movement.equals(Direction.NORTH) || movement.equals(Direction.SOUTH)) {
            pushResult = pushVertical(warehouse, movement, boxLeft);
        } else if (movement.equals(Direction.EAST)) {
            pushResult = pushRight(warehouse, boxLeft);
        } else if (movement.equals(Direction.WEST)) {
            pushResult = pushLeft(warehouse, boxLeft);
        } else {
            throw new IllegalStateException("Unsupported direction " + movement);
        }

        return pushResult;
    }

    private PushResult pushVertical(Grid2d<WarehouseItem> warehouse, Direction movement, Vec2d boxLeft) {
        final var boxRight = boxLeft.add(new Vec2d(1, 0));

        final var result = new ArrayList<PushResult>();
        for (Vec2d box : List.of(boxLeft, boxRight)) {
            final var target = movement.moveTo(box, warehouse).orThrow();
            final var targetType = warehouse.get(target);

            result.add(switch (targetType) {
                case WALL -> new PushResult.Failure();
                case EMPTY -> new PushResult.Success(Set.of(boxLeft, boxRight));
                case BOX -> {
                    final var boxPushResult = tryPush(warehouse, box, movement);
                    yield switch (boxPushResult) {
                        case PushResult.Failure f -> f;
                        case PushResult.Success s -> {
                            final var locations = new HashSet<>(s.locationsToPush());
                            yield new PushResult.Success(locations);
                        }
                    };
                }
                case ROBOT -> throw new IllegalStateException("Robot tried pushing into itself");
            });
        }

        if (result.stream().anyMatch(pushResult -> pushResult instanceof PushResult.Failure)) {
            return new PushResult.Failure();
        }

        final var allPushed = result.stream()
                .filter(pushResult -> pushResult instanceof PushResult.Success success)
                .map(PushResult.Success.class::cast)
                .flatMap(success -> success.locationsToPush().stream())
                .collect(Collectors.toSet());

        return new PushResult.Success(allPushed);
    }

    private PushResult pushRight(Grid2d<WarehouseItem> warehouse, Vec2d boxLeft) {
        final var boxRight = boxLeft.add(new Vec2d(1, 0));
        final var target = boxLeft.add(new Vec2d(2, 0));
        final var targetType = warehouse.get(target);

        final var rightPushResult = switch (targetType) {
            case WALL -> new PushResult.Failure();
            case EMPTY -> new PushResult.Success(Set.of(boxLeft, boxRight));
            case BOX -> {
                final var boxPushResult = tryPush(warehouse, target, Direction.EAST);
                yield switch (boxPushResult) {
                    case PushResult.Failure f -> f;
                    case PushResult.Success s -> {
                        final var locations = new HashSet<>(s.locationsToPush());
                        locations.add(boxLeft);
                        locations.add(boxRight);
                        yield new PushResult.Success(locations);
                    }
                };
            }
            case ROBOT -> throw new IllegalStateException("Robot tried pushing into itself");
        };

        if (!(rightPushResult instanceof PushResult.Success rightSuccess)) {
            return new PushResult.Failure();
        }

        final var allPushed = new HashSet<>(rightSuccess.locationsToPush());
        return new PushResult.Success(allPushed);
    }

    private PushResult pushLeft(Grid2d<WarehouseItem> warehouse, Vec2d boxLeft) {
        final var boxRight = boxLeft.add(new Vec2d(1, 0));
        final var target = boxLeft.add(new Vec2d(-1, 0));
        final var targetType = warehouse.get(target);

        final var leftPushResult = switch (targetType) {
            case WALL -> new PushResult.Failure();
            case EMPTY -> new PushResult.Success(Set.of(boxLeft, boxRight));
            case BOX -> {
                final var boxPushResult = tryPush(warehouse, target, Direction.WEST);
                yield switch (boxPushResult) {
                    case PushResult.Failure f -> f;
                    case PushResult.Success s -> {
                        final var locations = new HashSet<>(s.locationsToPush());
                        locations.add(boxLeft);
                        locations.add(boxRight);
                        yield new PushResult.Success(locations);
                    }
                };
            }
            case ROBOT -> throw new IllegalStateException("Robot tried pushing into itself");
        };

        if (!(leftPushResult instanceof PushResult.Success leftSuccess)) {
            return new PushResult.Failure();
        }

        final var allPushed = new HashSet<Vec2d>();
        allPushed.addAll(leftSuccess.locationsToPush());
        return new PushResult.Success(allPushed);
    }

    private Vec2d findLeftPartOfBox(Grid2d<WarehouseItem> warehouse, Vec2d box) {
        int boxesOnLine = 0;
        for (int i = box.x(); i >= 0; i--) {
            final var item = warehouse.get(new Vec2d(i, box.y()));
            if (item.equals(WarehouseItem.BOX)) {
                boxesOnLine++;
            } else {
                break;
            }
        }

        return new Vec2d(boxesOnLine % 2 == 1 ? box.x() : box.x() - 1, box.y());
    }

    public static Grid2d<WarehouseItem> fromMultiDimensionalString(String input) {
        final var lines = input.split("\\r?\\n", -1);
        final var height = lines.length;
        int width = 0;
        WarehouseItem[][] grid = null;

        for (int cellY = 0; cellY < lines.length; cellY++) {
            final var line = lines[cellY];
            final var cells = line.toCharArray();

            if (width == 0) {
                width = cells.length;
                grid = new WarehouseItem[width * 2][height];
            }

            for (int cellX = 0; cellX < cells.length; cellX++) {
                if (grid == null) {
                    throw new IllegalStateException("Grid was not initialized");
                }

                final var baseCell = cellX * 2;
                switch (cells[cellX]) {
                    case '.' -> {
                        grid[baseCell][cellY] = WarehouseItem.EMPTY;
                        grid[baseCell + 1][cellY] = WarehouseItem.EMPTY;
                    }
                    case '#' -> {
                        grid[baseCell][cellY] = WarehouseItem.WALL;
                        grid[baseCell + 1][cellY] = WarehouseItem.WALL;
                    }
                    case 'O' -> {
                        grid[baseCell][cellY] = WarehouseItem.BOX;
                        grid[baseCell + 1][cellY] = WarehouseItem.BOX;
                    }
                    case '@' -> {
                        grid[baseCell][cellY] = WarehouseItem.ROBOT;
                        grid[baseCell + 1][cellY] = WarehouseItem.EMPTY;
                    }
                    default -> throw new IllegalArgumentException("Cell %d is not a valid cell '%s'".formatted(cellX, cells[cellX]));
                }
            }
        }

        if (grid == null) {
            throw new IllegalStateException("Grid was not initialized");
        }
        return new Grid2d<>(grid);
    }

    sealed interface PushResult {
        record Success(Set<Vec2d> locationsToPush) implements PushResult {}
        record Failure() implements PushResult {}
    }

    enum WarehouseItem {
        EMPTY,
        WALL,
        BOX,
        ROBOT
    }
}
