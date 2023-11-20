import java.util.List;
import java.util.Map;

public class Day2 implements Day {
    record Dir(int dx, int dy) {
    }

    static final Map<Character, Dir> DIRS =
            Map.of('U', new Dir(0, -1),
                    'R', new Dir(1, 0),
                    'D', new Dir(0, 1),
                    'L', new Dir(-1, 0));

    static final int[][] KEYPAD1 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };

    record Pos1(int x, int y) {

        static int pin(int val) {
            return Math.min(Math.max(val, 0), 2);
        }

        Pos1 move(Dir dir) {
            return new Pos1(pin(x + dir.dx), pin(y + dir.dy));
        }

        int digit() {
            return KEYPAD1[y][x];
        }
    }

    public void run(Support support) throws Exception {
        var input = support.readLines("day2.input");
        part1(input);
        part2(input);
    }

    private static void part1(List<String> lines) {
        StringBuilder digits = new StringBuilder();
        Pos1 pos = new Pos1(1, 1);
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                pos = pos.move(DIRS.get(c));
            }
            digits.append(pos.digit());
        }
        System.out.printf("day 2 part 1: %s%n", digits);
    }

    static final char[][] KEYPAD2 = {
            {0, 0, '1', 0, 0},
            {0, '2', '3', '4', 0},
            {'5', '6', '7', '8', '9'},
            {0, 'A', 'B', 'C', 0},
            {0, 0, 'D', 0, 0}
    };

    record Pos2(int x, int y) {

        static int pin(int val) {
            return Math.min(Math.max(val, 0), 4);
        }

        Pos2 move(Dir dir) {
            Pos2 newPos = new Pos2(pin(x + dir.dx), pin(y + dir.dy));
            if (newPos.digit() != 0) {
                return newPos;
            }
            return this;
        }

        char digit() {
            return KEYPAD2[y][x];
        }
    }

    private static void part2(List<String> lines) {
        StringBuilder digits = new StringBuilder();
        Pos2 pos = new Pos2(0, 2);
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                pos = pos.move(DIRS.get(c));
            }
            digits.append(pos.digit());
        }
        System.out.printf("day 2 part 2: %s%n", digits);
    }
}
