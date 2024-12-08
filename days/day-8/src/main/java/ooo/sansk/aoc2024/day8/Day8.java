package ooo.sansk.aoc2024.day8;

import java.util.ArrayList;
import java.util.List;

class Day8 {
    int runPartOne(String input) {
        final var area = scanArea(input);

        return area.antennas().size();
    }

    int runPartTwo(String input) {
        return 0;
    }

    Area scanArea(String input) {


        final var antennas = new ArrayList<Antenna>();

        final var lines = input.split("\\r?\\n", -1);

        int width = 0;
        int height = lines.length;
        for (int y = 0; y < lines.length; y++) {
            final var line = lines[y];
            width = line.length();
            for (int x = 0; x < line.length(); x++) {
                final var frequency = line.charAt(x);
                if (frequency != '.') {
                    antennas.add(new Antenna(frequency, new Point(x, y)));
                }
            }
        }

        return new Area(width, height, antennas);
    }

    record Area(int width, int height, List<Antenna> antennas) {

    }

    record Point(int x, int y) {
    }

    record Antenna(char frequency, Point location) {
    }
}
