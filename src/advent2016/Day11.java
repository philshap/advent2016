package advent2016;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11 extends Day {
  public Day11() {
    super(11);
  }

  record Device(int floor, char type, boolean chip) {
    // hydrogen-compatible microchip
    // hydrogen generator
    static Device fromString(String s, int floor) {
      return new Device(floor, s.charAt(0), s.split(" ")[1].charAt(0) == 'm');
    }

    public Device moveTo(int newFloor) {
      return new Device(newFloor, type, chip);
    }
  }

  static <T> Set<T> replace(Set<T> s, T remove, T add) {
    Set<T> newS = new HashSet<>(s);
    newS.remove(remove);
    newS.add(add);
    return newS;
  }

  static final int FLOORS = 4;

  record Facility(int elevator, Set<Device> devices) {
    static Facility fromLines(List<String> lines) {
      Facility f = new Facility(0, new HashSet<>());
      for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        int floor = i;
        Arrays.stream(line.split(" a "))
            .skip(1)
            .map(s -> Device.fromString(s, floor))
            .forEach(f.devices::add);
      }
      return f;
    }

    boolean safe() {
      for (Device device : devices) {
        if (device.chip) {
          boolean hasRtg = false;
          boolean isSafe = false;
          for (Device d2 : devices) {
            if (d2.floor == device.floor && !d2.chip) {
              if (d2.type == device.type) {
                isSafe = true;
                break;
              }
              hasRtg = true;
            }
          }
          // A chip is unsafe if it doesn't have its RTG and another RTG is present.
          if (hasRtg && !isSafe) {
            return false;
          }
        }
      }
      return true;
    }

    record Pair(int chipFloor, int rtgFloor) {
      Pair merge(Pair other) {
        return new Pair(Math.max(chipFloor, other.chipFloor), Math.max(rtgFloor, other.rtgFloor));
      }
    }

    List<Pair> collectByDevice() {
      Map<Character, Pair> allByType = devices.stream()
          .collect(Collectors.toMap(Device::type,
              dev -> new Pair(dev.chip ? dev.floor : 0, dev.chip ? 0 : dev.floor),
              Pair::merge));
      return allByType.values().stream().sorted(Comparator.comparing(Pair::chipFloor).thenComparing(Pair::rtgFloor)).toList();
    }

    @Override
    public boolean equals(Object obj) {
      Facility other = (Facility) obj;
      if (elevator != other.elevator) {
        return false;
      }
      return collectByDevice().equals(other.collectByDevice());
    }

    @Override
    public int hashCode() {
      return Objects.hash(collectByDevice());
    }

    Facility moveDevice(Device device, int newFloor) {
      return new Facility(newFloor, replace(devices, device, device.moveTo(newFloor)));
    }

    static final int[] UP_DOWN = {-1, 1};

    Stream<Facility> move() {
      List<Facility> newFacilities = new ArrayList<>();
      // The elevator can move one floor up or down. It must carry one or two items.
      for (int dy : UP_DOWN) {
        int newFloor = elevator + dy;
        if (newFloor < 0 || newFloor == FLOORS) {
          continue;
        }
        if (dy == -1 && devices.stream().noneMatch(device -> device.floor <= newFloor)) {
          continue;
        }
        // Create new facilities by moving devices from the elevator floor to the new floor.
        List<Device> list = List.copyOf(devices);
        for (int i = 0; i < devices.size(); i++) {
          Device device = list.get(i);
          if (device.floor != elevator) {
            continue;
          }
          Facility moveOne = moveDevice(device, newFloor);
          newFacilities.add(moveOne);
          for (int j = i + 1; j < devices.size(); j++) {
            Device device2 = list.get(j);
            if (device2.floor != elevator) {
              continue;
            }
            newFacilities.add(moveOne.moveDevice(device2, newFloor));
          }
        }
      }
      return newFacilities.stream();
    }

    boolean done() {
      return devices.stream().allMatch(device -> device.floor == FLOORS - 1);
    }
  }

  static int minSteps(Facility facility) {
    Set<Facility> seen = new HashSet<>();
    int steps = 0;
    List<Facility> facilities = List.of(facility);
    while (facilities.stream().noneMatch(Facility::done) && steps < 100) {
      facilities = facilities.stream().flatMap(Facility::move).filter(Facility::safe).filter(seen::add).toList();
      steps++;
    }
    return steps;
  }

  @Override
  String part1() {
    Facility facility = Facility.fromLines(input);
    return String.valueOf(minSteps(facility));
  }

  @Override
  String part2() {
    Facility facility = Facility.fromLines(input);
    facility.devices.addAll(Set.of(new Device(0, 'e', false),
        new Device(0, 'e', true),
        new Device(0, 'd', false),
        new Device(0, 'd', true)));
    return String.valueOf(minSteps(facility));
  }

  public static void main(String[] args) {
    Day day = new Day11() {
      @Override
      String getData() {
        return """
            The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
            The second floor contains a hydrogen generator.
            The third floor contains a lithium generator.
            The fourth floor contains nothing relevant.""";
      }
    };
    day.run("11", "100");
  }
}
