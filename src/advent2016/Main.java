package advent2016;

import java.util.Objects;
import java.util.stream.IntStream;

public class Main {

  private void runDays() {
    IntStream.range(1, 25)
        .mapToObj("advent2016.Day%s"::formatted)
        .map(name -> {
          try {
            return (Day) Class.forName(name).getDeclaredConstructors()[0].newInstance();
          } catch (Exception e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .forEach(Day::run);
  }

  private void testDays() {
    IntStream.range(1, 25)
        .mapToObj("advent2016.Day%s"::formatted)
        .forEach(name -> {
          try {
            Class.forName(name).getMethod("main", String[].class).invoke(null, (Object) null);
          } catch (Exception e) {
            // ignore errors
          }
        });
  }

  public static void main(String[] args) {
    if (args.length == 1 && args[0].equals("test")) {
      new Main().testDays();
    } else {
      new Main().runDays();
    }
  }
}
