package ooo.sansk.aoc2024.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Day2 {
    int runPartOne(String input) {
        int amountOfSafeReports = 0;

        for (final var reportText : input.split("\n", -1)) {
            final var report = Arrays.stream(reportText.split("\\s+", -1)).mapToInt(Integer::parseInt).toArray();
            final boolean isSafe = isReportSafe(report);
            if (isSafe) {
                amountOfSafeReports++;
            }
        }

        return amountOfSafeReports;
    }

    int runPartTwo(String input) {
        // This method is very inefficient, but less complex. Since it works in a reasonable amount of time, I'm not going to bother too much.
        return day2Naive(input);
/*         int amountOfSafeReports = 0;

        for (final var reportText : input.split("\n", -1)) {
            final var report = Arrays.stream(reportText.split("\\s+", -1)).mapToInt(Integer::parseInt).toArray();
            final var isSafe = isReportSafeWithProblemsDampend(report, -1);
            if (isSafe) {
                amountOfSafeReports++;
            }
        }

        return amountOfSafeReports; */
    }
    
    boolean isReportSafe(int[] report) {
        int previousValue = report[0];
        Direction direction = null;
        for (int i = 1; i < report.length; i++) {
            final var value = report[i];
            
            final Direction newDirection;
            if (value > previousValue) {
                newDirection = Direction.INCREASING;
            } else if (value < previousValue) {
                newDirection = Direction.DECREASING;
            } else {
                // Values are equal, which means the report is unsafe
                return false;
            }

            if (direction == null) {
                direction = newDirection;
            } else if (!direction.equals(newDirection)) {
                return false;
            }

            int difference = Math.abs(value - previousValue);
            boolean isSafeDifference = difference >= 1 && difference <= 3;

            if (!isSafeDifference) {
                return false;
            }

            previousValue = value;
        }

        return true;
    }
    
    // TODO This should be more efficient, but the directionality checks fail in certain cases. 
    /*
    boolean isReportSafeWithProblemsDampend(int[] originalReport, int skip) {
        int[] report = originalReport;
        if (skip >= 0) {
            final var copy = new ArrayList<Integer>();
            for (int i = 0; i < originalReport.length; i++) {
                if (i != skip) {
                    copy.add(originalReport[i]);
                }
                report = copy.stream().mapToInt(Integer::intValue).toArray();
            }
        }

        Direction direction = null;
        int previousValue = report[0];
        for (int i = 1; i < report.length; i++) {
            final var value = report[i];
            
            final Direction newDirection = Direction.from(previousValue, value);
            if (Direction.CONSTANT.equals(newDirection)) {
                // Values are equal, which means the report is unsafe
                if (skip >= 0) {
                    return false;
                }
                return recheckWithIncompatibleValueRemoved(report, i);
            }

            if (direction == null) {
                direction = newDirection;
            } else if (!direction.equals(newDirection)) {
                if (skip >= 0) {
                    return false;
                }
                return recheckWithIncompatibleValueRemoved(report, i);
            }

            int difference = Math.abs(value - previousValue);
            boolean isSafeDifference = difference >= 1 && difference <= 3;

            if (!isSafeDifference) {
                if (skip >= 0) {
                    return false;
                }
                return recheckWithIncompatibleValueRemoved(report, i);
            }

            previousValue = value;
        }

        return true;
    }

    private boolean recheckWithIncompatibleValueRemoved(int[] report, int errorIndex) {
        boolean resultWithFirstRemoved = isReportSafeWithProblemsDampend(report, errorIndex - 1);
        if (resultWithFirstRemoved) {
            return true;
        }

        boolean resultWithSecondRemoved = isReportSafeWithProblemsDampend(report, errorIndex);
        if (resultWithSecondRemoved) {
            return true;
        }

        return false;
    }
    */

    enum Direction {
        INCREASING, DECREASING, CONSTANT;

        static Direction from(int first, int second) {
            if (first > second) {
                return INCREASING;
            } else if (first < second) {
                return DECREASING;
            } else {
                return CONSTANT;
            }
        }
    }

    int day2Naive(String input) {
        int amountOfSafeReports = 0;

        for (final var reportText : input.split("\n", -1)) {
            final var report = Arrays.stream(reportText.split("\\s+", -1)).mapToInt(Integer::parseInt).toArray();
            final var isSafe = isReportSafe(report);
            if (isSafe) {
                // System.out.println(Arrays.toString(report));
                amountOfSafeReports++;
            } else {
                for (int i = 0; i < report.length; i++) {
                    final List<Integer> copy = Arrays.stream(report).boxed().collect(Collectors.toCollection(ArrayList::new));
                    copy.remove(i);
                    final var newRep = copy.stream().mapToInt(Integer::intValue).toArray();
                    if (isReportSafe(newRep)) {
                        // System.out.println(Arrays.toString(report));
                        amountOfSafeReports++;
                        break;
                    }
                }
            }
        }

        return amountOfSafeReports;
    }
}
