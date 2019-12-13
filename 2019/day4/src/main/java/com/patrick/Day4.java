package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day4 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day4.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(134564, 585159));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long problem1(int low, int high) {
        int counter = 0;

        for (int x = low; x < high; x++) {
            if (hasDouble(x) && onlyAscending(x)) {
                counter++;
            }
        }

        return counter;
    }

    private static boolean hasDouble(int x) {
        Integer lastDigit = null;
        int y = x;
        boolean dbl = false;
        boolean reset = false;
        while (y > 0) {
            if (lastDigit != null) {
                if (y % 10 == lastDigit) {
                    if (dbl) {
                        reset = true;
                    }
                    dbl = true;
                } else {
                    if (dbl && !reset) {
                        return true;
                    }
                    reset = false;
                    dbl = false;
                }
            }
            lastDigit = y % 10;
            y /= 10;
        }

        return dbl && !reset;
    }

    private static boolean onlyAscending(int x) {
       Integer lastDigit = null;
       int y = x;
       while (y > 0) {
           if (lastDigit != null) {
               if (y % 10 > lastDigit) {
                   return false;
               }
           }
           lastDigit = y % 10;
           y /= 10;
       }
       return true;
    }
}
