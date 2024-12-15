package ooo.sansk.aoc2024.day4;

import java.util.Locale;

class Day4 {
    private static final String SEARCH_STRING = "XMAS";

    int runPartOne(String input) {
        return searchInputPart1(input, SEARCH_STRING);
    }

    int searchInputPart1(String input, String searchString) {
        int width = input.indexOf("\r");
        if (width == -1) {
            width = input.indexOf("\n");
        }

        if (width == -1) {
            throw new IllegalArgumentException("Invalid input: " + input);
        }

        final var characters = input.replaceAll("\r?\n", "").toCharArray();

        int found = 0;
        for (int i = 0; i < characters.length; i++) {
            found += searchInDirectionForWord(searchString, Direction.NORTH, characters, width, i, 0) ? 1 : 0;
            found += searchInDirectionForWord(searchString, Direction.NORTH_EAST, characters, width, i, 0) ? 1 : 0;
            found += searchInDirectionForWord(searchString, Direction.EAST, characters, width, i, 0) ? 1 : 0;
            found += searchInDirectionForWord(searchString, Direction.SOUTH_EAST, characters, width, i, 0) ? 1 : 0;
            found += searchInDirectionForWord(searchString, Direction.SOUTH, characters, width, i, 0) ? 1 : 0;
            found += searchInDirectionForWord(searchString, Direction.SOUTH_WEST, characters, width, i, 0) ? 1 : 0;
            found += searchInDirectionForWord(searchString, Direction.WEST, characters, width, i, 0) ? 1 : 0;
            found += searchInDirectionForWord(searchString, Direction.NORTH_WEST, characters, width, i, 0) ? 1 : 0;
        }

        return found;
    }

    private boolean searchInDirectionForWord(String searchString, Direction direction, char[] characters, int width, int currentIndex, int searchOffset) {
        final var current = Character.toLowerCase(characters[currentIndex]);

        if (current == searchString.toLowerCase(Locale.ROOT).charAt(searchOffset)) {
            if (searchOffset == searchString.length() - 1) {
                return true;
            }

            final var nextIndex = direction.calculateNext(currentIndex, width, characters.length);
            if (nextIndex == null) {
                return false;
            }

            return searchInDirectionForWord(searchString, direction, characters, width, nextIndex, searchOffset + 1);
        }
        return false;
    }

    int runPartTwo(String input) {
        int width = input.indexOf("\r");
        if (width == -1) {
            width = input.indexOf("\n");
        }

        if (width == -1) {
            throw new IllegalArgumentException("Invalid input: " + input);
        }

        final var characters = input.replaceAll("\r?\n", "").toCharArray();

        int found = 0;
        for (int i = 0; i < characters.length; i++) {
            char c = characters[i];
            if (c == '\n' && width == 0) {
                width = i;
            }

            if (Character.toLowerCase(c) != 'a') {
                continue;
            }

            final var leftTopIndex = Direction.NORTH_WEST.calculateNext(i, width, characters.length);
            final var rightBottomIndex = Direction.SOUTH_EAST.calculateNext(i, width, characters.length);
            final var leftBottomIndex = Direction.SOUTH_WEST.calculateNext(i, width, characters.length);
            final var rightTopIndex = Direction.NORTH_EAST.calculateNext(i, width, characters.length);
            if (leftTopIndex == null || rightBottomIndex == null || leftBottomIndex == null || rightTopIndex == null) {
                continue;
            }

            final var leftTopCharacter = Character.toLowerCase(characters[leftTopIndex]);
            final var rightBottomCharacter = Character.toLowerCase(characters[rightBottomIndex]);
            final var leftBottomCharacter = Character.toLowerCase(characters[leftBottomIndex]);
            final var rightTopCharacter = Character.toLowerCase(characters[rightTopIndex]);

            if (leftTopCharacter != 'm' && leftTopCharacter != 's') {
                continue;
            }

            if (leftBottomCharacter != 'm' && leftBottomCharacter != 's') {
                continue;
            }

            final var rightBottomValid = rightBottomCharacter == (leftTopCharacter == 'm' ? 's' : 'm');
            final var rightTopValid = rightTopCharacter == (leftBottomCharacter == 'm' ? 's' : 'm');

            if (rightBottomValid && rightTopValid) {
                found++;
            }
        }

        return found;
    }
}
