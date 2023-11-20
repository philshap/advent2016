import java.util.*;

public class Day7 implements Day {
    @Override
    public void run(Support support) throws Exception {
        var input = support.readLines("day7.input");
        part1(input);
        part2(input);
    }

    private boolean supportsTls(String ip) {
        boolean inHypernet = false;
        boolean abbaIp = false;
        for (int i = 0; i < ip.length() - 3; i++) {
            var substring = ip.substring(i, i + 4);
            if (substring.contains("[")) {
                inHypernet = true;
                i += 3;
            } else if (substring.contains("]")) {
                inHypernet = false;
                i += 3;
            } else {
                char c0 = substring.charAt(0), c1 = substring.charAt(1), c2 = substring.charAt(2), c3 = substring.charAt(3);
                boolean hasAbba = (c0 != c1) && (c0 == c3) && (c1 == c2);
                if (hasAbba) {
                    if (inHypernet) {
                        return false;
                    }
                    abbaIp = true;
                }
            }
        }
        return abbaIp;
    }

    private void part1(List<String> lines) {
        var count = lines.stream().filter(this::supportsTls).count();
        System.out.printf("day 7 part 2: %s%n", count);

    }

    private boolean supportsSsl(String ip) {
        boolean inHypernet = false;
        Set<String> abas = new HashSet<>();
        for (int i = 0; i < ip.length() - 2; i++) {
            var substring = ip.substring(i, i + 3);
            if (substring.contains("[")) {
                inHypernet = true;
                i += 2;
            } else if (substring.contains("]")) {
                inHypernet = false;
                i += 2;
            } else if (!inHypernet) {
                char c0 = substring.charAt(0), c1 = substring.charAt(1), c2 = substring.charAt(2);
                boolean hasAba = (c0 == c2) && (c0 != c1);
                if (hasAba) {
                    abas.add("" + c1 + c0 + c1);
                }
            }
        }
        if (!abas.isEmpty()) {
            for (int i = 0; i < ip.length() - 2; i++) {
                var substring = ip.substring(i, i + 3);
                if (substring.contains("[")) {
                    inHypernet = true;
                    i += 2;
                } else if (substring.contains("]")) {
                    inHypernet = false;
                    i += 2;
                } else if (inHypernet && abas.contains(substring)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void part2(List<String> lines) {
        var count = lines.stream().filter(this::supportsSsl).count();
        System.out.printf("day 7 part 2: %s%n", count);
    }
}
