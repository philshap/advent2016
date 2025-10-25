package advent2016;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 extends Day {
  protected Day6() {
    super(6);
  }

  private static String readMessage(List<String> lines, Comparator<Map.Entry<Character, Integer>> comparator) {
    List<Map<Character, Integer>> frequencies = new ArrayList<>();
    for (int i = 0; i < lines.getFirst().length(); i++) {
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

  String part1() {
    return readMessage(input, Map.Entry.<Character, Integer>comparingByValue().reversed());
  }

  String part2() {
    return readMessage(input, Map.Entry.comparingByValue());
  }

  public static void main(String[] args) {
    var day = new Day6() {
      @Override
      String getData() {
        return """
            eedadn
            drvtee
            eandsr
            raavrd
            atevrs
            tsrnev
            sdttsa
            rasrtv
            nssdts
            ntnada
            svetve
            tesnvt
            vntsnd
            vrdear
            dvrsen
            enarar""";
      }
    };
    day.run("easter", "advent");
  }
}
