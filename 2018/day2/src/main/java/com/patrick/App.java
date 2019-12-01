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
        //    System.out.println(problem1(stream));
            problem2(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long problem1(Stream<String> lines) {
        String[] ids = lines
              .toArray(String[]::new);

        long two = Arrays.stream(ids)
                .filter(x -> checksum(x, 2))
                .count();

        long three = Arrays.stream(ids)
                .filter(x -> checksum(x, 3))
                .count();

        return two * three;
    }

    // Check if line contains any character count times
    private static boolean checksum(String line, Integer count) {
        HashMap<Character, Integer> map = new HashMap();

        for (int i = 0; i < line.length(); i++) {
            Character c = line.charAt(i);
            Integer current = map.getOrDefault(c, 0);
            map.put(c, current + 1);
        }

        return map.containsValue(count);
    }


    public static void problem2(Stream<String> lines) {
        String[] ids = lines
                .toArray(String[]::new);

        for (int i = 0; i < ids.length; i++) {
            String id1 = ids[i];
            for (int j = 0; j < ids.length; j++) {
                String id2 = ids[j];

                if (hammingOne(id1, id2)) {
                    System.out.println(id1);
                    System.out.println(id2);
                    return;
                }
            }
        }

        return;
    }

    private static boolean hammingOne(String a, String b) {
        boolean foundDifference = false;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                if (foundDifference) {
                    return false;
                } else {
                    foundDifference = true;
                }
            }
        }

        return foundDifference;
    }
}
