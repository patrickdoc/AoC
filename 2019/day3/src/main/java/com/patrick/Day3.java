package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day3 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day3.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long problem1(Stream<String> lines) {
        String[] line = lines
                .toArray(String[]::new);
        Map<Pair<Integer, Integer>, Integer> set1 = p1Helper(line[0]);
        Map<Pair<Integer, Integer>, Integer> set2 = p1Helper(line[1]);

        Iterator<Pair<Integer, Integer>> it = set2.keySet().iterator();
        int leastSteps = 10000000;
        while (it.hasNext()) {
            Pair<Integer, Integer> p = it.next();
            if (set1.containsKey(p)) {
                int steps = set1.get(p) + set2.get(p);
                if (steps < leastSteps && steps > 0) {
                    leastSteps = steps;
                }
            }
        }

        return leastSteps;
    }

    private static Map<Pair<Integer, Integer>, Integer> p1Helper(String line) {

        String[] codes = Arrays
                .stream(line.split(","))
                .toArray(String[]::new);

        Map<Pair<Integer,Integer>, Integer> points = new HashMap<>();
        points.put(new Pair(0,0), 0);
        int i = 0;
        int j = 0;
        int counter = 0;

        for (String s : codes) {
            Integer dist = Integer.parseInt(s.substring(1));
            switch (s.charAt(0)) {
                case 'R':
                    for (; dist > 0; dist--) {
                        counter++;
                        i++;
                        points.put(new Pair<Integer, Integer>(i, j), counter);
                    }
                    break;
                case 'U':
                    for (; dist > 0; dist--) {
                        counter++;
                        j++;
                        points.put(new Pair<Integer, Integer>(i, j), counter);
                    }
                    break;
                case 'D':
                    for (; dist > 0; dist--) {
                        counter++;
                        j--;
                        points.put(new Pair<Integer, Integer>(i, j), counter);
                    }
                    break;
                case 'L':
                    for (; dist > 0; dist--) {
                        counter++;
                        i--;
                        points.put(new Pair<Integer, Integer>(i, j), counter);
                    }
                    break;
                default:
                    System.out.println("ERROR");
            }
        }

        return points;
    }
}
