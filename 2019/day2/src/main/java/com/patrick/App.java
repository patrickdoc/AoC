package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
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

        int[] copy = Arrays.copyOf(codes, codes.length);

        copy[1] = 12;
        copy[2] = 2;

        OpCode prog = new OpCode(copy);
        return prog.run(0);
    }

    private static int p1Helper(int[] codes) {
        int ip = 0;

        // Loop
        while (true) {
            if (codes[ip] == 1) {
                codes[codes[ip + 3]] = codes[codes[ip + 1]] + codes[codes[ip + 2]];
            } else if (codes[ip] == 2) {
                codes[codes[ip + 3]] = codes[codes[ip + 1]] * codes[codes[ip + 2]];
            } else if (codes[ip] == 99) {
                break;
            }
            ip += 4;
        }
        return codes[0];
    }

    public static int problem2(Stream<String> lines) {
        String line = lines
                .toArray(String[]::new)[0];

        int[] codes = Arrays
                .stream(line.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        int val = 0;
        for (int i = 0; i < codes.length; i++) {
            for (int j = 0; j < codes.length; j++) {
                int[] copy = Arrays.copyOf(codes, codes.length);

                copy[1] = i;
                copy[2] = j;

                try {
                    val = p1Helper(copy);
                } catch (Exception e) {
                    val = 0;
                }
                if (val == 19690720) {
                    System.out.println(i + ", " + j);
                    return val;
                }
            }
        }
        return 0;
    }
}
