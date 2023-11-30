package advent2016;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day12 implements Day {

    @Override
    public void run(Support support) throws Exception {
        var input = support.readLines(12);
        part1(input);
        part2(input);
    }

    static class Computer {
        final Map<String, Integer> registers = new HashMap<>();
        final List<String[]> instructions;

        public Computer(List<String> instructions) {
            this.instructions = instructions.stream().map(s -> s.split(" ")).toList();
            for (var register : List.of("a", "b", "c", "d")) {
                registers.put(register, 0);
            }
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
                    registers.put(code[2], getValue(code[1]));
                    pc++;
                    break;
                case "inc":
                    registers.merge(code[1], 1, Integer::sum);
                    pc++;
                    break;
                case "dec":
                    registers.merge(code[1], -1, Integer::sum);
                    pc++;
                    break;
                case "jnz":
                    if (getValue(code[1]) != 0) {
                        pc += Integer.parseInt(code[2]);
                    } else {
                        pc++;
                    }
                    break;
                }
            }
        }
    }

    private void part1(List<String> input) {
        var computer = new Computer(input);
        computer.runToEnd();
        System.out.printf("day 12 part 1: %s%n", computer.registers.get("a"));
    }

    private void part2(List<String> input) {
        var computer = new Computer(input);
        computer.registers.put("c", 1);
        computer.runToEnd();
        System.out.printf("day 12 part 2: %s%n", computer.registers.get("a"));
    }
}
