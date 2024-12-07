package ooo.sansk.aoc2024.day7;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

class Day7 {
    BigInteger runPartOne(String input) {
        return readEquations(input)
            .stream()
            .filter(equation -> equation.canBeMadeWithOperators(new Operator[]{ Operator.ADD, Operator.MULTIPLY }))
            .map(Equation::target)
            .reduce(BigInteger.ZERO, BigInteger::add);
    }

    BigInteger runPartTwo(String input) {
        return readEquations(input)
            .stream()
            .filter(equation -> equation.canBeMadeWithOperators(Operator.values()))
            .map(Equation::target)
            .reduce(BigInteger.ZERO, BigInteger::add);
    }

    List<Equation> readEquations(String input) {
        final var equations = new ArrayList<Equation>();
        final var lines = input.split("\r?\n", -1);

        for (String line : lines) {
            final var parts = line.split(":", -1);
            final var target = new BigInteger(parts[0]);
            final var values = parts[1].split("\\s+", -1);
            final var parsedValues = Arrays.stream(values).filter(not(String::isBlank)).map(BigInteger::new).collect(Collectors.toCollection(LinkedList::new));
            equations.add(new Equation(target, parsedValues));
        }

        return equations;
    }

    record Equation(BigInteger target, List<BigInteger> values) {
        boolean canBeMadeWithOperators(Operator[] operators) {
            for (final var operator : operators) {
                final var components = new ArrayDeque<>(values);
                if (components.isEmpty()) {
                    return false;
                }
                final var canBeMade = recurse(target, components.poll(), components, operator, operators);
                if (canBeMade) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean recurse(BigInteger target, BigInteger current, Queue<BigInteger> values, Operator operator, Operator[] operators) {
        if (values.peek() == null) {
            return false;
        }

        final var result = operator.apply(current, values.poll());
        if (result.equals(target) && values.isEmpty()) {
            return true;
        }

        if (result.compareTo(target) > 0) {
            return false;
        }

        for (Operator nextOperator : operators) {
            final var canBeMadeWithOperators = recurse(target, result, new ArrayDeque<>(values), nextOperator, operators);
            if (canBeMadeWithOperators) {
                return true;
            }
        }
        return false;
    }

    enum Operator {
        ADD(BigInteger::add),
        MULTIPLY(BigInteger::multiply),
        CONCAT((a, b) -> new BigInteger(a.toString() + b));

        private final BinaryOperator<BigInteger> operator;

        Operator(BinaryOperator<BigInteger> operator) {
            this.operator = operator;
        }

        BigInteger apply(BigInteger a, BigInteger b) {
            return operator.apply(a, b);
        }
    }
}
