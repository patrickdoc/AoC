package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(App.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
        //    System.out.println(problem1(stream));
            System.out.println(problem2(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int problem1(Stream<String> lines) {
        return lines
                .map(Integer::parseInt)
                .reduce(0, (a, b) -> a + b);
    }

    public static int problem2(Stream<String> lines) {
        Integer[] nums = lines
              .map(Integer::parseInt)
              .toArray(Integer[]::new);
        HashSet<Integer> seen = new HashSet<Integer>();

        return p2Helper(nums, 0, seen);
    }

    private static int p2Helper(Integer[] nums, Integer c, HashSet<Integer> seen) {
        for (Integer i : nums) {
            c += i;
            if (!seen.add(c)) {
                return c;
            }
        }

        return p2Helper(nums, c, seen);
    }
}
