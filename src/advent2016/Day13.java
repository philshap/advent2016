package advent2016;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;

public class Day13 extends Day {
  protected Day13() {
    super(13);
  }

  static final int WALL = -1;
  static final int EMPTY = -2;

  static int input;

  record Pos(int x, int y) {
    int isWall() {
      int val = x * x + 3 * x + 2 * x * y + y + y * y + input;
      String bin = Integer.toBinaryString(val);
      int count = 0;
      for (char c : bin.toCharArray()) {
        if (c == '1') {
          count++;
        }
      }
      return (count % 2 == 0) ? EMPTY : WALL;
    }

    List<Pos> neighbors() {
      List<Pos> neighbors = new ArrayList<>();
      if (x != 0) {
        neighbors.add(new Pos(x - 1, y));
      }
      if (y != 0) {
        neighbors.add(new Pos(x, y - 1));
      }
      neighbors.addAll(List.of(new Pos(x + 1, y), new Pos(x, y + 1)));
      return neighbors;
    }
  }

  record Walk(Pos pos, int distance) {}

  private Map<Pos, Integer> navigateUntil(Function<Walk, Boolean> done) {
    Map<Pos, Integer> floor = new HashMap<>();
    PriorityQueue<Walk> walks = new PriorityQueue<>(Comparator.comparingInt(w -> w.distance));
    Pos start = new Pos(1, 1);
    walks.add(new Walk(start, 0));
    floor.put(start, 0);
    while (!walks.isEmpty()) {
      Walk current = walks.poll();
      if (done.apply(current)) {
        return floor;
      }
      int newDistance = current.distance + 1;
      for (Pos neighbor : current.pos.neighbors()) {
        int space = floor.computeIfAbsent(neighbor, Pos::isWall);
        if (space == EMPTY) {
          floor.put(neighbor, newDistance);
          walks.add(new Walk(neighbor, newDistance));
        }
      }
    }
    return Map.of();
  }

  String part1() {
    input = Integer.parseInt(data);
    var target = new Pos(31, 39);
    var floor = navigateUntil(walk -> walk.pos.equals(target));
    return String.valueOf(floor.get(target));
  }

  String part2() {
    input = Integer.parseInt(data);
    var floor = navigateUntil(walk -> walk.distance == 50);
    long count = floor.values().stream().filter(x -> x != WALL && x != EMPTY).count();
    return String.valueOf(count);
  }
}
