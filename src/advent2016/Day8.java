package advent2016;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day8 extends Day {
  protected Day8() {
    super(8);
  }

    static final int WIDTH = 50;
    static final int HEIGHT = 6;

    record Pixel(int x, int y) {}

    private void drawRect(Set<Pixel> pixels, String line) {
        String[] wh = line.substring("rect ".length()).split("x");
        int width = Integer.parseInt(wh[0]);
        int height = Integer.parseInt(wh[1]);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels.add(new Pixel(x, y));
            }
        }
    }

    private void rotateColumn(Set<Pixel> pixels, String line) {
        String[] colSize = line.substring("rotate column x= ".length() - 1).split(" by ");
        int x = Integer.parseInt(colSize[0]);
        int size = Integer.parseInt(colSize[1]);
        Set<Pixel> toRemove = new HashSet<>();
        Set<Pixel> toAdd = new HashSet<>();
        for (int y = 0; y < HEIGHT; y++) {
            Pixel old = new Pixel(x, y);
            if (pixels.contains(old)) {
                toRemove.add(old);
                int newY = (y + size) % HEIGHT;
                toAdd.add(new Pixel(x, newY));
            }
        }
        pixels.removeAll(toRemove);
        pixels.addAll(toAdd);
    }

    private void rotateRow(Set<Pixel> pixels, String line) {
        String[] rowSize = line.substring("rotate row y= ".length() - 1).split(" by ");
        int y = Integer.parseInt(rowSize[0]);
        int size = Integer.parseInt(rowSize[1]);
        Set<Pixel> toRemove = new HashSet<>();
        Set<Pixel> toAdd = new HashSet<>();
        for (int x = 0; x < WIDTH; x++) {
            Pixel old = new Pixel(x, y);
            if (pixels.contains(old)) {
                toRemove.add(old);
                int newX = (x + size) % WIDTH;
                toAdd.add(new Pixel(newX, y));
            }
        }
        pixels.removeAll(toRemove);
        pixels.addAll(toAdd);
    }

    private Set<Pixel> drawPixels(List<String> lines) {
        Set<Pixel> pixels = new HashSet<>();
        for (String line : lines) {
            if (line.startsWith("rect")) {
                drawRect(pixels, line);
            } else if (line.startsWith("rotate row")) {
                rotateRow(pixels, line);
            } else {
                rotateColumn(pixels, line);
            }
        }
        return pixels;
    }

    String part1() {
        Set<Pixel> pixels = drawPixels(input);
        return String.valueOf(pixels.size());
    }

    private void display(Set<Pixel> pixels) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (pixels.contains(new Pixel(x, y))) {
                    System.out.print("##");
                } else {
                    System.out.print("..");
                }
            }
            System.out.println();
        }
    }

    String part2() {
        display(drawPixels(input));
        return "";
    }
}
