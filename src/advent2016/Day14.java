package advent2016;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.BiFunction;

public class Day14 implements Day {
    private final MessageDigest md5;

    public Day14() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(Support support) throws Exception {
        var input = support.readString(14);
        part1(input);
        part2(input);
    }

    static final byte[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    byte[] hashToChars(byte[] hash) {
        byte[] result = new byte[32];
        for (int i = 0; i < hash.length; i++) {
            result[i * 2] = CHARS[(hash[i] >> 4) & 0xf];
            result[i * 2 + 1] = CHARS[hash[i] & 0xf];
        }
        return result;
    }

    private byte[] computeHash(byte[] message) {
        md5.reset();
        md5.update(message);
        return hashToChars(md5.digest());
    }

    byte[] computeHash(String prefix, int count) {
        return computeHash((prefix + count).getBytes());
    }

    private byte findTriple(byte[] hash) {
        for (int i = 0; i < 32 - 2; i++) {
            if (hash[i] == hash[i + 1] && hash[i] == hash[i + 2]) {
                return hash[i];
            }
        }
        return -1;
    }

    private boolean findPentad(byte[] hash, byte val) {
        for (int i = 0; i < 32 - 5; i++) {
            if (val == hash[i] && val == hash[i + 1] && val == hash[i + 2] &&
                    val == hash[i + 3] && val == hash[i + 4]) {
                return true;
            }
        }
        return false;
    }

    boolean isKey(String prefix, int index, BiFunction<String, Integer, byte[]> hashFunc) {
        byte[] hash = hashFunc.apply(prefix, index);
        byte triple = findTriple(hash);
        if (triple == -1) {
            return false;
        }
        for (int i = 1; i <= 1000; i++) {
            byte[] subhash = hashFunc.apply(prefix, index + i);
            if (findPentad(subhash, triple)) {
                return true;
            }
        }
        return false;
    }

    private int getLastKeyIndex(String prefix, BiFunction<String, Integer, byte[]> hashFunc) {
        int foundKeys = 0;
        int i = 0;
        for (; foundKeys != 64; i++) {
            if (isKey(prefix, i, hashFunc)) {
                foundKeys++;
            }
        }
        return i - 1;
    }

    private void part1(String prefix) {
        int index = getLastKeyIndex(prefix, this::computeHash);
        System.out.printf("day 14 part 1: %s%n", index);
    }

    static final int MAX_ENTRIES = 10000;

    static final Map<String, byte[]> hash2Cache = new LinkedHashMap<>(MAX_ENTRIES + 1, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    byte[] computeHash2(String prefix, int count) {
        String message = prefix + count;
        return hash2Cache.computeIfAbsent(message, (s) -> {
            byte[] hash = computeHash(prefix, count);
            for (int i = 0; i < 2016; i++) {
                hash = computeHash(hash);
            }
            return hash;
        });
    }

    private void part2(String prefix) {
        int index = getLastKeyIndex(prefix, this::computeHash2);
        System.out.printf("day 14 part 2: %s%n", index);
    }
}
