package advent2016;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 extends Day {
  protected Day10() {
    super(10);
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

  Bot.LowHigh part1Goal = new Bot.LowHigh(17, 61);

  String part1() {
    return String.valueOf(runFactory(input, new HashMap<>(), part1Goal));
  }

  String part2() {
    Map<Integer, Integer> output = new HashMap<>();
    runFactory(input, output, null);
    return String.valueOf(output.get(0) * output.get(1) * output.get(2));
  }

  public static void main(String[] args) {
    Day10 day = new Day10() {
      @Override
      String getData() {
        return """
            value 5 goes to bot 2
            bot 2 gives low to bot 1 and high to bot 0
            value 3 goes to bot 1
            bot 1 gives low to output 1 and high to bot 0
            bot 0 gives low to output 2 and high to output 0
            value 2 goes to bot 2""";
      }
    };
    day.part1Goal = new Bot.LowHigh(2, 5);
    day.run("2", "30");
  }
}
