package advent2016;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day3 extends Day {
  protected Day3() {
    super(3);
  }

  record Triangle(List<Integer> sides) {
    Triangle(String line) {
      this(Arrays.stream(line.split(" "))
          .filter(Predicate.not(String::isEmpty))
          .map(Integer::parseInt)
          .toList());
    }

    boolean isPossible() {
      int allSides = sides.stream().mapToInt(x -> x).sum();
      int longSide = sides.stream().mapToInt(x -> x).max().orElseThrow();
      return (allSides - longSide) > longSide;
    }
  }

  String part1() {
    var possible = input.stream().map(Triangle::new).filter(Triangle::isPossible).count();
    return String.valueOf(possible);
  }

  String part2() {
    var triangles =
        input.stream()
            .map(Triangle::new)
            .toList();
    var possible = Support.partition(triangles, 3)
        .flatMap(t -> Stream.of(List.of(t.get(0).sides.get(0), t.get(1).sides.get(0), t.get(2).sides.get(0)),
            List.of(t.get(0).sides.get(1), t.get(1).sides.get(1), t.get(2).sides.get(1)),
            List.of(t.get(0).sides.get(2), t.get(1).sides.get(2), t.get(2).sides.get(2))))
        .map(Triangle::new)
        .filter(Triangle::isPossible).count();
    return String.valueOf(possible);
  }
}
