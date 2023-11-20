import java.util.*;

public class Day6 implements Day {
    @Override
    public void run(Support support) throws Exception {
        var lines = support.readLines("day6.input");
        part1(lines);
        part2(lines);
    }

    private static String readMessage(List<String> lines, Comparator<Map.Entry<Character, Integer>> comparator) {
        List<Map<Character, Integer>> frequencies = new ArrayList<>();
        for (int i = 0; i < lines.get(0).length(); i++) {
            frequencies.add(new HashMap<>());
        }
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                frequencies.get(i).merge(line.charAt(i), 1, Integer::sum);
            }
        }
        return frequencies.stream()
                .map(freq -> freq.entrySet().stream()
                        .sorted(comparator)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElseThrow())
                .collect(Support.collectToString());
    }

    private void part1(List<String> lines) {
        var message = readMessage(lines, Map.Entry.<Character, Integer>comparingByValue().reversed());
        System.out.printf("day 6 part 1: %s%n", message);
    }

    private void part2(List<String> lines) {
        var message = readMessage(lines, Map.Entry.comparingByValue());
        System.out.printf("day 6 part 2: %s%n", message);
    }
}
