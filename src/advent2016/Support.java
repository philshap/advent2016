package advent2016;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Support {
  Pattern NUMBER = Pattern.compile("(-?\\d+)");

  static Collector<Character, StringBuilder, String> collectToString() {
    return Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString);
  }

  static <T> Stream<List<T>> partition(List<T> source, int length) {
    return IntStream.range(0, source.size() / length).mapToObj(
        n -> source.subList(n * length, n * length + length));
  }

  static List<String> splitInput(String input) {
    return Arrays.asList(input.split("\n"));
  }

  static List<Integer> integers(String input) {
    return NUMBER.matcher(input).results().map(MatchResult::group).map(Integer::parseInt).toList();
  }

  static List<Long> longs(String input) {
    return NUMBER.matcher(input).results().map(MatchResult::group).map(Long::parseLong).toList();
  }

  enum MD5 {
    SINGLETON;
    private final MessageDigest md5;

    MD5() {
      try {
        md5 = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
    }
    byte[] computeHash(byte[] message) {
      md5.reset();
      md5.update(message);
      return md5.digest();
    }
  }

  static byte[] computeHash(String message) {
    return computeHash(message.getBytes());
  }

  static byte[] computeHash(byte[] message) {
    return MD5.SINGLETON.computeHash(message);
  }

  byte[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  static byte[] hashToChars(byte[] hash) {
    byte[] result = new byte[32];
    for (int i = 0; i < hash.length; i++) {
      result[i * 2] = CHARS[(hash[i] >> 4) & 0xf];
      result[i * 2 + 1] = CHARS[hash[i] & 0xf];
    }
    return result;
  }
}
