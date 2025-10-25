package advent2016;

import java.util.stream.Stream;

public class Day18 extends Day {
  protected Day18() {
    super(18);
  }
  static final char SAFE = '.';
  static final char TRAP = '^';

  record Row(String row) {

    boolean trapAt(int index) {
      return index >= 0 && index < row.length() && row.charAt(index) == TRAP;
    }

    // A new tile is a trap only in one of the following situations:
    //
    // - Its left and center tiles are traps, but its right tile is not.
    // - Its center and right tiles are traps, but its left tile is not.
    // - Only its left tile is a trap.
    // - Only its right tile is a trap.
    //
    Row nextRow() {
      char[] next = new char[row.length()];
      for (int i = 0; i < next.length; i++) {
        boolean left = trapAt(i - 1);
        boolean center = trapAt(i);
        boolean right = trapAt(i + 1);
        if ((left && center && !right) ||
            (!left && center && right) ||
            (left && !center && !right) ||
            (!left && !center && right)) {
          next[i] = TRAP;
        } else {
          next[i] = SAFE;
        }
      }
      return new Row(new String(next));
    }

    long countSafe() {
      return row.chars().filter(ch -> ch == SAFE).count();
    }
  }

  @Override
  String part1() {
    long totalSafe = Stream.iterate(new Row(data), Row::nextRow)
        .mapToLong(Row::countSafe)
        .limit(40)
        .sum();
    return String.valueOf(totalSafe);
  }

  @Override
  String part2() {
    long totalSafe = Stream.iterate(new Row(data), Row::nextRow)
        .mapToLong(Row::countSafe)
        .limit(400000)
        .sum();
    return String.valueOf(totalSafe);
  }

  public static void main(String[] args) {
    Day day = new Day18() {
      @Override
      String getData() {
        return ".^^.^.^^^^";
      }
    };
    day.run("185", "1935478");
  }
}
