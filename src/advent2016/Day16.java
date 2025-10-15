package advent2016;

import java.util.Arrays;

public class Day16 extends Day {
  protected Day16() {
    super(16);
  }

  static final int LENGTH = 272;

  static class Data {
    final int[] data = new int[LENGTH * 2 + 1];
    int size;

    Data(String initial) {
      int i = 0;
      for (byte b : initial.getBytes()) {
        data[i++] = b;
      }
      size = initial.length();
    }

    void expand() {
      int newSize = size * 2 + 1;
      for (int i = 0; i < size; i++) {
        data[newSize - i - 1] = data[i] == '0' ? '1' : '0';
      }
      data[size] = '0';
      size = newSize;
    }

    void checksum() {
      int[] checksum = new int[size];
      int j = 0;
      for (int i = 0; i < size - 1; i += 2) {
        checksum[j++] = data[i] == data[i + 1] ? '1' : '0';
      }
      size = j;
      System.arraycopy(checksum, 0, data, 0, j);
    }

    String print() {
      String s = Arrays.stream(data).mapToObj(i -> (char) i).collect(Support.collectToString());
      return s.substring(0, size);
    }
  }

  String part1() {
    Data d = new Data(data);
    while (d.size < LENGTH) {
      d.expand();
    }
    d.size = LENGTH;
    do {
      d.checksum();
    } while (d.size % 2 == 0);
    return String.valueOf(d.print());
  }

  String part2() {
    return "???";
  }
}
