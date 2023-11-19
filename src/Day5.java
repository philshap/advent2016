import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 implements Day {
    private final MessageDigest md5;

    public Day5() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(Support support) throws Exception {
        var input = "reyedfim";
        if (support.includeSlow()) {
            part1(input);
            part2(input);
        } else {
            System.out.println("day 5 part 1: too slow, skipping");
            System.out.println("day 5 part 2: too slow, skipping");
        }
    }

    String computeHash(String prefix, int count) {
        md5.reset();
        md5.update(StandardCharsets.UTF_8.encode(prefix + count));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }

    private void part1(String prefix) {
        var password = IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> computeHash(prefix, i))
                .filter(s -> s.startsWith("00000"))
                .limit(8)
                .map(s -> s.charAt(5))
                .collect(Support.collectToString());
        System.out.printf("day 5 part 1: %s%n", password);
    }

    static final Map<Character, Integer> CHAR_TO_INT =
            IntStream.range(0, 8).boxed().collect(Collectors.toMap(i -> (char) ('0' + i), Function.identity()));

    private void part2(String prefix) {
        char[] password = new char[8];
        Arrays.fill(password, '.');
        var foundPass = IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> computeHash(prefix, i))
                .filter(s -> s.startsWith("00000"))
                .map(hash -> {
                    Integer pos = CHAR_TO_INT.get(hash.charAt(5));
                    if (pos != null && password[pos] == '.') {
                        password[pos] = hash.charAt(6);
                    }
                    return new String(password);
                }).dropWhile(s -> s.contains("."))
                .findFirst().orElseThrow();
        System.out.printf("day 5 part 2: %s%n", foundPass);
    }
}
