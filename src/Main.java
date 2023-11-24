import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main implements Support {

    private final boolean includeSlow;

    public Main(String[] args) {
        includeSlow = (args.length == 1 && args[0].equals("includeSlow"));
    }

    @Override
    public String readString(String name) throws IOException {
        try (var stream = Objects.requireNonNull(getClass().getResourceAsStream(name))) {
            return new String(stream.readAllBytes());
        }
    }

    @Override
    public List<String> readLines(String name) throws IOException {
        return Arrays.asList(readString(name).split("\n"));
    }

    @Override
    public boolean includeSlow() {
        return includeSlow;
    }

    private void runDays() throws Exception {
        Day[] days = {new Day1(), new Day2(), new Day3(), new Day4(), new Day5(), new Day6(),
                new Day7(), new Day8(), new Day9(), new Day10(), new Day12()};
        for (var day : days) {
            day.run(this);
        }
    }

    public static void main(String[] args) throws Exception {
        new Main(args).runDays();
    }
}
