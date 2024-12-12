package ooo.sansk.aoc2024.day6;

import ooo.sansk.aoc2024.grid.Grid2d;
import ooo.sansk.aoc2024.grid.Vec2d;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

class Day6 {
    int runPartOne(String input) {
        final var map = readMap(input);
        final var guard = new Guard(map.initialPosition(), Direction.NORTH);

        System.out.println(map.initialPosition());
        while (guard.step(map).equals(StepResult.PATROLLING)) {
//            printMap(map, guard);
        }

        return guard.countVisitedPositions();
    }

    private void printMap(AreaMap area, Guard guard) {
        System.out.println("-----");
        final var map = area.map();
        for (int y = 0; y < map.height(); y++) {
            for (int x = 0; x < map.width(); x++) {
                final var cell = map.get(x, y);
                char c = '.';
                if (cell == 1) {
                    c = '#';
                }
                final var currentPosition = new Vec2d(x, y);
                if (guard.visited.contains(currentPosition)) {
                    c = 'X';
                }
                if (guard.position.equals(currentPosition)) {
                    c = '^';
                }

                System.out.print(c);
            }
            System.out.printf("%n");
        }
    }

    int runPartTwo(String input) {
        final var map = readMap(input);
        final var guard = new Guard(map.initialPosition(), Direction.NORTH);

        Set<Vec2d> obstructablePositions = new HashSet<>();
        while (guard.step(map).equals(StepResult.PATROLLING)) {
            final var target = guard.facing.findPointInDirection(guard.position);
            if (!map.isOccupied(target)) {
                final var copyMap = map.copyWithObstruction(target);
                if (canBlockGuard(guard, copyMap)) {
                    obstructablePositions.add(target);
                }
            }
        }

        return obstructablePositions.size();
    }

    int bruteForcePartTwo(String input) {
        int amount = 0;
        final var map = readMap(input);
        for (int y = 0; y < map.map.height(); y++) {
            for (int x = 0; x < map.map().width(); x++) {
                Vec2d position = new Vec2d(x, y);
                if (map.isOccupied(position)) {
                    continue;
                }
                final var obstructedMap = map.copyWithObstruction(position);
                Guard guard = new Guard(obstructedMap.initialPosition(), Direction.NORTH);
                StepResult stepResult = StepResult.PATROLLING;
                while (stepResult == StepResult.PATROLLING) {
                    stepResult = guard.step(obstructedMap);
                }

                if (stepResult.equals(StepResult.LOOPING)) {
                    amount++;
                }
            }
        }

        return amount;
    }

    private boolean canBlockGuard(Guard originalGuard, AreaMap map) {
        Guard guard = originalGuard.copy();
        StepResult stepResult = StepResult.PATROLLING;
        while (stepResult == StepResult.PATROLLING) {
            stepResult = guard.step(map);
        }
        return stepResult.equals(StepResult.LOOPING);
    }

    class Guard {
        private Vec2d position;
        private Direction facing;
        private Set<Step> visited;

        public Guard(Vec2d initialPosition, Direction initialFacing) {
            this.position = initialPosition;
            this.facing = initialFacing;
            this.visited = new HashSet<>();
            visited.add(new Step(this.facing, this.position));
        }

        StepResult step(AreaMap map) {
            Vec2d target = this.facing.findPointInDirection(position);
            while (map.isOccupied(target)) {
                this.facing = this.facing.turnRight();
                target = this.facing.findPointInDirection(position);
            }

            this.position = target;
            if (!map.map().isOnGrid(position.x(), position.y())) {
                return StepResult.LEFT_AREA;
            }
            if (visited.contains(new Step(this.facing, this.position))) {
                return StepResult.LOOPING;
            }
            visited.add(new Step(this.facing, this.position));

            return StepResult.PATROLLING;
        }

        int countVisitedPositions() {
            return visited.stream().map(Step::position).collect(Collectors.toSet()).size();
        }

        public Guard copy() {
            final var guard = new Guard(this.position, this.facing);
            guard.visited = new HashSet<>(this.visited);
            return guard;
        }
    }

    private AreaMap readMap(String input) {
        final var grid = Grid2d.fromMultiDimensionalString(Integer.class, input, "|", text -> switch (text) {
            case "." -> 0;
            case "#" -> 1;
            case "^" -> 2;
            default -> throw new IllegalArgumentException("Map had invalid character (%s)".formatted(text));
        });

        Vec2d initialPosition = null;
        for (int x = 0; x < grid.width(); x++) {
            for (int y = 0; y < grid.height(); y++) {
                final var pos = grid.get(x, y);
                if (pos.equals(2)) {
                    initialPosition = new Vec2d(x, y);
                    grid.set(x, y, 0);
                    break;
                }
            }
        }

        if (initialPosition == null) {
            throw new IllegalStateException("No initial position found");
        }

        return new AreaMap(grid, initialPosition);
    }

    record AreaMap(Grid2d<Integer> map, Vec2d initialPosition) {
        AreaMap copyWithObstruction(Vec2d obstruction) {
            Integer[][] newMap = new Integer[this.map.width()][this.map.height()];
            for (int y = 0; y < this.map.height(); y++) {
                for (int x = 0; x < this.map.width(); x++) {
                    newMap[x][y] = this.map.get(x, y);
                }
            }

            if (obstruction.x() >= 0 && obstruction.x() < map.height() && obstruction.y() >= 0 && obstruction.y() < map.width()) {
                newMap[obstruction.x()][obstruction.y()] = 1;
            }
            return new AreaMap(new Grid2d(newMap), initialPosition);
        }

        private boolean isOccupied(Vec2d position) {
            if(!map.isOnGrid(position.x(), position.y())) {
                return false;
            }

            return map.get(position.x(), position.y()) == 1;
        }
    }

    record Step(Direction facing, Vec2d position) {}

    enum Direction {
        NORTH(point -> new Vec2d(point.x(), point.y() - 1)),
        EAST(point -> new Vec2d(point.x() + 1, point.y())),
        SOUTH(point -> new Vec2d(point.x(), point.y() + 1)),
        WEST(point -> new Vec2d(point.x() - 1, point.y()));

        private final UnaryOperator<Vec2d> nextPointFunction;

        Direction(UnaryOperator<Vec2d> nextPointFunction) {
            this.nextPointFunction = nextPointFunction;
        }

        public Direction turnRight() {
            final var directions = Direction.values();
            return directions[(this.ordinal() + 1) % directions.length];
        }

        public Vec2d findPointInDirection(Vec2d current) {
            return this.nextPointFunction.apply(current);
        }
    }

    enum StepResult {
        PATROLLING,
        LEFT_AREA,
        LOOPING
    }

}
