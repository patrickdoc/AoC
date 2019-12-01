package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day25 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day25.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day25.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Star[] handler(Stream<String> lines) {
        Star[] events = lines
                .map(Star::new)
                .toArray(Star[]::new);

        return events;
    }

    public static String problem1(Star[] stars) {
        HashSet<Star> allToNone = new HashSet<>(Arrays.asList(stars));
        HashSet<Star> noneToAll = new HashSet<>();
        int count = 0;

        for (Star s : stars) {
            if (allToNone.contains(s)) {
                helper(s, allToNone, noneToAll);
                count++;
            }
        }

        return String.format("Number of constellations: %d\n", count);
    }

    public static class Star {
        int w;
        int x;
        int y;
        int z;

        public Star(int a, int b, int c, int d) {
            w = a;
            x = b;
            y = c;
            z = d;
        }

        public Star(String s) {
            String[] strs = s.split(",");
            w = Integer.parseInt(strs[0]);
            x = Integer.parseInt(strs[1]);
            y = Integer.parseInt(strs[2]);
            z = Integer.parseInt(strs[3]);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            Star o = (Star) obj;
            return w == o.w && x == o.x && y == o.y && z == o.z;
        }

        @Override
        public int hashCode() {
            return w + x*100 + y*10000 + z*1000000;
        }
    }

    public static int dist(Star a, Star b) {
        return Math.abs(a.w - b.w)
                + Math.abs(a.x - b.x)
                + Math.abs(a.y - b.y)
                + Math.abs(a.z - b.z);
    }

    public static void helper(Star s, HashSet<Star> allToNone, HashSet<Star> noneToAll) {
        noneToAll.add(s);
        for (int wo = s.w - 3; wo <= s.w + 3; wo++) {
            for (int xo = s.x - 3; xo <= s.x + 3; xo++) {
                for (int yo = s.y - 3; yo <= s.y + 3; yo++) {
                    for (int zo = s.z - 3; zo <= s.z + 3; zo++) {
                        Star test = new Star(wo, xo, yo, zo);
                        int d = dist(test,s);
                        if (0 < d && d <= 3 && !noneToAll.contains(test)
                                && allToNone.contains(test)) {
                            helper(test, allToNone, noneToAll);
                        }
                    }
                }
            }
        }
        allToNone.remove(s);
    }

    public static String problem2(long x) {
        return "1";
    }
}

