package advent2016;

import java.util.Arrays;

public class Day16 extends Day {
  protected Day16() {
    super(16);
  }

  static class Data {
    final int length;
    final int[] data;
    int size;

    Data(String initial, int length) {
      this.length = length;
      data = new int[length * 2 + 1];
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

    String computeChecksum() {
      while (size < length) {
        expand();
      }
      size = length;
      do {
        checksum();
      } while (size % 2 == 0);

      return Arrays.stream(data).limit(size).mapToObj(i -> (char) i).collect(Support.collectToString());
    }
  }

  String part1() {
    Data d = new Data(data, 272);
    return d.computeChecksum();
  }

  String part2() {
    Data d = new Data(data, 35651584);
    return d.computeChecksum();
  }

  public static void main(String[] args) {
    Day day = new Day16() {
      @Override
      String getData() {
        return "11110010111001001";
      }
    };
    day.run("01110011101111011", "11001111011000111");
  }
}
