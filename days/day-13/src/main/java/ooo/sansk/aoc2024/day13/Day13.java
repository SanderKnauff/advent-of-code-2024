package ooo.sansk.aoc2024.day13;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalLong;
import java.util.regex.Pattern;

class Day13 {
    private static final Pattern BUTTON_PATTERN = Pattern.compile("Button \\w: X\\+(?<X>\\d+), Y\\+(?<Y>\\d+)");
    private static final Pattern PRIZE_PATTERN = Pattern.compile("Prize: X=(?<X>\\d+), Y=(?<Y>\\d+)");

    long runPartOne(String input) {
        Loader.loadNativeLibraries();
        final var machines = readInput(input, new Vec2d(0, 0));

        return machines.stream()
                .flatMapToLong(arcadeMachine -> arcadeMachine.play().stream())
                .sum();
    }

    long runPartTwo(String input) {
        Loader.loadNativeLibraries();
        final var machines = readInput(input, new Vec2d(10_000_000_000_000L, 10_000_000_000_000L));

        return machines.stream()
                .flatMapToLong(arcadeMachine -> arcadeMachine.play().stream())
                .sum();
    }

    List<ArcadeMachine> readInput(String input, Vec2d prizeOffset) {
        return Arrays.stream(input.split("\\r?\\n\\r?\\n", -1))
                .map((String input1) -> readArcadeMachine(input1, prizeOffset))
                .toList();
    }

    ArcadeMachine readArcadeMachine(String input, Vec2d prizeOffset) {
        final var parts = input.split("\\r?\\n", -1);

        final var buttonA = readButton(parts[0]);
        final var buttonB = readButton(parts[1]);
        final var prizeLocation = readPriceLocation(parts[2], prizeOffset);

        return new ArcadeMachine(buttonA, buttonB, prizeLocation);
    }

    Vec2d readButton(String buttonLine) {
        final var parts = BUTTON_PATTERN.matcher(buttonLine);
        if (!parts.matches()) {
            throw new IllegalArgumentException("Invalid button line: " + buttonLine);
        }
        return new Vec2d(Integer.parseInt(parts.group("X")), Integer.parseInt(parts.group("Y")));
    }

    Vec2d readPriceLocation(String priceLine, Vec2d prizeOffset) {
        final var parts = PRIZE_PATTERN.matcher(priceLine);
        if (!parts.matches()) {
            throw new IllegalArgumentException("Invalid prizeline line: " + priceLine);
        }
        return new Vec2d(Integer.parseInt(parts.group("X")), Integer.parseInt(parts.group("Y"))).add(prizeOffset);
    }

    record ArcadeMachine(Vec2d aButtonOffset, Vec2d bButtonOffset, Vec2d prizeLocation) {
        @SuppressWarnings("unused") // This method has been added after the fact when reading /r/adventofcode. The solution used to solve the puzzle was made using OR-Tools
        OptionalLong playCalculated() {
            final var determinant = (aButtonOffset().x() * bButtonOffset().y()) - aButtonOffset().y() * bButtonOffset().x();
            double aPresses = (double) ((this.prizeLocation().x() * bButtonOffset().y()) - (this.prizeLocation().y() * bButtonOffset().x())) / determinant;
            double bPresses = (double) ((aButtonOffset().x() * prizeLocation.y()) - (aButtonOffset().y() * prizeLocation().x())) / determinant;

            if ((long) aPresses != aPresses || (long) bPresses != bPresses) {
                return OptionalLong.empty();
            }

            return OptionalLong.of((long) ((3 * aPresses) + bPresses));
        }

        OptionalLong play() {
            MPSolver solver = MPSolver.createSolver("SCIP");
            final var aPresses = solver.makeIntVar(0, MPSolver.infinity(), "aPresses");
            final var bPresses = solver.makeIntVar(0, MPSolver.infinity(), "bPresses");

            MPConstraint xConstraint = solver.makeConstraint(prizeLocation.x(), prizeLocation.x());
            xConstraint.setCoefficient(aPresses, aButtonOffset.x());
            xConstraint.setCoefficient(bPresses, bButtonOffset.x());

            MPConstraint yConstraint = solver.makeConstraint(prizeLocation.y(), prizeLocation.y());
            yConstraint.setCoefficient(aPresses, aButtonOffset.y());
            yConstraint.setCoefficient(bPresses, bButtonOffset.y());

            final var objective = solver.objective();
            objective.setCoefficient(aPresses, 3);
            objective.setCoefficient(bPresses, 1);

            objective.setMinimization();

            final var result = solver.solve();

            if (!result.equals(MPSolver.ResultStatus.OPTIMAL)) {
                return OptionalLong.empty();
            }

            return OptionalLong.of((long) objective.value());
        }
    }

    record Vec2d(long x, long y) {
        public Vec2d add(Vec2d offset) {
            return new Vec2d(x() + offset.x(), y() + offset.y());
        }
    }
}
