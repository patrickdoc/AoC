package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day21 {

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day21.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day21.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        Integer ip = Integer.parseInt(events[0].split("\\s")[1]);
        List<Function<Integer[], Integer[]>> prog = new ArrayList<>();

        for (int i = 1; i < events.length; i++) {
            prog.add(helper(events[i]));
        }

        return problem1(ip, prog);
    }

    private static class Params {
        Integer a;
        Integer b;
        Integer c;

        public Params(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    public static String problem1(Integer ip, List<Function<Integer[], Integer[]>> prog) {
        Integer[] registers = new Integer[]{0, 0, 0, 0, 0, 0};

        Integer first = null;
        while (registers[ip] < prog.size()) {
            if (registers[ip] == 28) {
                System.out.printf("[%d, %d, %d, %d, %d, %d]\n", registers[0],
                    registers[1],
                    registers[2],
                    registers[3],
                    registers[4],
                    registers[5]);
                if (first == null) {
                    first = registers[1];
                } else if (registers[1] == first) {
                    break;
                }
            }
            registers = prog.get(registers[ip]).apply(registers);
            registers[ip]++;
        }

        return String.format("Value in register 0: %d\n", registers[0]);
    }

    private static Function<Integer[], Integer[]> helper(String s) {
        String[] strs = s.split("\\s");

        List<BiFunction<Params, Integer[], Integer[]>> ins = new ArrayList<>(16);
        ins.add(Day21::addr);
        ins.add(Day21::addi);
        ins.add(Day21::mulr);
        ins.add(Day21::muli);
        ins.add(Day21::banr);
        ins.add(Day21::bani);
        ins.add(Day21::borr);
        ins.add(Day21::bori);
        ins.add(Day21::setr);
        ins.add(Day21::seti);
        ins.add(Day21::gtir);
        ins.add(Day21::gtri);
        ins.add(Day21::gtrr);
        ins.add(Day21::eqir);
        ins.add(Day21::eqri);
        ins.add(Day21::eqrr);

        // Setup parameters
        Integer a = Integer.parseInt(strs[1]);
        Integer b = Integer.parseInt(strs[2]);
        Integer c = Integer.parseInt(strs[3]);
        Params p = new Params(a, b, c);

        final BiFunction<Params, Integer[], Integer[]> f;
        switch (strs[0]) {
            case "addr":
                f = ins.get(0);
                break;
            case "addi":
                f = ins.get(1);
                break;
            case "mulr":
                f = ins.get(2);
                break;
            case "muli":
                f = ins.get(3);
                break;
            case "banr":
                f = ins.get(4);
                break;
            case "bani":
                f = ins.get(5);
                break;
            case "borr":
                f = ins.get(6);
                break;
            case "bori":
                f = ins.get(7);
                break;
            case "setr":
                f = ins.get(8);
                break;
            case "seti":
                f = ins.get(9);
                break;
            case "gtir":
                f = ins.get(10);
                break;
            case "gtri":
                f = ins.get(11);
                break;
            case "gtrr":
                f = ins.get(12);
                break;
            case "eqir":
                f = ins.get(13);
                break;
            case "eqri":
                f = ins.get(14);
                break;
            case "eqrr":
                f = ins.get(15);
                break;
            default:
                System.out.println("Unknown ins encountered");
                f = null;
        }
        return x -> f.apply(p, x);
    }

    private static Integer[] addr(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] + before[p.b];
        return result;
    }

    private static Integer[] addi(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] + p.b;
        return result;
    }

    private static Integer[] mulr(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] * before[p.b];
        return result;
    }

    private static Integer[] muli(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] * p.b;
        return result;
    }

    private static Integer[] banr(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] & before[p.b];
        return result;
    }

    private static Integer[] bani(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] & p.b;
        return result;
    }

    private static Integer[] borr(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] | before[p.b];
        return result;
    }

    private static Integer[] bori(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] | p.b;
        return result;
    }

    private static Integer[] setr(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a];
        return result;
    }

    private static Integer[] seti(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = p.a;
        return result;
    }

    private static Integer[] gtir(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = p.a > before[p.b] ? 1 : 0;
        return result;
    }

    private static Integer[] gtri(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] > p.b ? 1 : 0;
        return result;
    }

    private static Integer[] gtrr(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a] > before[p.b] ? 1 : 0;
        return result;
    }

    private static Integer[] eqir(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = p.a.equals(before[p.b]) ? 1 : 0;
        return result;
    }

    private static Integer[] eqri(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a].equals(p.b) ? 1 : 0;
        return result;
    }

    private static Integer[] eqrr(Params p, Integer[] before) {
        Integer[] result = before.clone();
        result[p.c] = before[p.a].equals(before[p.b]) ? 1 : 0;
        return result;
    }

    public static String problem2() {
        return String.format("Final value = %d", 0);
    }
}
