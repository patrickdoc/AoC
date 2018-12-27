package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day18 {

    static int WIDTH;
    static int HEIGHT;

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day18.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day18.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Terrain {O, W, L}

    public static Terrain[] handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        
        WIDTH = events[0].length();
        HEIGHT = events.length;

        Terrain[] land = new Terrain[WIDTH * HEIGHT];

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                switch (events[y].charAt(x)) {
                    case '.':
                        land[fromPos(x,y)] = Terrain.O;
                        break;
                    case '|':
                        land[fromPos(x,y)] = Terrain.W;
                        break;
                    case '#':
                        land[fromPos(x,y)] = Terrain.L;
                        break;
                }
            }
        }

        return land;
    }

    public static String problem1(Terrain[] land) {
        for (int i = 0; i < 10; i++) {
            System.out.println("Changing");
            helper(land);
            printLand(land);
        }

        int wooded = 0;
        int lumber = 0;
        for (int i = 0; i < land.length; i++) {
            if (land[i] == Terrain.W) {
                wooded++;
            } else if (land[i] == Terrain.L) {
                lumber++;
            }
        }
        return String.format("Total value: (| * #)  %d * %d = %d", wooded,
                lumber, wooded * lumber);
    }


    public static void helper(Terrain[] land) {
        Terrain[] next = land.clone();

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Terrain type = land[fromPos(x,y)];
                if (type == Terrain.O
                        && countType(land, x, y, Terrain.W) >= 3) {
                    next[fromPos(x,y)] = Terrain.W;
                } else if (type == Terrain.W
                        && countType(land, x, y, Terrain.L) >= 3) {
                    next[fromPos(x,y)] = Terrain.L;
                } else if (type == Terrain.L) {
                    if (countType(land, x, y, Terrain.L) > 0
                            && countType(land, x, y, Terrain.W) > 0) {
                        next[fromPos(x,y)] = Terrain.L;
                    } else {
                        next[fromPos(x,y)] = Terrain.O;
                    }
                }
            }
        }

        land = next;
    }

    public static void printLand(Terrain[] land) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                switch (land[fromPos(x, y)]) {
                    case O:
                        System.out.print('.');
                        break;
                    case W:
                        System.out.print('|');
                        break;
                    case L:
                        System.out.print('#');
                        break;
                }
            }
            System.out.println("");
        }
    }

    public static int countType(Terrain[] land, int x, int y, Terrain t) {
        int count = 0;

        for (int yo = -1; yo <= 1; yo++) {
            for (int xo = -1; xo <= 1; xo++) {
                int newX = x + xo;
                int newY = y + yo;
                if (xo != 0 || yo != 0) {
                    if (newX >= 0 && newX < WIDTH
                            && newY >= 0 && newY < HEIGHT
                            && land[fromPos(newX,newY)] == t) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static int fromPos(int x, int y) {
        return y * WIDTH + x;
    }

    public static String problem2(long x) {
        return "1";
    }
}

