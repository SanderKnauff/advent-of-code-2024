package ooo.sansk.aoc2024.day12;

import ooo.sansk.aoc2024.grid.Direction;
import ooo.sansk.aoc2024.grid.Grid2d;
import ooo.sansk.aoc2024.grid.Vec2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Day12 {
    private static final Direction[] DIRECTIONS = new Direction[]{ Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    long runPartOne(String input) {
        Grid2d<Character> grid = Grid2d.fromMultiDimensionalString(Character.class, input, "|", text -> text.charAt(0));

        Set<Vec2d> remaining = new HashSet<>();
        for (int x = 0; x < grid.width(); x++) {
            for (int y = 0; y < grid.height(); y++) {
                remaining.add(new Vec2d(x, y));
            }
        }

        final var regions = new ArrayList<FencedRegion>();
        while (!remaining.isEmpty()) {
            final var position = remaining.iterator().next();

            regions.add(exploreArea(grid, remaining, position));
        }

        return regions.stream().mapToLong(value -> value.area() * value.fences().size()).sum();
    }

    long runPartTwo(String input) {
        Grid2d<Character> grid = Grid2d.fromMultiDimensionalString(Character.class, input, "|", text -> text.charAt(0));

        Set<Vec2d> remaining = new HashSet<>();
        for (int x = 0; x < grid.width(); x++) {
            for (int y = 0; y < grid.height(); y++) {
                remaining.add(new Vec2d(x, y));
            }
        }

        final var regions = new ArrayList<FencedRegion>();
        while (!remaining.isEmpty()) {
            final var position = remaining.iterator().next();

            regions.add(exploreArea(grid, remaining, position));
        }

        return regions.stream().mapToLong(value -> value.area() * value.countSides()).sum();
    }

    private FencedRegion exploreArea(Grid2d<Character> grid, Set<Vec2d> remaining, Vec2d position) {
        char plantType = grid.get(position);
        remaining.remove(position);

        Set<Fence> fences = new HashSet<>();
        long area = 1;

        for (Direction direction : DIRECTIONS) {
            final var result = direction.moveTo(position, grid);
            if (!(result instanceof Direction.MoveResult.Moved target)) {
                fences.add(new Fence(position, direction));
                continue;
            }

            char targetPlantType = grid.get(target.asVector());
            if (targetPlantType != plantType) {
                fences.add(new Fence(position, direction));
                continue;
            }

            if (!remaining.contains(target.asVector())) {
                continue;
            }

            final var region = exploreArea(grid, remaining, target.asVector());

            fences.addAll(region.fences());
            area += region.area();
        }

        return new FencedRegion(area, fences);
    }

    record FencedRegion(long area, Set<Fence> fences) {
        long countSides() {
            final var unaccountedFences = new HashSet<>(fences);
            int sides = 0;
            while (!unaccountedFences.isEmpty()) {
                final var fence = unaccountedFences.iterator().next();

                findAndRemoveAllAdjacentFences(unaccountedFences, fence);
                sides++;
            }

            return sides;
        }

        private void findAndRemoveAllAdjacentFences(Set<Fence> remaining, Fence fence) {
            remaining.remove(fence);

            final var moveDirections = switch (fence.direction) {
                case NORTH, SOUTH -> List.of(Direction.EAST, Direction.WEST);
                case EAST, WEST -> List.of(Direction.NORTH, Direction.SOUTH);
                default -> throw new IllegalStateException("Unexpected direction: " + fence.direction);
            };

            for (Direction direction : moveDirections) {
                final var result = direction.moveTo(fence.position.x(), fence.position.y(), Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
                if (!(result instanceof Direction.MoveResult.Moved target)) {
                    throw new IllegalStateException("Unexpected out of bounds in inifite grid");
                }

                if (!remaining.contains(new Fence(target.asVector(), fence.direction))) {
                    continue;
                }

                findAndRemoveAllAdjacentFences(remaining, new Fence(target.asVector(), fence.direction));
            }
        }
    }

    record Fence(Vec2d position, Direction direction) {}
}
