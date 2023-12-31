package advent2016;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day4 implements Day {
    @Override
    public void run(Support support) throws Exception {
        var input = support.readLines(4);
        part1(input);
        part2(input);
    }

    record Room(String name, int sectorId, String checksum) {
        static final Pattern PATTERN = Pattern.compile("([a-z-]+)-(\\d+)\\[([a-z]{5})]");

        static Room fromLine(String line) {
            var match = PATTERN.matcher(line);
            if (!match.matches()) {
                throw new RuntimeException("invalid room: " + line);
            }
            return new Room(match.group(1), Integer.parseInt(match.group(2)), match.group(3));
        }

        String computeChecksum() {
            Map<Character, Integer> frequencies = new HashMap<>();
            for (char ch : name.toCharArray()) {
                frequencies.merge(ch, 1, Integer::sum);
            }
            return frequencies.entrySet().stream()
                    .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed().thenComparing(Map.Entry::getKey))
                    .map(Map.Entry::getKey)
                    .filter(c -> c != '-')
                    .collect(Support.collectToString())
                    .substring(0, 5);
        }

        boolean isReal() {
            return computeChecksum().equals(checksum);
        }

        char shift(int c) {
            if (c == '-') {
                return '-';
            }
            return (char) ('a' + ((c - 'a') + sectorId % 26) % 26);
        }

        String decryptName() {
            return name.chars().mapToObj(this::shift).collect(Support.collectToString());
        }
    }

    private void part1(List<String> lines) {
        var sectorSum = lines.stream().map(Room::fromLine).filter(Room::isReal).mapToInt(Room::sectorId).sum();
        System.out.printf("day 4 part 1: %s%n", sectorSum);
    }

    private void part2(List<String> lines) {
        var poleObjects = lines.stream().map(Room::fromLine)
                .filter(Room::isReal)
                .filter(room -> room.decryptName().equals("northpole-object-storage"))
                .findFirst();
        System.out.printf("day 4 part 2: %s%n", poleObjects.orElseThrow().sectorId);
    }
}
