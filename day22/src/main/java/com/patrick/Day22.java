package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import java.util.HashSet;

/**
 * Hello world!
 *
 */
public class Day22 {

    public static final int WIDTH = 30;
    public static final int HEIGHT = 900;
    public static final long DEPTH = 11817;
    public static long[] ERO_LVLS;
    public static Terrain[] TERRAIN;
    public static Long[] TORCH;
    public static Long[] CLIMBING;
    public static Long[] NEITHER;

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day22.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem1(handler(stream)));
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

        ERO_LVLS = new long[WIDTH * HEIGHT];
        TERRAIN = new Terrain[WIDTH * HEIGHT];
        TORCH = new Long[WIDTH * HEIGHT];
        NEITHER = new Long[WIDTH * HEIGHT];
        CLIMBING = new Long[WIDTH * HEIGHT];
        
        int i = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                ERO_LVLS[i] = (geoIx(x, y) + DEPTH) % 20183;
                TERRAIN[i] = Terrain.values()[(int) ERO_LVLS[i] % 3];
                i++;
            }
        }

        return 1;
    }

    public static String problem1(long l) {
        return String.format("Risk level is: %d\n", 10);
    }

    public static long geoIx(int x, int y) {
        if (x == 9 && y == 751) {
            return 0;
        } else if (y == 0) {
            return x * 16807;
        } else if (x == 0) {
            return y * 7905;
        } else {
            return ERO_LVLS[fromPos(x-1, y)] * ERO_LVLS[fromPos(x, y-1)];
        }
    }

    public static void printEro() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                long val = ERO_LVLS[fromPos(x, y)];
                System.out.printf(" %d ", val);
            }
            System.out.println("");
        }
    }

    public static void printTerrain() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                long val = ERO_LVLS[fromPos(x, y)] % 3;

                if (val == 0) {
                    System.out.print(".");
                } else if (val == 1) {
                    System.out.print("=");
                } else if (val == 1) {
                    System.out.print("|");
                }
            }
            System.out.println("");
        }
    }

    public static int fromPos(int x, int y) {
        return y * WIDTH + x;
    }

    public static long gearCost(Equip start, Equip target) {
        return start == target ? 0 : 7;
    }

    public enum Equip {T, C, N}
    public enum Terrain {ROCK, WET, NARROW}


    public static long helper(int x, int y, Equip e, HashSet<Integer> seen) {

        int position = fromPos(x, y);
        Long[] info = null;
        switch (e) {
            case T:
                info = TORCH;
                break;
            case C:
                info = CLIMBING;
                break;
            case N:
                info = NEITHER;
                break;
        }

        if (info[position] != null) {
            return info[position];
        }

        seen.add(fromPos(x, y));

        // Set a bogus high value;
        long minCost = 1000000000;

        long torchCost = gearCost(e, Equip.T);
        long climbingCost = gearCost(e, Equip.C);
        long neitherCost = gearCost(e, Equip.N);

        int up      = fromPos(x, y-1);
        long upCost = 1000000000;
        if (up >= 0 && up < ERO_LVLS.length && !seen.contains(up)) {
            switch (TERRAIN[up]) {
                case ROCK:
                    upCost = Math.min(torchCost + helper(x, y-1, Equip.T, (HashSet<Integer>) seen.clone()), climbingCost + helper(x, y-1, Equip.C, (HashSet<Integer>) seen.clone()));
                    break;
                case NARROW:
                    upCost = Math.min(torchCost + helper(x, y-1, Equip.T, (HashSet<Integer>) seen.clone()), neitherCost + helper(x, y-1, Equip.N, (HashSet<Integer>) seen.clone()));
                    break;
                case WET:
                    upCost = Math.min(neitherCost + helper(x, y-1, Equip.N, (HashSet<Integer>) seen.clone()), climbingCost + helper(x, y-1, Equip.C, (HashSet<Integer>) seen.clone()));
                    break;
            }

            if (upCost < minCost) {
                minCost = upCost;
            }
        }

        int right   = fromPos(x+1, y);
        long rightCost = 1000000000;
        if (right >= 0 && right < ERO_LVLS.length && !seen.contains(right)) {
            switch (TERRAIN[right]) {
                case ROCK:
                    rightCost = Math.min(torchCost + helper(x+1, y, Equip.T, (HashSet<Integer>) seen.clone()), climbingCost + helper(x+1, y, Equip.C, (HashSet<Integer>) seen.clone()));
                    break;
                case NARROW:
                    rightCost = Math.min(torchCost + helper(x+1, y, Equip.T, (HashSet<Integer>) seen.clone()), neitherCost + helper(x+1, y, Equip.N, (HashSet<Integer>) seen.clone()));
                    break;
                case WET:
                    rightCost = Math.min(neitherCost + helper(x+1, y, Equip.N, (HashSet<Integer>) seen.clone()), climbingCost + helper(x+1, y, Equip.C, (HashSet<Integer>) seen.clone()));
                    break;
            }

            if (rightCost < minCost) {
                minCost = rightCost;
            }
        }

        int down    = fromPos(x, y+1);
        long downCost = 1000000000;
        if (down >= 0 && down < ERO_LVLS.length && !seen.contains(down)) {
            switch (TERRAIN[down]) {
                case ROCK:
                    downCost = Math.min(torchCost + helper(x, y+1, Equip.T, (HashSet<Integer>) seen.clone()), climbingCost + helper(x, y+1, Equip.C, (HashSet<Integer>) seen.clone()));
                    break;
                case NARROW:
                    downCost = Math.min(torchCost + helper(x, y+1, Equip.T, (HashSet<Integer>) seen.clone()), neitherCost + helper(x, y+1, Equip.N, (HashSet<Integer>) seen.clone()));
                    break;
                case WET:
                    downCost = Math.min(neitherCost + helper(x, y+1, Equip.N, (HashSet<Integer>) seen.clone()), climbingCost + helper(x, y+1, Equip.C, (HashSet<Integer>) seen.clone()));
                    break;
            }

            if (downCost < minCost) {
                minCost = downCost;
            }
        }

        int left    = fromPos(x-1, y);
        long leftCost = 100000000;
        if (left >= 0 && left < ERO_LVLS.length && !seen.contains(left)) {
            switch (TERRAIN[left]) {
                case ROCK:
                    leftCost = Math.min(torchCost + helper(x-1, y, Equip.T, (HashSet<Integer>) seen.clone()), climbingCost + helper(x-1, y, Equip.C, (HashSet<Integer>) seen.clone()));
                    break;
                case NARROW:
                    leftCost = Math.min(torchCost + helper(x-1, y, Equip.T, (HashSet<Integer>) seen.clone()), neitherCost + helper(x-1, y, Equip.N, (HashSet<Integer>) seen.clone()));
                    break;
                case WET:
                    leftCost = Math.min(neitherCost + helper(x-1, y, Equip.N, (HashSet<Integer>) seen.clone()), climbingCost + helper(x-1, y, Equip.C, (HashSet<Integer>) seen.clone()));
                    break;
            }

            if (leftCost < minCost) {
                minCost = leftCost;
            }
        }

        info[position] = 1 + minCost;
        return info[position];
    }

    public static String problem2(long x) {
        HashSet<Integer> seen = new HashSet<>();
        long bestTime = helper(0, 0, Equip.T, seen);

        return String.format("Shortest time: %d\n", bestTime);
    }
}

