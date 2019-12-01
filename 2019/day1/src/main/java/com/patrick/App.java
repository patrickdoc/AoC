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
            System.out.println("Problem 1 Solution:");
            System.out.println(p2Helper(5));
            //System.out.println(problem1(stream));
            System.out.println("Problem 2 Solution:");
            System.out.println(problem2(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int problem1(Stream<String> lines) {
        return lines
                .map(Integer::parseInt)
                .map((a) -> (a / 3) - 2)
                .reduce(0, (a,b) -> a + b);
    }

    public static int problem2(Stream<String> lines) {
        return lines
                .map(Integer::parseInt)
                .map((a) -> p2Helper(a))
                .reduce(0, (a,b) -> a + b);
    }

    static int p2Helper(int a) {
        int val = (a / 3) - 2;
        if (val > 0) {
            return val + p2Helper(val);
        } else {
            return 0;
        }
    }
}
