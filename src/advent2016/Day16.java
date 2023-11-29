package advent2016;

import java.util.Arrays;
import java.util.function.Function;

public class Day16 implements Day {

    static final int LENGTH = 272;

    @Override
    public void run(Support support) throws Exception {
        var input = support.readString(16);
        part1(input);
        part2(input);
    }

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

    private void part1(String input) {
        Data d = new Data(input);
        while (d.size < LENGTH) {
            d.expand();
        }
        d.size = LENGTH;
        do {
            d.checksum();
        } while (d.size % 2 == 0);
        System.out.printf("day 16 part 1: %s%n", d.print());
    }

    private void part2(String input) {
        System.out.printf("day 16 part 2: %s%n", input);
    }
}
