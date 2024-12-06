package ooo.sansk.aoc2024.day6;

import javax.swing.text.Position;
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
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map.length; x++) {
                final var cell = map[x][y];
                char c = '.';
                if (cell == 1) {
                    c = '#';
                }
                final var currentPosition = new Point(x, y);
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

        Set<Point> obstructablePositions = new HashSet<>();
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
        for (int y = 0; y < map.map.length; y++) {
            for (int x = 0; x < map.map[y].length; x++) {
                Point position = new Point(x, y);
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
        private Point position;
        private Direction facing;
        private Set<Step> visited;

        public Guard(Point initialPosition, Direction initialFacing) {
            this.position = initialPosition;
            this.facing = initialFacing;
            this.visited = new HashSet<>();
            visited.add(new Step(this.facing, this.position));
        }

        StepResult step(AreaMap map) {
            Point target = this.facing.findPointInDirection(position);
            while (map.isOccupied(target)) {
                this.facing = this.facing.turnRight();
                target = this.facing.findPointInDirection(position);
            }

            this.position = target;
            if (position.x() < 0 || position.x() >= map.map().length || position.y() < 0 || position.y() >= map.map()[0].length) {
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
        final var lines = input.split("\r?\n", -1);
        final var width = lines[0].length();
        final var height = lines.length;

        int[][] map = new int[width][height];
        Point initalPosition = null;
        for (int y = 0; y < lines.length; y++) {
            final String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                map[x][y] = switch (c) {
                    case '.' -> 0;
                    case '#' -> 1;
                    case '^' -> {
                        initalPosition = new Point(x, y);
                        yield 0;
                    }
                    default -> throw new IllegalArgumentException("Map had invalid character" + c);
                };
            }
        }

        if (initalPosition == null) {
            throw new IllegalArgumentException("Map had no starting point");
        }

        return new AreaMap(map, initalPosition);
    }

    record AreaMap(int[][] map, Point initialPosition) {
        AreaMap copyWithObstruction(Point obstruction) {
            int[][] newMap = new int[this.map.length][this.map[0].length];
            for (int y = 0; y < this.map.length; y++) {
                newMap[y] = Arrays.copyOf(this.map[y], this.map[y].length);
            }

            if (obstruction.x() >= 0 && obstruction.x() < map.length && obstruction.y() >= 0 && obstruction.y() < map[0].length) {
                newMap[obstruction.x()][obstruction.y()] = 1;
            }
            return new AreaMap(newMap, initialPosition);
        }

        private boolean isOccupied(Point position) {
            if (position.x() < 0 || position.x() >= map.length || position.y() < 0 || position.y() >= map[0].length) {
                return false;
            }
            return map[position.x()][position.y()] == 1;
        }
    }

    record Point(int x, int y) {
    }

    record Step(Direction facing, Point position) {}

    enum Direction {
        NORTH(point -> new Point(point.x(), point.y() - 1)),
        EAST(point -> new Point(point.x() + 1, point.y())),
        SOUTH(point -> new Point(point.x(), point.y() + 1)),
        WEST(point -> new Point(point.x() - 1, point.y()));

        private final UnaryOperator<Point> nextPointFunction;

        Direction(UnaryOperator<Point> nextPointFunction) {
            this.nextPointFunction = nextPointFunction;
        }

        public Direction turnRight() {
            final var directions = Direction.values();
            return directions[(this.ordinal() + 1) % directions.length];
        }

        public Point findPointInDirection(Point current) {
            return this.nextPointFunction.apply(current);
        }
    }

    enum StepResult {
        PATROLLING,
        LEFT_AREA,
        LOOPING
    }

}
