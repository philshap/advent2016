package advent2016;

import java.util.Arrays;
import java.util.List;

public class Day21 extends Day {
  String start1 = "abcdefgh";
  String start2 = "fbgdceah";

  Day21() {
    super(21);
  }

  static void swap(char[] password, int x, int y) {
    char swap = password[x];
    password[x] = password[y];
    password[y] = swap;
  }

  static int indexOf(char[] password, char ch) {
    for (int i = 0; i < password.length; i++) {
      if (password[i] == ch) {
        return i;
      }
    }
    return -1;
  }

  static void rotate(char[] password, int offset) {
    char[] orig = Arrays.copyOf(password, password.length);
    for (int i = 0; i < password.length; i++) {
      int toOffset = i + offset;
      if (toOffset < 0) {
        toOffset += password.length;
      }
      password[toOffset % password.length] = orig[i];
    }
  }

  static void movePosition(char[] password, int from, int to) {
    char orig = password[from];
    System.arraycopy(password, from + 1, password, from, password.length - 1 - from);
    System.arraycopy(password, to, password, to + 1, password.length - 1 - to);
    password[to] = orig;
  }

  enum Op {
    SWAP_POS("swap position") {
      @Override
      // "swap position X with position Y"
      // means that the letters at indexes X and Y (counting from 0) should be swapped.
      void scramble(String operation, char[] password) {
        List<Integer> ints = Support.integers(operation);
        swap(password, ints.getFirst(), ints.getLast());
      }
    },
    // "swap letter X with letter Y"
    // means that the letters X and Y should be swapped (regardless of where they appear in the string).
    SWAP_LETTER("swap letter") {
      @Override
      void scramble(String operation, char[] password) {
        String[] split = operation.split(" ");
        swap(password, indexOf(password, split[2].charAt(0)), indexOf(password, split[5].charAt(0)));
      }
    },
    // "rotate left/right X steps"
    // means that the whole string should be rotated; for example, one right rotation would turn abcd into dabc.
    ROTATE_LEFT("rotate left") {
      @Override
      void scramble(String operation, char[] password) {
        int offset = Support.integers(operation).getFirst();
        rotate(password, -offset);
      }

      @Override
      void unscramble(String operation, char[] password) {
        ROTATE_RIGHT.scramble(operation, password);
      }
    },
    ROTATE_RIGHT("rotate right") {
      @Override
      void scramble(String operation, char[] password) {
        int offset = Support.integers(operation).getFirst();
        rotate(password, offset);
      }

      @Override
      void unscramble(String operation, char[] password) {
        ROTATE_LEFT.scramble(operation, password);
      }
    },
    // "rotate based on position of letter X"
    // means that the whole string should be rotated to the right based on the index of letter X
    // (counting from 0) as determined before this instruction does any rotations. Once the index
    // is determined, rotate the string to the right one time, plus a number of times equal to
    // that index, plus one additional time if the index was at least 4.
    ROTATE_BASED("rotate based") {
      @Override
      void scramble(String operation, char[] password) {
        int index = indexOf(password, operation.split(" ")[6].charAt(0));
        int offset = 1 + index + (index >= 4 ? 1 : 0);
        rotate(password, offset);
      }

      @Override
      void unscramble(String operation, char[] password) {
        for (int i = 1; i < password.length; i++) {
          char[] guess = Arrays.copyOf(password, password.length);
          rotate(guess, -i);
          char[] test = Arrays.copyOf(guess, guess.length);
          ROTATE_BASED.scramble(operation, test);
          if (Arrays.equals(test, password)) {
            System.arraycopy(guess, 0, password, 0, guess.length);
            return;
          }
        }
      }
    },
    // "reverse positions X through Y" means that the span of letters at indexes X through Y
    // (including the letters at X and Y) should be reversed in order.
    REVERSE_POS("reverse positions") {
      @Override
      void scramble(String operation, char[] password) {
        List<Integer> xy = Support.integers(operation);
        int x = xy.getFirst();
        int y = xy.getLast();
        for (int i = 0; i < (y - x) / 2 + 1; i++) {
          swap(password, i + x, y - i);
        }
      }
    },
    // "move position X to position Y" means that the letter which is at index X should be
    // removed from the string, then inserted such that it ends up at index Y.
    MOVE_POS("move position") {
      @Override
      void scramble(String operation, char[] password) {
        List<Integer> xy = Support.integers(operation);
        int x = xy.getFirst();
        int y = xy.getLast();
        movePosition(password, x, y);
      }

      @Override
      void unscramble(String operation, char[] password) {
        List<Integer> xy = Support.integers(operation);
        int x = xy.getFirst();
        int y = xy.getLast();
        movePosition(password, y, x);
      }
    };
    final String prefix;
    Op(String prefix) {
      this.prefix = prefix;
    }

    abstract void scramble(String operation, char[] password);
    void unscramble(String operation, char[] password) {
      scramble(operation, password);
    }

    static Op fromLine(String operation) {
      return Arrays.stream(values()).filter(o -> operation.startsWith(o.prefix)).findFirst().orElseThrow();
    }
  }

  void scramble(String operation, char[] password) {
    Op.fromLine(operation).scramble(operation, password);
  }

  @Override
  String part1() {
    char[] password = start1.toCharArray();
    for (String operation : input) {
      scramble(operation, password);
    }
    return new String(password);
  }

  void unscramble(String operation, char[] password) {
    Op.fromLine(operation).unscramble(operation, password);
  }

  @Override
  String part2() {
    char[] password = start2.toCharArray();
    for (String operation : input.reversed()) {
      unscramble(operation, password);
    }
    return new String(password);
  }

  public static void main(String[] args) {
    Day21 day = new Day21() {
      @Override
      String getData() {
        return """
            swap position 4 with position 0
            swap letter d with letter b
            reverse positions 0 through 4
            rotate left 1 step
            move position 1 to position 4
            move position 3 to position 0
            rotate based on position of letter b
            rotate based on position of letter d
            """;
      }
    };
    day.start1 = "abcde";
    day.start2 = "decab";
    day.run("decab", "abcde");
  }
}
