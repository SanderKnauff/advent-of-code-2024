package ooo.sansk.aoc2024.day16;

import ooo.sansk.aoc2024.grid.Grid2d;
import ooo.sansk.aoc2024.grid.Vec2d;

import java.util.Set;

class Day16 {
    long runPartOne(String input) {
        final var maze = Grid2d.fromMultiDimensionalString(Tile.class, input, "|", text -> switch (text.charAt(0)) {
            case 'S' -> Tile.START;
            case 'E' -> Tile.END;
            case '#' -> Tile.WALL;
            case '.' -> Tile.EMPTY;
            default -> throw new IllegalStateException("Unexpected value: " + text.charAt(0));
        });
        final var end = maze.findLocationOfFirst(Tile.END).orElseThrow(() -> new IllegalStateException("No end found"));

        final var graph = generateGraphFromGrid(maze);

        return calculateShortestPath(graph, end);
    }

    long runPartTwo(String input) {
        return 0;
    }

    private Node generateGraphFromGrid(Grid2d<Tile> grid) {
        final var startCoordinates = grid.findLocationOfFirst(Tile.START).orElseThrow(() -> new IllegalStateException("No start found"));
        final var start = new Location(Plane.WEST_EAST, startCoordinates);

        return null;
    }

    private long calculateShortestPath(Node graph, Vec2d end) {
        return 0;
    }

    enum Tile {
        START,
        END,
        EMPTY,
        WALL
    }

    enum Plane {
        NORTH_SOUTH,
        WEST_EAST
    }

    record Location(Plane plane, Vec2d coordinates) {}

    record Node(Location location, Set<Node> nodes) {}
}
