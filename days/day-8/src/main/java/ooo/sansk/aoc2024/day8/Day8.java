package ooo.sansk.aoc2024.day8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class Day8 {
    int runPartOne(String input) {
        final var area = scanArea(input);

        final var uniqueLocations = area.antiNodes()
                .stream()
                .map(Antenna::location)
                .filter(area::isPointInArea)
                .collect(Collectors.toSet());

        printMap(area, uniqueLocations);

        return uniqueLocations.size();
    }

    private void printMap(Area area, Set<Vec2d> antiNodes) {
        for (int y = 0; y < area.height; y++) {
            for (int x = 0; x < area.width; x++) {
                final int finalY = y;
                final int finalX = x;
                char c = '.';
                if (antiNodes.stream().anyMatch(loc -> loc.equals(new Vec2d(finalX, finalY)))) {
                    c = '#';
                }

                final var optionalFreq = area.antennas().stream()
                        .filter(antenna -> antenna.location().equals(new Vec2d(finalX, finalY)))
                        .map(Antenna::frequency)
                        .findAny();

                if (optionalFreq.isPresent()) {
                    c = optionalFreq.get();
                }

                System.out.print(c);
            }
            System.out.println();
        }
    }

    int runPartTwo(String input) {
        final var area = scanArea(input);

        final var uniqueLocations = area.resonantAntiNodes()
                .stream()
                .map(Antenna::location)
                .filter(area::isPointInArea)
                .collect(Collectors.toSet());

        printMap(area, uniqueLocations);

        return uniqueLocations.size();
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
                    antennas.add(new Antenna(frequency, new Vec2d(x, y)));
                }
            }
        }

        return new Area(width, height, antennas);
    }

    record Area(int width, int height, List<Antenna> antennas) {
        List<Antenna> antiNodes() {
            final var groups = groupAntennasByFrequency();
            final var antiNodes = new ArrayList<Antenna>();

            for (final var antennaMap : groups.entrySet()) {
                final var antennas = antennaMap.getValue();
                for (int i = 0; i < antennas.size(); i++) {
                    final var firstAntenna = antennas.get(i);
                    for (int j = i + 1; j < antennas.size(); j++) {
                        final var secondAntenna = antennas.get(j);
                        antiNodes.addAll(firstAntenna.calculateAntiNodes(secondAntenna));
                    }
                }
            }

            return antiNodes;
        }

        List<Antenna> resonantAntiNodes() {
            final var groups = groupAntennasByFrequency();
            final var antiNodes = new ArrayList<Antenna>();

            for (final var antennaMap : groups.entrySet()) {
                final var antennas = antennaMap.getValue();
                for (int i = 0; i < antennas.size(); i++) {
                    final var firstAntenna = antennas.get(i);
                    for (int j = i + 1; j < antennas.size(); j++) {
                        final var secondAntenna = antennas.get(j);
                        antiNodes.addAll(firstAntenna.calculateResonantAntiNodes(secondAntenna, this));
                    }
                }
            }

            return antiNodes;
        }

        Map<Character, List<Antenna>> groupAntennasByFrequency() {
            return this.antennas.stream()
                    .reduce(new HashMap<>(), (map, antenna) -> {
                        map.computeIfAbsent(antenna.frequency(), k -> new ArrayList<>()).add(antenna);
                        return map;
                    }, (first, second) -> {
                        for (final var antennaMap : first.entrySet()) {
                            second.computeIfAbsent(antennaMap.getKey(), k -> new ArrayList<>()).addAll(antennaMap.getValue());
                        }
                        return second;
                    });
        }

        boolean isPointInArea(Vec2d vec2d) {
            if (vec2d.x < 0 || vec2d.y < 0 || vec2d.x >= width || vec2d.y >= height) {
                return false;
            }
            return true;
        }
    }

    record Vec2d(int x, int y) {
        Vec2d difference(Vec2d vec2d) {
            return new Vec2d(x() - vec2d.x(), y() - vec2d.y());
        }

        Vec2d add(Vec2d vec2d) {
            return new Vec2d(x() + vec2d.x(), y() + vec2d.y());
        }

        Vec2d subtract(Vec2d vec2d) {
            return new Vec2d(x() - vec2d.x(), y() - vec2d.y());
        }
    }

    record Antenna(char frequency, Vec2d location) {
        List<Antenna> calculateAntiNodes(Antenna interfering) {
            final var difference = this.location.difference(interfering.location());

            return List.of(
                    new Antenna(frequency(), location().add(difference)),
                    new Antenna(interfering.frequency(), interfering.location().subtract(difference))
            );
        }

        List<Antenna> calculateResonantAntiNodes(Antenna interfering, Area area) {
            final var difference = this.location.difference(interfering.location());
            final var antiNodes = new ArrayList<Antenna>();
            antiNodes.add(this);
            antiNodes.add(interfering);

            Antenna increasingAntinodes = this;
            while (area.isPointInArea(increasingAntinodes.location())) {
                increasingAntinodes = new Antenna(frequency(), increasingAntinodes.location().add(difference));
                antiNodes.add(increasingAntinodes);
            }

            Antenna decreasingAntinode = interfering;
            while (area.isPointInArea(decreasingAntinode.location())) {
                decreasingAntinode = new Antenna(frequency(), decreasingAntinode.location().subtract(difference));
                antiNodes.add(decreasingAntinode);
            }

            return antiNodes;
        }
    }
}
