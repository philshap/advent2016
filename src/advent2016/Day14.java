package advent2016;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Day14 extends Day {
  public Day14() {
    super(14);
  }

  byte[] computeHash(String prefix, int count) {
    return Support.hashToChars(Support.computeHash(prefix + count));
  }

  private byte findTriple(byte[] hash) {
    for (int i = 0; i < 32 - 2; i++) {
      if (hash[i] == hash[i + 1] && hash[i] == hash[i + 2]) {
        return hash[i];
      }
    }
    return -1;
  }

  private boolean findPentad(byte[] hash, byte val) {
    for (int i = 0; i < 32 - 5; i++) {
      if (val == hash[i] && val == hash[i + 1] && val == hash[i + 2] &&
          val == hash[i + 3] && val == hash[i + 4]) {
        return true;
      }
    }
    return false;
  }

  boolean isKey(String prefix, int index, BiFunction<String, Integer, byte[]> hashFunc) {
    byte[] hash = hashFunc.apply(prefix, index);
    byte triple = findTriple(hash);
    if (triple == -1) {
      return false;
    }
    for (int i = 1; i <= 1000; i++) {
      byte[] subhash = hashFunc.apply(prefix, index + i);
      if (findPentad(subhash, triple)) {
        return true;
      }
    }
    return false;
  }

  private int getLastKeyIndex(String prefix, BiFunction<String, Integer, byte[]> hashFunc) {
    int foundKeys = 0;
    int i = 0;
    for (; foundKeys != 64; i++) {
      if (isKey(prefix, i, hashFunc)) {
        foundKeys++;
      }
    }
    return i - 1;
  }

  String part1() {
    int index = getLastKeyIndex(data, this::computeHash);
    return String.valueOf(index);
  }

  static final int MAX_ENTRIES = 10000;

  static final Map<String, byte[]> hash2Cache = new LinkedHashMap<>(MAX_ENTRIES + 1, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
      return size() > MAX_ENTRIES;
    }
  };

  byte[] computeHash2(String prefix, int count) {
    String message = prefix + count;
    return hash2Cache.computeIfAbsent(message, (s) -> {
      byte[] hash = computeHash(prefix, count);
      for (int i = 0; i < 2016; i++) {
        hash = Support.hashToChars(Support.computeHash(hash));
      }
      return hash;
    });
  }

  String part2() {
    int index = getLastKeyIndex(data, this::computeHash2);
    return String.valueOf(index);
  }

  public static void main(String[] args) {
    var day = new Day14() {
      @Override
      String getData() {
        return "abc";
      }
    };
    day.run("22728", "22551");
  }
}
