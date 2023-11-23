import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9 implements Day {
    @Override
    public void run(Support support) throws Exception {
        var input = support.readString("day9.input");
        part1(input);
        part2(input);
    }

    static final Pattern PATTERN = Pattern.compile("(\\d+)x(\\d+)");

    private static StringBuilder decompress(String input) {
        StringBuilder output = new StringBuilder();
        Matcher matcher = PATTERN.matcher(input);
        for (int i = 0; i < input.length(); i++) {
            if (matcher.find(i)) {
                int length = Integer.parseInt(matcher.group(1));
                int count = Integer.parseInt(matcher.group(2));
                int end = matcher.end() + 1;
                String repeat = input.substring(end, end + length);
                output.append(repeat.repeat(count));
                i = end + length - 1;
            } else {
                output.append(input.charAt(i));
            }
        }
        return output;
    }

    private void part1(String input) {
        System.out.printf("day 9 part 1: %s%n", decompress(input).length());
    }

    long decompressCount(String input) {
        long decompSize = 0;
        Matcher matcher = PATTERN.matcher(input);
        for (int i = 0; i < input.length(); i++) {
            if (matcher.find(i)) {
                int length = Integer.parseInt(matcher.group(1));
                int count = Integer.parseInt(matcher.group(2));
                int end = matcher.end() + 1;
                decompSize += decompressCount(input.substring(end, end + length)) * count;
                i = end + length - 1;
            } else {
                decompSize++;
            }
        }
        return decompSize;
    }

    private void part2(String input) {
        System.out.printf("day 9 part 2: %s%n", decompressCount(input));
    }
}
