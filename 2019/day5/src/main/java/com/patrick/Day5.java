package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

        OpCode prog = new OpCode(codes);
        return prog.run(5);
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

                OpCode prog = new OpCode(copy);
                try {
                    val = prog.run(0);
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
