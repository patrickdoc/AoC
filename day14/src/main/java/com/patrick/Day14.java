package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day14 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day14.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day14.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);

        return 846021;
    }

    public static String problem1(long x) {
        Integer[] cs = new Integer[]{3, 7};
        ArrayList<Integer> recipes = new ArrayList<>(Arrays.asList(cs));
        int elf1 = 0;
        int elf2 = 1;

        while (recipes.size() < x * 30) {
            System.out.println(recipes.size());
            int vals = recipes.get(elf1) + recipes.get(elf2);
            if (vals / 10 > 0) {
                recipes.add(vals / 10);
            }
            recipes.add(vals % 10);
            elf1 = (elf1 + recipes.get(elf1) + 1) % recipes.size();
            elf2 = (elf2 + recipes.get(elf2) + 1) % recipes.size();
        }

        Integer[] seq = new Integer[]{8,4,6,0,2,1};
        ArrayList<Integer> goal = new ArrayList(Arrays.asList(seq));
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i) == 8) {
                if (recipes.subList(i, i+6).equals(goal)) {
                    return String.format("Found at: %d", i);
                }
            }
        }

        return String.format("Output: %s\n", "1");
    }


    public static long helper() {
        return 1;
    }

    public static String problem2(long x) {
        return "1";
    }
}

