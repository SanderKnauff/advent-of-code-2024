package ooo.sansk.aoc2024.grid;

public enum Direction {
    NORTH((int x, int y, int minX, int minY, int maxX, int maxY) -> {
        final var target = new Vec2d(x, y + 1);
        if (!Grid2d.isInBox(target.x(), target.y(), minX, minY, maxX, maxY)) {
            return new MoveResult.OutOfBounds();
        }
        return new MoveResult.Moved(target.x(), target.y());
    }),
    EAST((int x, int y, int minX, int minY, int maxX, int maxY) -> {
        final var target = new Vec2d(x + 1, y);
        if (!Grid2d.isInBox(target.x(), target.y(), minX, minY, maxX, maxY)) {
            return new MoveResult.OutOfBounds();
        }
        return new MoveResult.Moved(target.x(), target.y());
    }),
    SOUTH((int x, int y, int minX, int minY, int maxX, int maxY) -> {
        final var target = new Vec2d(x, y - 1);
        if (!Grid2d.isInBox(target.x(), target.y(), minX, minY, maxX, maxY)) {
            return new MoveResult.OutOfBounds();
        }
        return new MoveResult.Moved(target.x(), target.y());
    }),
    WEST((int x, int y, int minX, int minY, int maxX, int maxY) -> {
        final var target = new Vec2d(x - 1, y);
        if (!Grid2d.isInBox(target.x(), target.y(), minX, minY, maxX, maxY)) {
            return new MoveResult.OutOfBounds();
        }
        return new MoveResult.Moved(target.x(), target.y());
    }),
    NORTH_EAST((int x, int y, int minX, int minY, int maxX, int maxY) -> {
        final var step = NORTH.move.calculate(x, y, minX, minY, maxX, maxY);
        return switch (step) {
            case MoveResult.OutOfBounds result -> result;
            case MoveResult.Moved (int movedX, int movedY) -> EAST.move.calculate(movedX, movedY, minX, minY, maxX, maxY) ;
        };
    }),
    SOUTH_EAST((int x, int y, int minX, int minY, int maxX, int maxY) -> {
        final var step = SOUTH.move.calculate(x, y, minX, minY, maxX, maxY);
        return switch (step) {
            case MoveResult.OutOfBounds result -> result;
            case MoveResult.Moved (int movedX, int movedY) -> EAST.move.calculate(movedX, movedY, minX, minY, maxX, maxY) ;
        };    }),
    SOUTH_WEST((int x, int y, int minX, int minY, int maxX, int maxY) -> {
        final var step = SOUTH.move.calculate(x, y, minX, minY, maxX, maxY);
        return switch (step) {
            case MoveResult.OutOfBounds result -> result;
            case MoveResult.Moved (int movedX, int movedY) -> WEST.move.calculate(movedX, movedY, minX, minY, maxX, maxY) ;
        };
    }),
    NORTH_WEST((int x, int y, int minX, int minY, int maxX, int maxY) -> {
        final var step = NORTH.move.calculate(x, y, minX, minY, maxX, maxY);
        return switch (step) {
            case MoveResult.OutOfBounds result -> result;
            case MoveResult.Moved (int movedX, int movedY) -> WEST.move.calculate(movedX, movedY, minX, minY, maxX, maxY) ;
        };
    });

    private final CalculateNextPositionFunction move;

    Direction(CalculateNextPositionFunction move) {
        this.move = move;
    }

    public MoveResult moveTo(int x, int y, int minX, int minY, int maxX, int maxY) {
        return this.move.calculate(x, y, minX, minY, maxX, maxY);
    }

    public MoveResult moveTo(Vec2d current, Grid2d<?> grid) {
        return this.moveTo(current.x(), current.y(), 0, 0, grid.width(), grid.height());
    }

    @FunctionalInterface
    private interface CalculateNextPositionFunction {
        MoveResult calculate(int x, int y, int minX, int minY, int maxX, int maxY);
    }

    public sealed interface MoveResult {
        record OutOfBounds() implements MoveResult {}
        record Moved(int x, int y) implements MoveResult {
            public Vec2d asVector() {
                return new Vec2d(x, y);
            }
        }
    }
}
