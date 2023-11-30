package advent2016;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 implements Day {

    @Override
    public void run(Support support) throws Exception {
        var input = support.readLines(10);
        part1(input);
        part2(input);
    }

    static class Bot {
        final int id;
        final boolean lowBot;
        final int lowId;
        final boolean highBot;
        final int highId;
        Integer low;
        Integer high;

        public Bot(int id, int lowId, boolean lowBot, int highId, boolean highBot) {
            this.id = id;
            this.lowId = lowId;
            this.lowBot = lowBot;
            this.highId = highId;
            this.highBot = highBot;
        }

        static final Pattern PATTERN = Pattern.compile("bot (\\d+) gives low to ([a-z]+) (\\d+) and high to ([a-z]+) (\\d+)");

        static Bot fromLine(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                int id = Integer.parseInt(matcher.group(1));
                boolean lowBot = "bot".equals(matcher.group(2));
                int lowId = Integer.parseInt(matcher.group(3));
                boolean highBot = "bot".equals(matcher.group(4));
                int highId = Integer.parseInt(matcher.group(5));
                return new Bot(id, lowId, lowBot, highId, highBot);
            }
            return null;
        }

        void addValue(int value) {
            if (low != null) {
                if (value > low) {
                    high = value;
                } else {
                    high = low;
                    low = value;
                }
            } else if (high == null) {
                low = value;
            }
        }

        record LowHigh(int low, int high) {}

        LowHigh handOff(Map<Integer, Bot> bots, Map<Integer, Integer> output) {
            if (low != null && high != null) {
                if (lowBot) {
                    bots.get(lowId).addValue(low);
                } else {
                    output.put(lowId, low);
                }
                if (highBot) {
                    bots.get(highId).addValue(high);
                } else {
                    output.put(highId, high);
                }
                LowHigh lowHigh = new LowHigh(low, high);
                low = null;
                high = null;
                return lowHigh;
            }
            return null;
        }
    }

    record Value(int value, int bot) {
        static final Pattern PATTERN = Pattern.compile("value (\\d+) goes to bot (\\d+)");

        static Value fromLine(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                return new Value(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }
            return null;
        }
    }

    int runFactory(List<String> input, Map<Integer, Integer> output, Bot.LowHigh findLowHigh) {
        Map<Integer, Bot> bots = new HashMap<>();
        List<Value> values = new ArrayList<>();
        for (var line : input) {
            Bot bot = Bot.fromLine(line);
            if (bot != null) {
                bots.put(bot.id, bot);
            } else {
                values.add(Value.fromLine(line));
            }
        }
        for (var value : values) {
            bots.get(value.bot).addValue(value.value);
        }

        boolean didHandOff;
        do {
            didHandOff = false;
            for (Bot bot : bots.values()) {
                Bot.LowHigh lowHigh = bot.handOff(bots, output);
                if (lowHigh != null) {
                    if (lowHigh.equals(findLowHigh)) {
                        return bot.id;
                    }
                    didHandOff = true;
                }
            }
        } while (didHandOff);
        return -1;
    }

    private void part1(List<String> input) {
        Map<Integer, Integer> output = new HashMap<>();
        System.out.printf("day 10 part 1: %s%n", runFactory(input, output, new Bot.LowHigh(17, 61)));
    }

    private void part2(List<String> input) {
        Map<Integer, Integer> output = new HashMap<>();
        runFactory(input, output, null);
        System.out.printf("day 10 part 2: %s%n", output.get(0) * output.get(1) * output.get(2));
    }
}
