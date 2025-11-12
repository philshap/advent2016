package advent2016;

import java.util.Arrays;
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

  String part1() {
    return IntStream.iterate(0, i -> i + 1)
        .mapToObj(i -> Support.computeHash(data + i))
        .filter(this::startsWithZeros)
        .map(hash -> (char) Support.CHARS[hash[2] & 0xf])
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
            password[pos] = (char) Support.CHARS[(hash[3] >> 4) & 0xf];
          }
          return new String(password);
        })
        .dropWhile(s -> s.contains("."))
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
