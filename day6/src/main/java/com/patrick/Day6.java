package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Math.abs;

/**
 * Hello world!
 *
 */
public class Day6 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day6.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem2(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long problem1(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        String regex = "(\\d+), (\\d+)";
        Pattern coord = Pattern.compile(regex);
        Integer[] xs = new Integer[events.length];
        Integer[] ys = new Integer[events.length];

        // Parse
        for (int i = 0; i < events.length; i++) {
            Matcher c = coord.matcher(events[i]);
            if (c.matches()) {
                xs[i] = Integer.parseInt(c.group(1));
                ys[i] = Integer.parseInt(c.group(2));
            } else {
                System.out.println("no match");
            }
        }

        // Bounding box
        Integer left = xs[0];
        Integer right = xs[0];
        Integer top = ys[0];
        Integer bottom = ys[0];
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] > right) {
                right = xs[i];
            }
            if (xs[i] < left) {
                left = xs[i];
            }
            if (ys[i] > top) {
                top = ys[i];
            }
            if (ys[i] < bottom) {
                bottom = ys[i];
            }
        }

        // Create and fill grid
        Integer[] counts = new Integer[xs.length];
        Arrays.fill(counts, 0);
        for (int i = bottom; i <= top; i ++) {
            for (int j = left; j <= right; j++) {
                int point = minDistance(xs, ys, j, i);
                if (point != -1) {
                    if (i == bottom || i == top || j == left || j == right) {
                        counts[point] = -10000;
                    } else {
                        counts[point]++;
                    }
                }
            }
        }

        return counts[max(counts)];
    }

    // location of max val
    private static Integer max(Integer[] arr) {
        Integer maxVal = null;
        Integer maxLoc = null;
        for (int i = 0; i < arr.length; i++) {
            if (maxVal == null || arr[i] > maxVal) {
                maxVal = arr[i];
                maxLoc = i;
            }
        }
        return maxLoc;
    }

    private static int minDistance(Integer[] xs, Integer[] ys, int x, int y) {
        long minDistance = 10000000;
        Integer minPoint = null;
        boolean unique = true;
        for (int i = 0; i < xs.length; i++) {
            long dist = distance(x, y, xs[i], ys[i]);
            if (dist < minDistance) {
                minDistance = dist;
                minPoint = i;
                unique = true;
                continue;
            }

            if (dist == minDistance) {
                unique = false;
            }
        }

        if (unique) {
            return minPoint;
        } else {
            return -1;
        }
    }

    private static long distance(int x1, int y1, int x2, int y2) {
        return abs(y2 - y1) + abs(x2 - x1);
    }

    private static int sumDistance(Integer[] xs, Integer[] ys, int x, int y) {
        int totalDist = 0;
        for (int i = 0; i < xs.length; i++) {
            totalDist += distance(x, y, xs[i], ys[i]);
        }
        return totalDist;
    }

    public static long problem2(Stream<String> lines) {
                String[] events = lines.toArray(String[]::new);
        String regex = "(\\d+), (\\d+)";
        Pattern coord = Pattern.compile(regex);
        Integer[] xs = new Integer[events.length];
        Integer[] ys = new Integer[events.length];

        // Parse
        for (int i = 0; i < events.length; i++) {
            Matcher c = coord.matcher(events[i]);
            if (c.matches()) {
                xs[i] = Integer.parseInt(c.group(1));
                ys[i] = Integer.parseInt(c.group(2));
            } else {
                System.out.println("no match");
            }
        }

        // Bounding box
        Integer left = xs[0];
        Integer right = xs[0];
        Integer top = ys[0];
        Integer bottom = ys[0];
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] > right) {
                right = xs[i];
            }
            if (xs[i] < left) {
                left = xs[i];
            }
            if (ys[i] > top) {
                top = ys[i];
            }
            if (ys[i] < bottom) {
                bottom = ys[i];
            }
        }

        // Create and fill grid
        long count = 0;
        for (int i = bottom-300; i <= top+300; i ++) {
            for (int j = left-300; j <= right+300; j++) {
                if (sumDistance(xs, ys, j, i) < 10000) {
                    count++;
                }
            }
        }

        return count;
    }

}

