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
        helper(minY, maxY, 500, 0);
        printSlice(minY, maxY);
        int count = 0;
        for (int y = minY; y <= maxY; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (slice[fromPos(x,y)] == Terrain.W) {
                    count++;
                }
            }
        }
        return String.format("Water volume: %d\n", count);
    }

    public static int fromPos(int x, int y) {
        return y * WIDTH + x;
    }

    public static boolean helper(Integer minY, Integer maxY,
            Integer x, Integer y) {
        //printSlice(minY, maxY);
        // we are below the bottom, stop
        if (y > maxY) { return false; }

        // If we hit a wall, return true
        if (slice[fromPos(x, y)] == Terrain.C) { return true; }
        
        // If we hit water, stop, but return false
        if (slice[fromPos(x, y)] == Terrain.W
                || slice[fromPos(x, y)] == Terrain.F) { return false; }

        // Mark square as falling
        slice[fromPos(x,y)] = Terrain.F;
        helper(minY, maxY, x, y+1);
        if (slice[fromPos(x,y+1)] == Terrain.C
                || slice[fromPos(x,y+1)] == Terrain.W) {
            boolean left = helper(minY, maxY, x - 1, y);
            boolean right = helper(minY, maxY, x + 1, y);
            boolean stable = left && right;
            if (stable) {
                // Mark row as stable between clay walls

                int xseek = x;
                while (slice[fromPos(xseek,y)] != Terrain.C) {
                    xseek--;
                }
                xseek++;
                while (slice[fromPos(xseek,y)] != Terrain.C) {
                    slice[fromPos(xseek,y)] = Terrain.W;
                    xseek++;
                }
            }

            return left || right;
        } else {
            return false;
        }
    }

    public static void printSlice(int minY, int maxY) {
        for (int y = minY; y <= maxY; y++) {
            for (int x = 400; x < 600; x++) {
                Terrain t = slice[fromPos(x,y)];
                switch (t) {
                    case C:
                        System.out.print('#');
                        break;
                    case F:
                        System.out.print('|');
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

