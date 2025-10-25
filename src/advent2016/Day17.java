package advent2016;

// Two Steps Forward
// #########
// #S| | | #
// #-#-#-#-#
// # | | | #
// #-#-#-#-#
// # | | | #
// #-#-#-#-#
// # | | |
// ####### V

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day17 extends Day {

  static int MIN = 0;
  static int MAX = 4;

  record Pos(int x, int y) {
    static final Pos U = new Pos(0, -1);
    static final Pos R = new Pos(1, 0);
    static final Pos D = new Pos(0, 1);
    static final Pos L = new Pos(-1, 0);
    static Map<Pos, Character> DIR_CHAR = Map.of(U, 'U', R, 'R', D, 'D', L, 'L');
    boolean valid() {
      return MIN <= x && x < MAX && MIN <= y && y < MAX;
    }
    Pos plus(Pos p) {
      return new Pos(x + p.x, y + p.y);
    }
  }
  static final Pos START = new Pos(0, 0);
  static final Pos END = new Pos(3, 3);

  private final MessageDigest md5;

  protected Day17() {
    super(17);
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  String computeHash(String message) {
    md5.reset();
    md5.update(StandardCharsets.UTF_8.encode(message));
    return String.format("%032x", new BigInteger(1, md5.digest()));
  }

  // The doors in your current room are either open or closed (and locked)
  // based on the hexadecimal MD5 hash of a passcode (your puzzle input)
  // followed by a sequence of uppercase characters representing the path
  // you have taken so far (U for up, D for down, L for left, and R for
  // right).
  //
  // Only the first four characters of the hash are used; they represent,
  // respectively, the doors up, down, left, and right from your current
  // position. Any b, c, d, e, or f means that the corresponding door is
  // open; any other character (any number or a) means that the
  // corresponding door is closed and locked.
  static final Pos[] DIRS = {Pos.U, Pos.D, Pos.L, Pos.R};
  static final String OPEN = "bcdef";
  Stream<Pos> moves(String room) {
    var hash = computeHash(room);
    return IntStream.range(0, DIRS.length)
        .filter(i -> OPEN.indexOf(hash.charAt(i)) != -1)
        .mapToObj(i -> DIRS[i]);
  }

  record Path(Pos pos, String path) {
    Path move(Pos dir) {
      return new Path(dir.plus(pos), path + Pos.DIR_CHAR.get(dir));
    }

    boolean valid() {
      return pos.valid();
    }
    boolean atEnd() {
      return pos.equals(END);
    }
  }

  Stream<Path> extendPath(Path path) {
    return moves(path.path).map(path::move).filter(Path::valid);
  }

  List<Path> extendPaths(List<Path> paths) {
    return paths.stream().flatMap(this::extendPath).toList();
  }

  @Override
  String part1() {
    Path start = new Path(START, data);
    Path end =
        Stream.iterate(List.of(start), this::extendPaths)
            .map(paths -> paths.stream().filter(Path::atEnd).toList())
            .dropWhile(List::isEmpty)
            .findFirst().orElseThrow().getFirst();
    return end.path.substring(data.length());
  }

  record Iter(List<Path> paths, List<Path> complete) { }

  @Override
  String part2() {
    Path start = new Path(START, data);
    int longest = Stream.iterate(new Iter(List.of(start), List.of()),
            iter -> !iter.paths.isEmpty(),
            iter -> {
              var partition = iter.paths.stream().collect(Collectors.partitioningBy(Path::atEnd));
              return new Iter(partition.get(false).stream().flatMap(this::extendPath).toList(),
                  Stream.concat(iter.complete.stream(), partition.get(true).stream()).toList());
            })
        .toList().getLast().complete.stream()
        .mapToInt(p -> p.path.length()).max().orElseThrow();
    return String.valueOf(longest - data.length());
  }

  public static void main(String[] args) {
    var day = new Day17() {
      @Override
      String getData() {
        return "ihgpwlah";
      }
    };
    day.run("DDRRRD", "370");
  }
}
