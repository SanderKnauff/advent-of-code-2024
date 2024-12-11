package ooo.sansk.aoc2024.day10;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.UnaryOperator;

class Day10 {
    long runPartOne(String input) {
        final var area = readMap(input);

        return area.trailHeads()
                .stream()
                .mapToInt(trailHead -> new HashSet<>(followTrail(area, trailHead, 0)).size())
                .sum();
    }

    long runPartTwo(String input) {
        final var area = readMap(input);

        return area.trailHeads()
                .stream()
                .mapToInt(trailHead -> followTrail(area, trailHead, 0).size())
                .sum();
    }

    private List<Vec2d> followTrail(Area area, Vec2d currentPosition, int lastHeight) {
        final var uniqueTrailHeads = new ArrayList<Vec2d>();
        for (Direction direction : Direction.values()) {
            final var target = direction.findCoordinateInDirection(currentPosition);
            if (!area.isPointInArea(target)) {
                continue;
            }

            final var height = area.map()[target.y()][target.x()];
            if (height == (lastHeight + 1)) {
                if (height == 9) {
                    uniqueTrailHeads.add(target);
                } else {
                    uniqueTrailHeads.addAll(followTrail(area, target, height));
                }
            }
        }

        return uniqueTrailHeads;
    }

    Area readMap(String input) {
        final var lines = input.split("\r?\n", -1);
        final var width = lines[0].length();
        final var height = lines.length;

        List<Vec2d> trailheads = new ArrayList<>();

        int[][] map = new int[width][height];

        for (int y = 0; y < lines.length; y++) {
            final String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c == '.') {
                    map[y][x] = -1;
                } else {
                    map[y][x] = Integer.parseInt(String.valueOf(c));
                }
                if (c == '0') {
                    trailheads.add(new Vec2d(x, y));
                }
            }
        }

        return new Area(trailheads, map);
    }

    record Vec2d(int x, int y) {}

    record Area(List<Vec2d> trailHeads, int[][] map) {
        boolean isPointInArea(Vec2d vec2d) {
            if (vec2d.x() < 0 || vec2d.y() < 0 || vec2d.x() >= map()[0].length || vec2d.y() >= map().length) {
                return false;
            }
            return true;
        }
    }

    enum Direction {
        NORTH(point -> new Vec2d(point.x(), point.y() - 1)),
        EAST(point -> new Vec2d(point.x() + 1, point.y())),
        SOUTH(point -> new Vec2d(point.x(), point.y() + 1)),
        WEST(point -> new Vec2d(point.x() - 1, point.y()));

        private final UnaryOperator<Vec2d> nextPointFunction;

        Direction(UnaryOperator<Vec2d> nextPointFunction) {
            this.nextPointFunction = nextPointFunction;
        }

        public Vec2d findCoordinateInDirection(Vec2d current) {
            return this.nextPointFunction.apply(current);
        }
    }
}
