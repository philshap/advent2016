package advent2016;

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
        var input = support.readString(5);
        part1(input);
        part2(input);
    }

    byte[] computeHash(String prefix, int count) {
        md5.reset();
        md5.update(StandardCharsets.UTF_8.encode(prefix + count));
        return md5.digest();
    }

    // Bytes in the MD5 digest are split into two characters in an MD5 hex string.
    // To check for five leading zeros, we check the first five 4-bit values in the byte array.
    private boolean startsWithZeros(byte[] hash) {
        return hash[0] == 0 && hash[1] == 0 && (hash[2] >> 4) == 0;
    }

    static final Map<Integer, Character> INT_TO_CHAR =
            IntStream.range(0, 16).boxed()
                    .collect(Collectors.toMap(Function.identity(), i -> Integer.toHexString(i).charAt(0)));

    private void part1(String prefix) {
        var password = IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> computeHash(prefix, i))
                .filter(this::startsWithZeros)
                .map(hash -> INT_TO_CHAR.get(hash[2] & 0xf))
                .limit(8)
                .collect(Support.collectToString());
        System.out.printf("day 5 part 1: %s%n", password);
    }

    private void part2(String prefix) {
        char[] password = new char[8];
        Arrays.fill(password, '.');
        var foundPass = IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> computeHash(prefix, i))
                .filter(this::startsWithZeros)
                .map(hash -> {
                    int pos = hash[2] & 0xf;
                    if (pos < password.length && password[pos] == '.') {
                        password[pos] = INT_TO_CHAR.get((hash[3] >> 4) & 0xf);
                    }
                    return new String(password);
                }).dropWhile(s -> s.contains("."))
                .findFirst().orElseThrow();
        System.out.printf("day 5 part 2: %s%n", foundPass);
    }
}
