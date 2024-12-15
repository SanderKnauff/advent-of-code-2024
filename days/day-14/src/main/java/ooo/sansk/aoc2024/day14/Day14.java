package ooo.sansk.aoc2024.day14;

import ooo.sansk.aoc2024.grid.Vec2d;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

class Day14 {
    private static final Pattern ROBOT_LINE_PATTERN = Pattern.compile("p=(?<px>-?\\d+),(?<py>-?\\d+)\\sv=(?<vx>-?\\d+),(?<vy>-?\\d+)");

    long runPartOne(String input, int width, int height) {
        final var robots = readRobots(input).stream()
                .map(robot -> robot.move(100, width, height))
                .toList();

        return calculateSafetyFactor(robots, width, height);
    }

    long runPartTwo(String input, int width, int height) {
        // Use renderer to find number
        return 0;
    }

    List<Robot> readRobots(String input) {
        final var split = input.split("\\r?\\n", -1);

        return Arrays.stream(split)
                .map(this::readRobot)
                .toList();
    }

    public Robot readRobot(String input) {
        Matcher matcher = ROBOT_LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid input: " + input);
        }

        return new Robot(
                new Vec2d(Integer.parseInt(matcher.group("px")), Integer.parseInt(matcher.group("py"))),
                new Vec2d(Integer.parseInt(matcher.group("vx")), Integer.parseInt(matcher.group("vy")))
        );
    }

    private long calculateSafetyFactor(List<Robot> robots, int width, int height) {
        return calculateQuadrants(width, height)
                .stream()
                .mapToLong(quadrant -> robots.stream().filter(robot -> quadrant.isInside(robot.position())).count())
                .reduce(1L, (a, b) -> Math.min(a, 1) * b);
    }

    record Robot(Vec2d position, Vec2d velocity) {
        Robot move(int times, int width, int height) {
            final var newPosition = position.add(new Vec2d((velocity().x() * times), (velocity().y() * times)));
            final var teleportedPosition = new Vec2d(Math.floorMod(newPosition.x(), width), Math.floorMod(newPosition.y(), height));

            return new Robot(teleportedPosition, velocity);
        }

        int getStepsPerLoop(int width, int height) {
            Robot currentRobot = this.move(1, width, height);
            int steps = 0;
            while (!position.equals(currentRobot.position())) {
                currentRobot = currentRobot.move(1, width, height);
                steps++;
            }

            return steps;
        }
    }

    record Sector(Vec2d min, Vec2d max) {
        boolean isInside(Vec2d point) {
            return point.x() >= min().x() && point.x() < max().x() && point.y() >= min().y() && point.y() < max().y();
        }
    }

    List<Sector> calculateQuadrants(int width, int height) {
        int quadrantWidth = (int) (width / 2.0);
        int quadrantHeight = (int) (height / 2.0);

        return List.of(
                new Sector(new Vec2d(0, 0), new Vec2d(quadrantWidth, quadrantHeight)),
                new Sector(new Vec2d(width - quadrantWidth, 0), new Vec2d(width, quadrantHeight)),
                new Sector(new Vec2d(0, height - quadrantHeight), new Vec2d(quadrantWidth, height)),
                new Sector(new Vec2d(width - quadrantWidth, height - quadrantHeight), new Vec2d(width, height))
        );
    }
}
