package advent2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main implements Support {

    private final boolean includeSlow;

    public Main(String[] args) {
        includeSlow = (args.length == 1 && args[0].equals("includeSlow"));
    }

    @Override
    public String readString(int day) throws IOException {
        return Files.readString(Paths.get("src/advent2016/day%s.input".formatted(day)));
    }

    @Override
    public List<String> readLines(int day) throws IOException {
        return Arrays.asList(readString(day).split("\n"));
    }

    @Override
    public boolean includeSlow() {
        return includeSlow;
    }

    private void runDays() throws Exception {
        Day[] days = {
                new Day1(), new Day2(), new Day3(), new Day4(), new Day5(), new Day6(),
                new Day7(), new Day8(), new Day9(), new Day10(), new Day12(),
                new Day13()};
        for (var day : days) {
            day.run(this);
        }
    }

    public static void main(String[] args) throws Exception {
        new Main(args).runDays();
    }
}
