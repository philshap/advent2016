package advent2016;

import java.util.HashSet;
import java.util.Set;

public class Day1 extends Day {
  protected Day1() {
    super(1);
  }
  enum Dir {
    N(0, 1),
    E(1, 0),
    S(0, -1),
    W(-1, 0);
    final int dx;
    final int dy;

    Dir(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }

    Dir turn(String turn) {
      boolean left = turn.equals("L");
      return switch (this) {
        case N -> left ? W : E;
        case E -> left ? N : S;
        case S -> left ? E : W;
        case W -> left ? S : N;
      };
    }
  }

  record Pos(int x, int y) {
    Pos move(Dir dir, int n) {
      return new Pos(x + (n * dir.dx), y + (n * dir.dy));
    }
  }

  String part1() {
    var pos = new Pos(0, 0);
    var dir = Dir.N;
    for (String s : data.split(", ")) {
      dir = dir.turn(s.substring(0, 1));
      pos = pos.move(dir, Integer.parseInt(s.substring(1)));
    }
    return String.valueOf(Math.abs(pos.x) + Math.abs(pos.y));
  }

  String part2() {
    var pos = new Pos(0, 0);
    var dir = Dir.N;
    Set<Pos> seen = new HashSet<>();
    for (String s : data.split(", ")) {
      dir = dir.turn(s.substring(0, 1));
      for (int i = 0; i < Integer.parseInt(s.substring(1)); i++) {
        pos = pos.move(dir, 1);
        if (!seen.add(pos)) {
          return String.valueOf(Math.abs(pos.x) + Math.abs(pos.y));
        }
      }
    }
    return "no solution found";
  }

  public static void main(String[] args) {
    Day day = new Day1() {
      @Override
      String getData() {
        return "R8, R4, R4, R8";
      }
    };
    day.run("8", "4");
  }
}
