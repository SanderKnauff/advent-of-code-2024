package ooo.sansk.aoc2024.grid;

public record Vec2d(int x, int y) {
    public Vec2d add(Vec2d offset) {
        return new Vec2d(x() + offset.x(), y() + offset.y());
    }
}
