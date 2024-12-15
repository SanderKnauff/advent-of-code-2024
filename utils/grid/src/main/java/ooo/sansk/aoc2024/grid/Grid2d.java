package ooo.sansk.aoc2024.grid;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

/// 2d grid where each cell is of a specific type.
/// @param <CELL_TYPE>
public class Grid2d<CELL_TYPE> {
    CELL_TYPE[][] grid;

    public Grid2d(CELL_TYPE[][] grid) {
        this.grid = grid;
    }

    public boolean isOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < grid[0].length && y < grid.length;
    }

    public void set(int x, int y, CELL_TYPE value) {
        grid[x][y] = value;
    }

    public void set(Vec2d position, CELL_TYPE value) {
        grid[position.x()][position.y()] = value;
    }

    public CELL_TYPE get(int x, int y) {
        if (x >= width()) {
            throw new IllegalArgumentException("x (%d) is out of bounds".formatted(x));
        }

        if (y >= height()) {
            throw new IllegalArgumentException("y (%d) is out of bounds".formatted(y));
        }
        return grid[x][y];
    }

    public CELL_TYPE get(Vec2d vec2d) {
        return this.get(vec2d.x(), vec2d.y());
    }

    public int width() {
        return grid[0].length;
    }

    public int height() {
        return grid.length;
    }

    /// Reads a grid from a multiline string.
    /// The grid will be constructed on an X/Y coordinate where X is the character offset in the line and Y is the line number of the input.
    /// The lines are delimited by the newline (`\n`) character which optionally can be preceded by a carriage return (`\r`)
    ///
    /// @param input The input string to read. All lines must have the same amount of cells.
    /// @param cellDelimiter the character to split each cell on. An empty string will split all characters.
    /// @return a 2d grid containing all the cells
    ///
    public static <CELL_TYPE> Grid2d<CELL_TYPE> fromMultiDimensionalString(Class<CELL_TYPE> clazz, String input, String cellDelimiter, CellMapper<CELL_TYPE> cellMapper) {
        final var lines = input.split("\\r?\\n", -1);
        final var height = lines.length;
        int width = 0;
        CELL_TYPE[][] grid = null;

        for (int cellY = 0; cellY < lines.length; cellY++) {
            final var line = lines[cellY];
            final var cells = Arrays.stream(line.split(cellDelimiter, -1)).filter(not(String::isBlank)).toArray(String[]::new);

            if (width == 0) {
                width = cells.length;
                grid = (CELL_TYPE[][]) Array.newInstance(clazz, width, height);
            } else if (width != cells.length) {
                throw new IllegalArgumentException("Line %d did not have the same amount of cells as line 0".formatted(cellY));
            }

            for (int cellX = 0; cellX < cells.length; cellX++) {
                if (grid == null) {
                    throw new IllegalStateException("Grid was not initialized");
                }
                grid[cellX][cellY] = cellMapper.map(cells[cellX]);
            }
        }

        if (grid == null) {
            throw new IllegalStateException("Grid was not initialized");
        }
        return new Grid2d<>(grid);
    }

    public List<Vec2d> findAll(Predicate<CELL_TYPE> matcher) {
        final var result = new ArrayList<Vec2d>();
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (matcher.test(grid[x][y])) {
                    result.add(new Vec2d(x, y));
                }
            }
        }
        return result;
    }

    public Optional<Vec2d> findLocationOfFirst(CELL_TYPE cell) {
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (grid[x][y].equals(cell)) {
                    return Optional.of(new Vec2d(x, y));
                }
            }
        }
        return Optional.empty();
    }

    public void print(Function<CELL_TYPE,String> cellMapper) {
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                final var cell = grid[x][y];
                final var representation = cellMapper.apply(cell);
                System.out.print(representation);
            }
            System.out.println();
        }
    }

    public static boolean isInBox(int x, int y, int minX, int minY, int maxX, int maxY) {
        return (x >= minX && x < maxX && y >= minY && y < maxY);
   }
}