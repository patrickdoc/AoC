package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day8 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day8.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day8.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static IntStream handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);

        return Arrays.stream(events[0].split("\\s+")).mapToInt(Integer::parseInt);
    }

    private static Integer problem1(IntStream is) {
        Iterator<Integer> ints = is.iterator();

        return helper(ints);
    }


    private static Integer helper(Iterator<Integer> ints) {
        Integer children = ints.next();
        Integer metadata = ints.next();
        Integer output = 0;
        for (int i = 0; i < children; i++) {
            output += helper(ints);
        }
        for (int i = 0; i < metadata; i++) {
            output += ints.next();
        }
        return output;
    }
    private static Integer helper2(Iterator<Integer> ints) {
        Integer children = ints.next();
        Integer metadata = ints.next();
        Integer output = 0;
        Integer[] kids = new Integer[children];
        if (children > 0) {
            for (int i = 0; i < children; i++) {
                kids[i] = helper2(ints);
            }
            for (int i = 0; i < metadata; i++) {
                Integer ref = ints.next();
                if (ref <= children) {
                    output += kids[ref-1];
                }
            }
        } else {
            for (int i = 0; i < metadata; i++) {
                output += ints.next();
            }
        }
        return output;
    }


    private static Integer problem2(IntStream is) {
        Iterator<Integer> ints = is.iterator();
        return helper2(ints);
    }
}

