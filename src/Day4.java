import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.stream.Collector;

public class Day4 implements Day {
    @Override
    public void run(Support support) throws Exception {
        var input = support.readLines("day4.input");
        part1(input);
        part2(input);
    }

    static Collector<Character, StringBuilder, String> collectToString() {
        return Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString);
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
                    .collect(collectToString())
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
            return name.chars().mapToObj(this::shift).collect(collectToString());
        }
    }

    private void part1(String[] lines) {
        var sectorSum = Arrays.stream(lines).map(Room::fromLine).filter(Room::isReal).mapToInt(Room::sectorId).sum();
        System.out.printf("day 4 part 1: %s%n", sectorSum);
    }

    private void part2(String[] lines) {
        var poleObjects = Arrays.stream(lines).map(Room::fromLine)
                .filter(Room::isReal)
                .filter(room -> room.decryptName().equals("northpole-object-storage"))
                .findFirst();
        System.out.printf("day 4 part 2: %s%n", poleObjects.orElseThrow().sectorId);
    }
}
