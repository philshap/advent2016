package advent2016;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class Day19 extends Day {
  Day19() {
    super(19);
  }

  @Override
  String part1() {
    int numElves = Integer.parseInt(data);
    Queue<Integer> elves = new LinkedList<>();
    IntStream.range(1, numElves + 1).forEach(elves::add);
    while (elves.size() != 1) {
      elves.add(elves.poll());
      elves.remove();
    }
    return String.valueOf(elves.poll());
  }

  @Override
  String part2() {
    int numElves = Integer.parseInt(data);
    Queue<Integer> low = new LinkedList<>();
    IntStream.range(1, (numElves + 1) / 2).forEach(low::add);
    Queue<Integer> high = new LinkedList<>();
    IntStream.range((numElves + 1) / 2, numElves + 1).forEach(high::add);
    int step = 0;
    while (!low.isEmpty()) {
      high.remove();
      high.add(low.poll());
      if (step++ % 2 == 0) {
        low.add(high.poll());
      }
    }
    return String.valueOf(high.poll());
  }

  public static void main(String[] args) {
    Day day = new Day19() {
      @Override
      String getData() {
        return "5";
      }
    };
    day.run("3", "2");
  }
}
