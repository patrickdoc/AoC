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
public class Day5 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day5.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long problem1(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        String chain = events[0];

        return react(chain).size();
    }

    private static Stack<Character> react(String chain) {
        Stack<Character> agg = new Stack();
        for (int i = 0; i < chain.length(); i++) {
            Character head;
            Character strHead = chain.charAt(i);
            if (agg.isEmpty()) {
                agg.push(chain.charAt(i));
                continue;
            } else {
                head = agg.peek();
            }

            if (head != strHead && Character.toLowerCase(head) == Character.toLowerCase(strHead)) {
                agg.pop();
            } else {
                agg.push(strHead);
            }
        }

        return agg;
    }

    public static long problem2(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        String chain = events[0];

        Integer minSize = null;
        for (char c = 'a'; c <= 'z'; c++) {
            String replace = "[" + c + Character.toUpperCase(c) + "]";
            String removeLetter = new String(chain).replaceAll(replace, "");

            Integer size = react(removeLetter).size();
            if (minSize == null || size < minSize) {
                minSize = size;
            }
        }

        return minSize;
    }

}

