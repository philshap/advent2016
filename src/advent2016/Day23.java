package advent2016;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day23 extends Day {
  Day23() {
    super(23);
  }
  record Computer(Map<String, Integer> registers, List<String[]> instructions) {

    static Computer fromInput(List<String> instructions) {
      Computer computer = new Computer(new HashMap<>(), instructions.stream().map(s -> s.split(" ")).toList());
      for (var register : List.of("a", "b", "c", "d")) {
        computer.registers.put(register, 0);
      }
      return computer;
    }

    private int getValue(String arg) {
      if (Character.isAlphabetic(arg.charAt(0))) {
        return registers.get(arg);
      }
      return Integer.parseInt(arg);
    }

    void runToEnd() {
      int pc = 0;
      while (pc != instructions.size()) {
        String[] code = instructions.get(pc);
        switch (code[0]) {
          case "cpy":
            if (registers.containsKey(code[2])) {
              registers.put(code[2], getValue(code[1]));
            }
            pc++;
            break;
          case "inc":
            if (registers.containsKey(code[1])) {
              registers.merge(code[1], 1, Integer::sum);
            }
            pc++;
            break;
          case "dec":
            if (registers.containsKey(code[1])) {
              registers.merge(code[1], -1, Integer::sum);
            }
            pc++;
            break;
          case "jnz":
            if (getValue(code[1]) != 0) {
              pc += getValue(code[2]);
            } else {
              pc++;
            }
            break;
          case "tgl":
            toggle(pc + getValue(code[1]));
            pc++;
            break;
        }
      }
    }

    // tgl x toggles the instruction x away (pointing at instructions like jnz does:
    // positive means forward; negative means backward):
    //
    // - For one-argument instructions, inc becomes dec, and all
    //   other one-argument instructions become inc.
    // - For two-argument instructions, jnz becomes cpy, and all
    //   other two-instructions become jnz.
    // - If an attempt is made to toggle an instruction outside the program, nothing happens.
    void toggle(int target) {
      if (target < 0 || target >= instructions.size()) {
        return;
      }
      String newOp =
          switch (instructions.get(target)[0]) {
            case "inc" -> "dec";
            case "dec", "tgl" -> "inc";
            case "jnz" -> "cpy";
            default -> "jnz";
          };
      instructions.get(target)[0] = newOp;
    }
  }

  @Override
  String part1() {
    Computer computer = Computer.fromInput(input);
    computer.registers.put("a", 7);
    computer.runToEnd();
    return String.valueOf(computer.registers.get("a"));
  }

  @Override
  String part2() {
    return "???";
  }

  public static void main(String[] args) {
    Day day = new Day23() {
      @Override
      String getData() {
        return """
            cpy 2 a
            tgl a
            tgl a
            tgl a
            cpy 1 a
            dec a
            dec a""";
      }
    };
    day.run("3", null);
  }
}
