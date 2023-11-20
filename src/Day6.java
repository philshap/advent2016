import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Day6 implements Day {
    @Override
    public void run(Support support) throws Exception {
        var lines = support.readLines("day6.input");
        part1(lines);
        part2(lines);
    }

    private static String readMessage(String[] lines, Comparator<Map.Entry<Character, Integer>> comparator) {
        Map<Character, Integer>[] frequencies = new Map[lines[0].length()];
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = new HashMap<>();
        }
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                frequencies[i].merge(line.charAt(i), 1, Integer::sum);
            }
        }
        return Arrays.stream(frequencies)
                .map(freq -> freq.entrySet().stream()
                        .sorted(comparator)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElseThrow())
                .collect(Support.collectToString());
    }

    private void part1(String[] lines) {
        var message = readMessage(lines, Map.Entry.<Character, Integer>comparingByValue().reversed());
        System.out.printf("day 6 part 1: %s%n", message);
    }

    private void part2(String[] lines) {
        var message = readMessage(lines, Map.Entry.comparingByValue());
        System.out.printf("day 6 part 2: %s%n", message);
    }
}
