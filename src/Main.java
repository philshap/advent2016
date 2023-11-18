import java.io.IOException;
import java.util.Objects;

public class Main implements Support {

    @Override
    public String readString(String name) throws IOException {
        try (var stream = Objects.requireNonNull(getClass().getResourceAsStream(name))) {
            return new String(stream.readAllBytes());
        }
    }

    @Override
    public String[] readLines(String name) throws IOException {
        return readString(name).split("\n");
    }

    private void runDays() throws Exception {
        Day[] days = {new Day1(), new Day2(), new Day3()};
        for (var day : days) {
            day.run(this);
        }
    }

    public static void main(String[] args) throws Exception {
        new Main().runDays();
    }
}
