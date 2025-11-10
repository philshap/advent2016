package advent2016;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 extends Day {

  protected Day5() {
    super(5);
  }

  // Bytes in the MD5 digest are split into two characters in an MD5 hex string.
  // To check for five leading zeros, we check the first five 4-bit values in the byte array.
  boolean startsWithZeros(byte[] hash) {
    return hash[0] == 0 && hash[1] == 0 && (hash[2] >> 4) == 0;
  }

  static final Map<Integer, Character> INT_TO_CHAR =
      IntStream.range(0, 16).boxed()
          .collect(Collectors.toMap(Function.identity(), i -> Integer.toHexString(i).charAt(0)));

  String part1() {
    return IntStream.iterate(0, i -> i + 1)
        .mapToObj(i -> Support.computeHash(data + i))
        .filter(this::startsWithZeros)
        .map(hash -> INT_TO_CHAR.get(hash[2] & 0xf))
        .limit(8)
        .collect(Support.collectToString());
  }

  String part2() {
    char[] password = new char[8];
    Arrays.fill(password, '.');
    return IntStream.iterate(0, i -> i + 1)
        .mapToObj(i -> Support.computeHash(data + i))
        .filter(this::startsWithZeros)
        .map(hash -> {
          int pos = hash[2] & 0xf;
          if (pos < password.length && password[pos] == '.') {
            password[pos] = INT_TO_CHAR.get((hash[3] >> 4) & 0xf);
          }
          return new String(password);
        }).dropWhile(s -> s.contains("."))
        .findFirst().orElseThrow();
  }

  public static void main(String[] args) {
    var day = new Day5() {
      @Override
      String getData() {
        return "abc";
      }
    };
    day.run("18f47a30", "05ace8e3");
  }
}
