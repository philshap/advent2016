package advent2016;

import java.util.ArrayList;
import java.util.List;

public class Day15 extends Day {
  Day15() {
    super(15);
  }

  record Disk(int current, int total) {
    Disk tick() {
      int newCurrent = current + 1;
      return new Disk(newCurrent == total ? 0 : newCurrent, total);
    }
  }

  record Sculpture(List<Disk> disks) {
    static Sculpture fromData(String data) {
      // Disc #1 has 5 positions; at time=0, it is at position 4.
      return new Sculpture(Support.partition(Support.integers(data), 4)
          .map(list -> new Disk(list.get(3), list.get(1)))
          .toList());
    }

    boolean tryDrop(List<Disk> disks) {
      int disk = 0;
      do {
        if (disks.get(disk).current != 0) {
          break;
        }
        disks = disks.stream().map(Disk::tick).toList();
      } while (++disk != disks.size());
      return disk == disks.size();
    }

    int findDropTick() {
      var testDisks = disks;
      int tick = 0;
      do {
        tick++;
        testDisks = testDisks.stream().map(Disk::tick).toList();
      } while (!tryDrop(testDisks));
      return tick - 1;
    }
  }

  @Override
  String part1() {
    Sculpture sculpture = Sculpture.fromData(data);
    return String.valueOf(sculpture.findDropTick());
  }

  @Override
  String part2() {
    Sculpture sculpture = Sculpture.fromData(data);
    List<Disk> disks = new ArrayList<>(sculpture.disks);
    disks.add(new Disk(0, 11));
    return String.valueOf(new Sculpture(disks).findDropTick());
  }

  public static void main(String[] args) {
    Day day = new Day15() {
      @Override
      String getData() {
        return """
            Disc #1 has 5 positions; at time=0, it is at position 4.
            Disc #2 has 2 positions; at time=0, it is at position 1.""";
      }
    };
    day.run("5", "85");
  }
}
