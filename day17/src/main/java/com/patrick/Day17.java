package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Hello world!
 *
 */
public class Day17 {

    static final Integer WIDTH = 800;
    static final Integer HEIGHT = 2000;
    static Terrain[] slice;

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day17.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day17.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            // System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Terrain {C, F, S, W}

    public static String handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);

        String xRegex = "x=(\\d+), y=(\\d+)\\.\\.(\\d+)";
        Pattern xp = Pattern.compile(xRegex);

        String yRegex = "y=(\\d+), x=(\\d+)\\.\\.(\\d+)";
        Pattern yp = Pattern.compile(yRegex);

        Integer minY = null;
        Integer maxY = null;
        slice = new Terrain[WIDTH * HEIGHT];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                slice[fromPos(x,y)] = Terrain.S;
            }
        }

        for (String s : events) {
            Matcher xm = xp.matcher(s);
            Matcher ym = yp.matcher(s);

            if (xm.matches()) {
                Integer x = Integer.parseInt(xm.group(1));
                Integer yStart = Integer.parseInt(xm.group(2));
                Integer yEnd = Integer.parseInt(xm.group(3));

                if (minY == null || yStart < minY) {
                    minY = yStart;
                }
                if (maxY == null || yEnd > maxY) {
                    maxY = yEnd;
                }

                for (int y = yStart; y <= yEnd; y++) {
                    slice[fromPos(x,y)] = Terrain.C;
                }

            } else if (ym.matches()) {
                Integer y = Integer.parseInt(ym.group(1));
                Integer xStart = Integer.parseInt(ym.group(2));
                Integer xEnd = Integer.parseInt(ym.group(3));

                if (minY == null || y < minY) {
                    minY = y;
                }
                if (maxY == null || y > maxY) {
                    maxY = y;
                }

                for (int x = xStart; x <= xEnd; x++) {
                    slice[fromPos(x,y)] = Terrain.C;
                }
            } else {
                System.out.printf("Unknown pattern encountered: %s", s);
            }
        }

        return problem1(minY, maxY);
    }

    public static String problem1(Integer minY, Integer maxY) {
        return String.format("Water volume: %d\n",
                helper(minY, maxY, 500, 0));
    }

    public static int fromPos(int x, int y) {
        return y * WIDTH + x;
    }

    public static long helper(Integer minY, Integer maxY,
            Integer x, Integer y) {
        System.out.printf("Focus on (%d, %d)\n", x, y);
        //printSlice(minY, maxY);
        // If we are below the maxY, we are worth 0
        if (y >= maxY) { return 0; }

        // If we are above the min y, we are worth 0
        int self = y >= minY ? 1 : 0;

        // If we are on clay or water, we are worth 0
        if (slice[fromPos(x, y)] == Terrain.C
                || slice[fromPos(x,y)] == Terrain.W) { return 0; }

        long down = helper(minY, maxY, x, y+1);
        slice[fromPos(x,y)] = Terrain.F;
        if (slice[fromPos(x,y+1)] == Terrain.C
                || slice[fromPos(x,y)] == Terrain.W) {
            long left = helper(minY, maxY, x - 1, y);
            long right = helper(minY, maxY, x + 1, y);
            return self + down + left + right;
        } else {
            return self + down;
        }
    }

    public static void printSlice(int minY, int maxY) {
        for (int y = minY; y < minY + 50; y++) {
            for (int x = 400; x < 600; x++) {
                Terrain t = slice[fromPos(x,y)];
                switch (t) {
                    case C:
                        System.out.print('#');
                        break;
                    case S:
                        System.out.print('.');
                        break;
                    case W:
                        System.out.print('~');
                        break;
                }
            }
            System.out.println();
        }
    }

    public static String problem2(long x) {
        return "1";
    }
}

