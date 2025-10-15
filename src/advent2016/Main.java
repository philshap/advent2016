package advent2016;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Main {

  record PartRun(String result, String duration) {
    static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("s.SSSSS");

    static PartRun run(Supplier<String> part) {
      Instant start = Instant.now();
      String result = part.get();
      Duration between = Duration.between(start, Instant.now());
      // https://stackoverflow.com/a/65586659
      return new PartRun(result, LocalTime.ofNanoOfDay(between.toNanos()).format(FORMAT));
    }
  }

  private void runDay(Day day) {
    PartRun part1 = PartRun.run(day::part1);
    System.out.printf("day %s part 1: (%s) %s%n", day.number(), part1.duration, part1.result);
    PartRun part2 = PartRun.run(day::part2);
    System.out.printf("day %s part 2: (%s) %s%n", day.number(), part2.duration, part2.result);
  }

  private void runDays() {
    IntStream.range(1, 25)
        .mapToObj("advent2016.Day%s"::formatted)
        .map(name -> {
          try {
            return (Day) Class.forName(name).getDeclaredConstructors()[0].newInstance();
          } catch (Exception e) {
            return null;
          }
        }).filter(Objects::nonNull)
        .forEach(this::runDay);
  }

  public static void main(String[] args) {
    new Main().runDays();
  }
}
