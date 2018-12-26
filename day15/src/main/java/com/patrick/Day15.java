package com.patrick;

import  com.patrick.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day15 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day15.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day15.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        Board board = new Board(events[0].length(), events.length);
        Unit[] units = new Unit[30];


        int u = 0;
        // Process board input
        for (int j = 0; j < events.length; j++) {
            for (int i = 0; i < events[j].length(); i++) {
                char c = events[j].charAt(i);
                if (c == '#') {
                    board.setPos(new Pair<>(i, j), Terrain.W);
                } else if (c == '.') {
                    board.setPos(new Pair<>(i, j), Terrain.G);
                } else if (c == 'G') {
                    board.setPos(new Pair<>(i, j), Terrain.G);
                    units[u] = new Unit(UnitType.G, new Pair<>(i,j));
                    u++;
                } else if (c == 'E') {
                    board.setPos(new Pair<>(i, j), Terrain.G);
                    units[u] = new Unit(UnitType.E, new Pair<>(i,j));
                    u++;
                } else {
                    return String.format("Bad input: %c\n", c);
                }
            }
        }

        return problem1(board, units);
    }

    public enum Terrain {G, W};

    public static class Board {
        int width;
        int height;
        Terrain[] board;

        public Board(int w, int h) {
            width = w;
            height = h;
            board = new Terrain[width * height];
        }

        public void setPos(Pair<Integer,Integer> pos, Terrain t) {
            board[pos.snd() * width + pos.fst()] = t;
        }

        public Terrain getPos(Pair<Integer, Integer> pos) {
            if (pos.fst() >= 0 && pos.fst() < width
                    && pos.snd() >= 0 && pos.snd() < height) {
                return board[pos.snd() * width + pos.fst()];
            } else {
                return Terrain.W;
            }
        }
    }

    public enum UnitType {G, E};

    public static class Unit {
        Pair<Integer, Integer> pos;
        UnitType t;
        int hp;

        public Unit(UnitType ut, Pair<Integer,Integer> p) {
            this.pos = p;
            this.t = ut;
            this.hp = 200;
        }
    }

    public static String problem1(Board board, Unit[] units) {
        boolean fighting = true;
        int rounds = 0;

        Comparator<Unit> c = Comparator.comparingInt(o -> o.pos.hashCode());

        while (fighting) {
            // Add units to q
            PriorityQueue<Unit> q = new PriorityQueue<>(c);
            for (Unit u : units) {
                if (u != null) {
                    q.add(u);
                }
            }

            while (!q.isEmpty()) {
                Unit u = q.poll();

                // Find targets
                ArrayList<Unit> targets = new ArrayList<>();
                for (Unit o : units) {
                    if (o.t != u.t) {
                        targets.add(o);
                    }
                }

                // No more enemies remain, end the game
                if (targets.size() == 0) {
                    fighting = false;
                    break;
                }

                // Check if target next to unit
                boolean canAttack = false;
                for (Unit e : targets) {
                    if (dist(u.pos, e.pos) == 1) {
                        canAttack = true;
                    }
                }

                // If I can't attack, try to move
                if (!canAttack) {
                    // Find squares in range of targets
                    ArrayList<Pair<Integer, Integer>> inRange = new ArrayList<>();
                    for (Unit e : targets) {
                        Pair<Integer, Integer> up = new Pair<>(e.pos.fst(), e.pos.snd() - 1);
                        Pair<Integer, Integer> down = new Pair<>(e.pos.fst(), e.pos.snd() + 1);
                        Pair<Integer, Integer> left = new Pair<>(e.pos.fst() - 1, e.pos.snd());
                        Pair<Integer, Integer> right = new Pair<>(e.pos.fst() + 1, e.pos.snd());
                        if (board.getPos(up) == Terrain.G) {
                            inRange.add(up);
                        }
                        if (board.getPos(down) == Terrain.G) {
                            inRange.add(down);
                        }
                        if (board.getPos(left) == Terrain.G) {
                            inRange.add(left);
                        }
                        if (board.getPos(right) == Terrain.G) {
                            inRange.add(right);
                        }
                    }
                }

                // Find reachable
            }

        }

        int health = 10;
        return String.format("Outcome: %d * %d = %d", rounds, health, rounds * health);
    }

    public static int dist(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        return Math.abs(a.fst() - b.fst()) + Math.abs(a.snd() - b.snd());
    }

    public static long helper() {
        return 1;
    }

    public static String problem2(long x) {
        return "1";
    }
}

