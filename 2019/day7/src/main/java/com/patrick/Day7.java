package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day7 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day7.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(stream));
            //System.out.println(problem2(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long problem1(Stream<String> lines) {
        String line = lines
                .toArray(String[]::new)[0];

        int[] codes = Arrays
                .stream(line.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        List<Integer> phases = new ArrayList<>();
        phases.add(5);
        phases.add(6);
        phases.add(7);
        phases.add(8);
        phases.add(9);
        int bestVal = 0;
        for (List<Integer> perm : permute(phases)) {
            int val = p1Helper(0, codes, perm);
            if (val > bestVal) {
                bestVal = val;
            }
        }
        return bestVal;
    }

    private static int p1Helper(int input, int[] codes, List<Integer> settings) {
        Amplifier a = new Amplifier(codes, settings.get(0));
        Amplifier b = new Amplifier(codes, settings.get(1));
        Amplifier c = new Amplifier(codes, settings.get(2));
        Amplifier d = new Amplifier(codes, settings.get(3));
        Amplifier e = new Amplifier(codes, settings.get(4));

        Deque<Integer> inputDeque = new ArrayDeque<>();
        inputDeque.add(input);

        Deque<Integer> output;
        int lastVal = 0;
        while (true) {
            output = a.run(inputDeque);
            if (output.isEmpty()) {
                break;
            }
            output = b.run(output);
            if (output.isEmpty()) {
                break;
            }
            output = c.run(output);
            if (output.isEmpty()) {
                break;
            }
            output = d.run(output);
            if (output.isEmpty()) {
                break;
            }
            output = e.run(output);
            if (output.isEmpty()) {
                break;
            }
            lastVal = output.peekLast();
            inputDeque = output;
        }

        return lastVal;
    }

    private static List<List<Integer>> permute(List<Integer> ints) {
        if (ints.isEmpty()) {
            List<List<Integer>> bigList = new ArrayList<>();
            bigList.add(new ArrayList<>());
            return bigList;
        }

        List<List<Integer>> permutations = new ArrayList<>();
        for (Integer i : ints) {
            List<Integer> copy = new ArrayList<>(ints);
            copy.remove(i);
            List<List<Integer>> perms = permute(copy);
            for (List<Integer> l : perms) {
                l.add(0, i);
            }
            permutations.addAll(perms);
        }

        return permutations;
    }

    public static int problem2(Stream<String> lines) {
        return 1;
    }

    private static class Amplifier {
        OpCode prog;
        int phase;

        public Amplifier(int[] p, int ph) {
            this.prog = new OpCode(p);
            this.phase = ph;
        }

        public Deque<Integer> run(Deque<Integer> input) {
            input.addFirst(phase);
            System.out.print("Input: ");
            System.out.println(input);
            Deque<Integer> output = prog.run(input);
            System.out.print("Output: ");
            System.out.println(output);
            return output;
        }
    }
}
