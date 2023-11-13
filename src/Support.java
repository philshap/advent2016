import java.io.IOException;

public interface Support {
    String readString(String name) throws IOException;
    String[] readLines(String name) throws IOException;
}
