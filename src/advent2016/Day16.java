package advent2016;

public class Day16 extends Day {
  protected Day16() {
    super(16);
  }

  static final byte ZERO = '0';
  static final byte ONE = '1';

  static class Data {
    final int length;
    final byte[] data;
    final int initialLength;

    Data(String initial, int length) {
      this.length = length;
      data = new byte[length * 2 + 1];
      System.arraycopy(initial.getBytes(), 0, data, 0, initial.length());
      initialLength = initial.length();
    }

    int expand(int size) {
      int newSize = size * 2 + 1;
      for (int i = 0; i < size; i++) {
        data[newSize - i - 1] = data[i] == ZERO ? ONE : ZERO;
      }
      data[size] = '0';
      return newSize;
    }

    int checksum(int size) {
      byte[] checksum = new byte[size];
      int j = 0;
      for (int i = 0; i < size - 1; i += 2) {
        checksum[j++] = data[i] == data[i + 1] ? ONE : ZERO;
      }
      System.arraycopy(checksum, 0, data, 0, j);
      return j;
    }

    String computeChecksum() {
      int size = initialLength;
      while (size < length) {
        size = expand(size);
      }
      size = length;
      do {
        size = checksum(size);
      } while (size % 2 == 0);

      return new String(data, 0, size);
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
