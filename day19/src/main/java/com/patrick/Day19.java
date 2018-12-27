package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day19 {

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day19.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day19.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Sample {
        public Integer[] before;
        public Integer[] opcode;
        public Integer[] after;

        public Sample(Integer[] b, Integer[] o, Integer[] a) {
            before = b;
            opcode = o;
            after = a;
        }
    }

    public static String handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        List<Integer[]> prog = new ArrayList<>();
        List<Sample> samples = new ArrayList<>();

        for (int i = 0; i < events.length; i++) {
            if (events[i].length() > 0 && events[i].charAt(0) == 'B') {
                Integer[] b = parseBefore(events[i]);
                i++;
                Integer[] o = parseOpcode(events[i]);
                i++;
                Integer[] a = parseAfter(events[i]);
                samples.add(new Sample(b,o,a));
            } else if (events[i].length() > 0) {
                prog.add(parseOpcode(events[i]));
            }
        }

        return problem2(samples, prog);
    }

    private static Integer[] parseBefore(String s) {
        String regex = "Before: \\[(\\d+), (\\d+), (\\d+), (\\d+)\\]";
        Matcher m = Pattern.compile(regex).matcher(s);

        if (m.matches()) {
            Integer[] result = new Integer[4];
            result[0] = Integer.parseInt(m.group(1));
            result[1] = Integer.parseInt(m.group(2));
            result[2] = Integer.parseInt(m.group(3));
            result[3] = Integer.parseInt(m.group(4));
            return result;
        } else {
            System.out.println("Ahhh");
            return null;
        }
    }

    private static Integer[] parseOpcode(String s) {
        String[] strs = s.split("\\s");
        Integer[] result = new Integer[strs.length];

        for (int i = 0; i < strs.length; i++) {
            result[i] = Integer.parseInt(strs[i]);
        }
        return result;
    }

    private static Integer[] parseAfter(String s) {
        String regex = "After:  \\[(\\d+), (\\d+), (\\d+), (\\d+)\\]";
        Matcher m = Pattern.compile(regex).matcher(s);

        if (m.matches()) {
            Integer[] result = new Integer[4];
            result[0] = Integer.parseInt(m.group(1));
            result[1] = Integer.parseInt(m.group(2));
            result[2] = Integer.parseInt(m.group(3));
            result[3] = Integer.parseInt(m.group(4));
            return result;
        } else {
            System.out.println("Ahhh2");
            return null;
        }
    }

    public static String problem1(List<Sample> samples) {
        // Number of samples matching 3+ codes
        int count = 0;

        List<Function<Sample, Integer[]>> funcs = new ArrayList<>(16);
        funcs.add(Day19::addr);
        funcs.add(Day19::addi);
        funcs.add(Day19::mulr);
        funcs.add(Day19::muli);
        funcs.add(Day19::banr);
        funcs.add(Day19::bani);
        funcs.add(Day19::borr);
        funcs.add(Day19::bori);
        funcs.add(Day19::setr);
        funcs.add(Day19::seti);
        funcs.add(Day19::gtir);
        funcs.add(Day19::gtri);
        funcs.add(Day19::gtrr);
        funcs.add(Day19::eqir);
        funcs.add(Day19::eqri);
        funcs.add(Day19::eqrr);

        for (Sample s : samples) {
            // Number of codes it matches
            int codeCount = 0;
            for (Function f : funcs) {
                if (checker(f, s)) {
                    codeCount++;
                }
            }
            if (codeCount >= 3) {
                count++;
            }
        }

        return String.format("%d samples match 3+ opcodes\n", count);
    }

    public static long helper() {
        return 1;
    }

    public static boolean checker(Function<Sample, Integer[]> f, Sample s) {
        return Arrays.equals(f.apply(s), s.after);

    }

    public static Integer[] addr(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] + s.before[s.opcode[2]];
        return result;
    }

    public static Integer[] addi(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] + s.opcode[2];
        return result;
    }

    public static Integer[] mulr(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] * s.before[s.opcode[2]];
        return result;
    }

    public static Integer[] muli(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] * s.opcode[2];
        return result;
    }

    public static Integer[] banr(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] & s.before[s.opcode[2]];
        return result;
    }

    public static Integer[] bani(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] & s.opcode[2];
        return result;
    }

    public static Integer[] borr(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] | s.before[s.opcode[2]];
        return result;
    }

    public static Integer[] bori(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] | s.opcode[2];
        return result;
    }

    public static Integer[] setr(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]];
        return result;
    }

    public static Integer[] seti(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.opcode[1];
        return result;
    }

    public static Integer[] gtir(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.opcode[1] > s.before[s.opcode[2]] ? 1 : 0;
        return result;
    }

    public static Integer[] gtri(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] > s.opcode[2] ? 1 : 0;
        return result;
    }

    public static Integer[] gtrr(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]] > s.before[s.opcode[2]] ? 1 : 0;
        return result;
    }

    public static Integer[] eqir(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.opcode[1].equals(s.before[s.opcode[2]]) ? 1 : 0;
        return result;
    }

    public static Integer[] eqri(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]].equals(s.opcode[2]) ? 1 : 0;
        return result;
    }

    public static Integer[] eqrr(Sample s) {
        Integer[] result = s.before.clone();
        result[s.opcode[3]] = s.before[s.opcode[1]].equals(s.before[s.opcode[2]]) ? 1 : 0;
        return result;
    }

    public static String problem2(List<Sample> samples, List<Integer[]> prog) {
        List<Function<Sample, Integer[]>> funcs = new ArrayList<>(16);
        funcs.add(Day19::addr);
        funcs.add(Day19::addi);
        funcs.add(Day19::mulr);
        funcs.add(Day19::muli);
        funcs.add(Day19::banr);
        funcs.add(Day19::bani);
        funcs.add(Day19::borr);
        funcs.add(Day19::bori);
        funcs.add(Day19::setr);
        funcs.add(Day19::seti);
        funcs.add(Day19::gtir);
        funcs.add(Day19::gtri);
        funcs.add(Day19::gtrr);
        funcs.add(Day19::eqir);
        funcs.add(Day19::eqri);
        funcs.add(Day19::eqrr);

        // 1-1 mapping for codes
        List<HashSet<Function<Sample, Integer[]>>> codeFuncs = new ArrayList<>(16);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);
        codeFuncs.add(null);

        // Match all functions to their opcodes
        for (Sample s : samples) {

            HashSet<Function<Sample, Integer[]>> matches = new HashSet<>();
            for (Function f : funcs) {
                if (checker(f, s)) {
                    matches.add(f);
                }
            }
            HashSet<Function<Sample, Integer[]>> current = codeFuncs.get(s.opcode[0]);
            if (current == null) {
                current = matches;
                codeFuncs.set(s.opcode[0], current);
            } else {
                current.retainAll(matches);
            }

            // Once we figure this one out, remove it from the others
            if (current.size() == 1) {
                for (int i = 0; i < codeFuncs.size(); i++) {
                    if (i != s.opcode[0]) {
                        HashSet<Function<Sample, Integer[]>> other = codeFuncs.get(i);
                        if (other != null) {
                            other.removeAll(current);
                        }
                    }
                }
            }
        }

        Integer[] registers = new Integer[4];
        registers[0] = 0;
        registers[1] = 0;
        registers[2] = 0;
        registers[3] = 0;
        for (Integer[] op : prog) {
            Sample input = new Sample(registers, op, null);
            registers = codeFuncs.get(op[0]).iterator().next().apply(input);
        }


        return String.format("Final value = %d", registers[0]);
    }
}