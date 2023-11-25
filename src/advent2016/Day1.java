package advent2016;

import java.util.HashSet;
import java.util.Set;

public class Day1 implements Day {
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

    public void run(Support support) throws Exception {
        var input = support.readString(1);
        part1(input);
        part2(input);
    }

    private static void part1(String input) {
        var pos = new Pos(0, 0);
        var dir = Dir.N;
        for (String s : input.split(", ")) {
            dir = dir.turn(s.substring(0, 1));
            pos = pos.move(dir, Integer.parseInt(s.substring(1)));
        }
        System.out.printf("day 1 part 1: %s%n", Math.abs(pos.x) + Math.abs(pos.y));
    }

    private static void part2(String input) {
        var pos = new Pos(0, 0);
        var dir = Dir.N;
        Set<Pos> seen = new HashSet<>();
        for (String s : input.split(", ")) {
            dir = dir.turn(s.substring(0, 1));
            for (int i = 0; i < Integer.parseInt(s.substring(1)); i++) {
                pos = pos.move(dir, 1);
                if (!seen.add(pos)) {
                    System.out.printf("day 1 part 2: %s%n", Math.abs(pos.x) + Math.abs(pos.y));
                    return;
                }
            }
        }
        System.out.println("day 1 part 2: no solution found");
    }
}
