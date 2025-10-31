package advent2016;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day20 extends Day {
  Day20() {
    super(20);
  }

  record Range(long low, long high) {
    static Range fromLine(String line) {
      String[] split = line.split("-");
      return new Range(Long.parseLong(split[0]), Long.parseLong(split[1]));
    }
    Range merge(Range other) {
      // disjoint
      if ((high + 1) < other.low || (other.high + 1) < low) {
        return null;
      }
      // fully contained
      if (low >= other.low && high <= other.high) {
        return other;
      }
      if (other.low >= low && other.high <= high) {
        return this;
      }
      // merge
      return new Range(Math.min(low, other.low), Math.max(high, other.high));
    }
  }

  record Merge(List<Range> merged, Range merge) {}

  Merge findMerge(List<Range> ranges) {
    for (int i = 0; i < ranges.size(); i++) {
      for (int j = i + 1; j < ranges.size(); j++) {
        var merged = ranges.get(i).merge(ranges.get(j));
        if (merged != null) {
          return new Merge(List.of(ranges.get(i), ranges.get(j)), merged);
        }
      }
    }
    return null;
  }

  List<Range> mergeRanges() {
    List<Range> ranges = input.stream().map(Range::fromLine).collect(Collectors.toList());
    Merge merge;
    while ((merge = findMerge(ranges)) != null) {
      ranges.removeAll(merge.merged);
      ranges.add(merge.merge);
    }
    ranges.sort(Comparator.comparing(Range::low));
    return ranges;
  }

  @Override
  String part1() {
    // Assuming that 0 is not an answer here.
    return String.valueOf(mergeRanges().getFirst().high + 1);
  }

  @Override
  String part2() {
    List<Range> ranges = mergeRanges();
    long total = 0;
    long prevHigh = ranges.getFirst().low - 1;
    for (Range range : ranges) {
      total += range.low - prevHigh - 1;
      prevHigh = range.high;
    }
    // Assuming that prevHigh is always the top.
    return String.valueOf(total);
  }

  public static void main(String[] args) {
    Day day = new Day20() {
      @Override
      String getData() {
        return """
            5-8
            0-2
            4-7""";
      }
    };
    day.run("3", "1");
  }
}
