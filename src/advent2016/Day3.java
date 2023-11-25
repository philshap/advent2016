package advent2016;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day3 implements Day {
    @Override
    public void run(Support support) throws Exception {
        var lines = support.readLines(3);
        part1(lines);
        part2(lines);
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

    private void part1(List<String> lines) {
        var possible = lines.stream().map(Triangle::new).filter(Triangle::isPossible).count();
        System.out.printf("day 3 part 1: %s%n", possible);
    }

    private void part2(List<String> lines) {
        var triangles =
                lines.stream()
                        .map(Triangle::new)
                        .toList();
        var possible = Support.partition(triangles, 3)
                .flatMap(t -> Stream.of(List.of(t.get(0).sides.get(0), t.get(1).sides.get(0), t.get(2).sides.get(0)),
                        List.of(t.get(0).sides.get(1), t.get(1).sides.get(1), t.get(2).sides.get(1)),
                        List.of(t.get(0).sides.get(2), t.get(1).sides.get(2), t.get(2).sides.get(2))))
                .map(Triangle::new)
                .filter(Triangle::isPossible).count();
        System.out.printf("day 3 part 2: %s%n", possible);
    }
}

