package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day21.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);

        return 1;
    }

    public static String problem1(long x) {
        return "1";
    }


    public static long helper() {
        return 1;
    }

    public static String problem2(long x) {
        return "1";
    }
}

