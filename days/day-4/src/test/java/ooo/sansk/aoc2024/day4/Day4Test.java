package ooo.sansk.aoc2024.day4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day4Test {
    private Day4 subject;

    @BeforeEach
    void setup() {
        subject = new Day4();
    }

    @Test
    void part1_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(18, result);
    }

    @Test
    void runPartOne() throws Exception {
        final var result = subject.runPartOne(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("This crossword contains %d times the word 'XMAS'".formatted(result));
    }

    @Test
    void part2_WithExampleInput_ReturnsSameResultAsExample() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/test-input").readAllBytes(), StandardCharsets.UTF_8));

        assertEquals(9, result);
    }

    @Test
    void runPartTwo() throws Exception {
        final var result = subject.runPartTwo(new String(getClass().getResourceAsStream("/puzzle-input").readAllBytes(), StandardCharsets.UTF_8));

        System.out.println("This crossword contains %d times an 'X-MAS'".formatted(result));
    }

    @Test
    void testDirection_North() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "EB");

        assertEquals(1, result);
    }

    @Test
    void testDirection_NorthEast() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "EC");

        assertEquals(1, result);
    }

    @Test
    void testDirection_East() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "EF");

        assertEquals(1, result);
    }

    @Test
    void testDirection_SouthEast() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "EI");

        assertEquals(1, result);
    }

    @Test
    void testDirection_South() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "EH");

        assertEquals(1, result);
    }

    @Test
    void testDirection_SouthWest() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "EG");

        assertEquals(1, result);
    }

    /**
     * 012
     * 345
     * 678
     * @param direction
     * @param expected
     */
    @ParameterizedTest
    @CsvSource({
            "NORTH,1",
            "NORTH_EAST,2",
            "EAST,5",
            "SOUTH_EAST,8",
            "SOUTH,7",
            "SOUTH_WEST,6",
            "WEST,3",
            "NORTH_WEST,0",
    })
    void testAllDirections(Day4.Direction direction, int expected) {
        final var result = direction.getNext().calculate(4, 3, 9);

        assertEquals(expected, result);
    }

    /**
     * 012
     * 345
     * 678
     * @param direction
     * @param expected
     */
    @ParameterizedTest
    @CsvSource({
            "NORTH,1",
            "NORTH_EAST,2",
            "EAST,5",
            "SOUTH_EAST,8",
            "SOUTH,7",
            "SOUTH_WEST,6",
            "WEST,3",
            "NORTH_WEST,0",
    })
    void testAllDirectionsWithBoundsChecks(Day4.Direction direction, int expected) {
        final var result = direction.calculateNext(4, 3, 8);

        assertEquals(expected, result);
    }

    @Test
    void testDirection_West() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "ED");

        assertEquals(1, result);
    }

    @Test
    void testDirection_NorthWest() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "EA");

        assertEquals(1, result);
    }

    @Test
    void testDirection_Wrapping() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "IG");

        assertEquals(0, result);
    }

    @Test
    void testDirection_StartOutOfBounds() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "AI");

        assertEquals(0, result);
    }

    @Test
    void testDirection_StartAtLeftEdgeBounds() {
        String input = """
                ABC
                DEF
                GHI\
                """;

        final var result = subject.searchInputPart1(input, "DC");

        assertEquals(0, result);
    }
}
