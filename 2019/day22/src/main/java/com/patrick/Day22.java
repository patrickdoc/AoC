package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.Stream;
import java.util.HashSet;

/**
 * Hello world!
 *
 */
public class Day22 {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 1000;
    private static long[] ERO_LVLS;
    private static Terrain[] TERRAIN;
    private static Integer[] DIST;
    // CHANGE TOGETHER
    private static final long DEPTH = 11817;
    private static final int TARGET_X = 9;
    private static final int TARGET_Y = 751;

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
        DIST = new Integer[WIDTH * HEIGHT];

        int i = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                ERO_LVLS[i] = (geoIx(x, y) + DEPTH) % 20183;
                TERRAIN[i] = Terrain.values()[(int) ERO_LVLS[i] % 3];
                DIST[i] = null;
                i++;
            }
        }

        return 1;
    }

    public static String problem1(long l) {
        return String.format("Risk level is: %d\n", 10);
    }

    public static long geoIx(int x, int y) {
        if (x == TARGET_X && y == TARGET_Y) {
            return 0;
        } else if (y == 0) {
            return x * 16807;
        } else if (x == 0) {
            return y * 7905;
        } else {
            return ERO_LVLS[fromPos(x-1, y)] * ERO_LVLS[fromPos(x, y-1)];
        }
    }

    public static void printDist() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Integer val = DIST[fromPos(x, y)];
                if (val == null) {
                    System.out.printf(" XX ", val);
                } else {
                    System.out.printf(" %d ", val);

                }
            }
            System.out.println();
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
                } else if (val == 2) {
                    System.out.print("|");
                }
            }
            System.out.println("");
        }
    }

    public static int fromPos(int x, int y) {
        return y * WIDTH + x;
    }

    public static boolean validTool(Equip current, Terrain dest) {
        boolean result = false;
        switch (dest) {
            case ROCK:
                result = current != Equip.N;
                break;
            case WET:
                result = current != Equip.T;
                break;
            case NARROW:
                result = current != Equip.C;
                break;
        }
        return result;
    }
    public static Equip switchGear(Equip current, Terrain src) {
        Equip result = null;
        switch (src) {
            case ROCK:
                result = current == Equip.T ? Equip.C : Equip.T;
                break;
            case WET:
                result = current == Equip.C ? Equip.N : Equip.C;
                break;
            case NARROW:
                result = current == Equip.T ? Equip.N : Equip.T;
                break;
        }
        return result;
    }

    public enum Equip {T, C, N}
    public enum Terrain {ROCK, WET, NARROW}

    public static class State {
        int dist;
        int x;
        int y;
        Equip e;

        public State(int d, int xpos, int ypos, Equip tool) {
            dist = d;
            x = xpos;
            y = ypos;
            e = tool;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            State o = (State) obj;
            return x == o.x && y == o.y && e == o.e;
        }

        @Override
        public int hashCode() {
            return fromPos(x, y) + (WIDTH * HEIGHT * e.ordinal());
        }
    }

    public static long helper() {

        HashSet<State> seen = new HashSet<>();

        Comparator<State> c = Comparator.comparingInt(x -> x.dist);
        PriorityQueue<State> q = new PriorityQueue<>(c);
        q.add(new State(0, 0, 0, Equip.T));

        while (!q.isEmpty()) {
            State s = q.poll();
            int p = fromPos(s.x, s.y);

            if (p == fromPos(TARGET_X, TARGET_Y) && s.e == Equip.T) {
                return s.dist;
            }

            if (seen.contains(s)) {
                continue;
            }
            seen.add(s);
            if (DIST[p] == null) {
                DIST[p] = s.dist;
            }

            State up = null;
            if (0 <= s.x && s.x < WIDTH && 0 <= s.y - 1 && s.y - 1 < HEIGHT) {
                up = new State(s.dist, s.x, s.y - 1, s.e);
            }
            State right = null;
            if (0 <= s.x + 1 && s.x + 1 < WIDTH && 0 <= s.y && s.y < HEIGHT) {
                right = new State(s.dist, s.x + 1, s.y, s.e);
            }
            State left = null;
            if (0 <= s.x - 1 && s.x - 1< WIDTH && 0 <= s.y && s.y < HEIGHT) {
                left = new State(s.dist, s.x - 1, s.y, s.e);
            }
            State down = null;
            if (0 <= s.x && s.x < WIDTH && 0 <= s.y + 1 && s.y + 1 < HEIGHT) {
                down = new State(s.dist, s.x, s.y + 1, s.e);
            }
            State[] dirs = new State[]{up, right, left, down};

            for (State d : dirs) {
                if (d == null) {
                    continue;
                }
                if (validTool(d.e, TERRAIN[fromPos(d.x, d.y)])) {
                    d.dist += 1;

                    if (!seen.contains(d)) {
                        q.add(d);
                    }
                }
            }

            // Consider switching gear
            State newS = new State(s.dist + 7, s.x, s.y, switchGear(s.e, TERRAIN[p]));
            if (!seen.contains(newS)) {
                q.add(newS);
            }
        }
        return 0;
    }

    public static String problem2(long x) {
        printTerrain();
        long bestTime = helper();

        return String.format("Shortest time: %d\n", bestTime);
    }
}