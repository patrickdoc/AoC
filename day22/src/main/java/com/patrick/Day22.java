package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day22 {

    public static final long DEPTH = 11817;

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day22.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day22.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);

        return 1;
    }

    public static String problem1(long l) {
        int width = 10;
        int height = 752;

        Terrain[] cave = new Terrain[width * height];

        int risk = 0;
        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (type(x, y)) {
                    case R:
                        break;
                    case W:
                        risk++;
                        break;
                    case N:
                        risk += 2;
                        break;
                }
                i++;
            }
        }
        return String.format("Risk level is: %d\n", risk);
    }

    public enum Terrain {R, W, N}

    public static long geoIx(long x, long y) {
        if (x == 9 && y == 751) {
            return 0;
        } else if (y == 0) {
            return x * 16807;
        } else if (x == 0) {
            return y * 48271;
        } else {
            return geoIx(x-1, y) * geoIx(x, y-1);
        }
    }

    public static long eroLvl(long x, long y) {
        return (geoIx(x, y) + DEPTH) % 20183;
    }

    public static Terrain type(long x, long y) {
        return Terrain.values()[(int) eroLvl(x, y) % 3];
    }

    public static long helper() {
        return 1;
    }

    public static String problem2(long x) {
        return "1";
    }
}

