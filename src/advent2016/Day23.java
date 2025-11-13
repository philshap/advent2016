package advent2016;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day23 extends Day {
  Day23() {
    super(23);
  }

  record Computer(Map<String, Long> registers, List<String[]> instructions) {

    static Computer fromInput(List<String> instructions) {
      Computer computer =
          new Computer(new HashMap<>(), instructions.stream().map(s -> s.split(" ")).toList());
      for (var register : List.of("a", "b", "c", "d")) {
        computer.registers.put(register, 0L);
      }
      return computer;
    }

    private long getValue(String arg) {
      if (registers.containsKey(arg)) {
        return registers.get(arg);
      }
      return Integer.parseInt(arg);
    }

    static class Debug {
      final Set<Integer> breakpoints = new HashSet<>();
      // Stop on first line if a TTY is attached.
      boolean stepping = System.console() != null;

      void tick(Computer c, int pc) {
        if (!stepping && !breakpoints.contains(pc)) {
          return;
        }
        while (true) {
          String[] command = System.console()
              .printf("%s %2s %-12s%-25s > ", breakpoints.contains(pc) ? '*' : ' ', pc,
                  String.join(" ", c.instructions.get(pc)), c.registers)
              .readLine().split(" ");
          switch (command[0]) {
            case "b":
              int bpc = command.length == 2 ? Integer.parseInt(command[1]) : pc;
              if (breakpoints.contains(bpc)) {
                breakpoints.remove(bpc);
              } else {
                breakpoints.add(bpc);
              }
              break;
            case "s", "":
              stepping = true;
              return;
            case "g":
              stepping = false;
              return;
          }
        }
      }
    }

    void runToEnd(int pc) {
      Debug debug = new Debug();
      while (pc != instructions.size()) {
        debug.tick(this, pc);
        String[] code = instructions.get(pc);
        switch (code[0]) {
          case "cpy":
            if (registers.containsKey(code[2])) {
              registers.put(code[2], getValue(code[1]));
            }
            break;
          case "inc":
            if (registers.containsKey(code[1])) {
              registers.merge(code[1], 1L, Long::sum);
            }
            break;
          case "dec":
            if (registers.containsKey(code[1])) {
              registers.merge(code[1], -1L, Long::sum);
            }
            break;
          case "jnz":
            if (getValue(code[1]) != 0) {
              pc += (int) getValue(code[2]);
              continue;
            }
            break;
          case "tgl":
            toggle(pc + getValue(code[1]));
            break;
        }
        pc++;
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
    void toggle(long target) {
      if (target < 0 || target >= instructions.size()) {
        return;
      }
      String newOp =
          switch (instructions.get((int) target)[0]) {
            case "inc" -> "dec";
            case "dec", "tgl" -> "inc";
            case "jnz" -> "cpy";
            default -> "jnz";
          };
      instructions.get((int) target)[0] = newOp;
    }
  }

  @Override
  String part1() {
    Computer computer = Computer.fromInput(input);
    computer.registers.put("a", 7L);
    computer.runToEnd(0);
    return String.valueOf(computer.registers.get("a"));
  }

  @Override
  String part2() {
    Computer computer = Computer.fromInput(input);
    if (computer.instructions.size() > 7) {
      // Using debugger, found that instruction 0-18 compute factorial of "a".
      computer.registers.put("a", Support.factorial(12L));
      // These instructions get toggled.
      computer.toggle(20);
      computer.toggle(22);
      computer.toggle(24);
      computer.runToEnd(19);
    } else {
      computer.runToEnd(0);
    }
    return String.valueOf(computer.registers.get("a"));
  }

  public static void main(String[] args) {
    Day day =
        new Day23() {
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
    day.run("3", "3");
  }
}
