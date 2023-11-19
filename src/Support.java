import java.io.IOException;
import java.util.stream.Collector;

public interface Support {
    static Collector<Character, StringBuilder, String> collectToString() {
        return Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString);
    }

    String readString(String name) throws IOException;
    String[] readLines(String name) throws IOException;

    boolean includeSlow();
}
