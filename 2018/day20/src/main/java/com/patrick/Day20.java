package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day20 {

    public static String s;
    public static HashMap<Pos, HashSet<Pos>> map;

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day20.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day20.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);

        return events[0];
    }

    public static String problem1(String str) {
        s = str;
        map = new HashMap<>();

        HashMap<Integer, Integer> closes = new HashMap<>();
        HashMap<Integer, List<Integer>> bars = new HashMap<>();
        HashMap<Integer, Integer> parents = new HashMap<>();
        Deque<Integer> opens = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                opens.addFirst(i);
            } else if (c == '|') {
                Integer parent = opens.peekFirst();
                parents.put(i, parent);
                List<Integer> bs = bars.get(parent);
                if (bs == null) {
                    List<Integer> newBs = new ArrayList<>();
                    newBs.add(i);
                    bars.put(parent, newBs);
                } else {
                    bs.add(i);
                }
            } else if (c == ')') {
                Integer parent = opens.pop();
                closes.put(parent, i);
            }
        }

        HashSet<Map.Entry> rooms = new HashSet<>();
        map.put(new Pos(0,0), new HashSet<>());

        helper(closes, bars, parents, rooms, 1, 0, 0);

        // bfs for last room
        HashSet<Pos> seen = new HashSet<>();
        seen.clear();
        seen.add(new Pos(0,0));
        int count = 0;
        int far = 0;
        Deque<Pos> q = new ArrayDeque<>();
        q.addLast(new Pos(0,0));
        while (!q.isEmpty()) {
            Deque<Pos> qq = new ArrayDeque<>();
            for (Pos p : q) {
                if (count >= 1000) {
                    far++;
                }
                HashSet<Pos> cons = map.get(p);
                for (Pos c : cons) {
                    if (!seen.contains(c)) {
                        qq.add(c);
                        seen.add(c);
                    }
                }
            }
            q = qq;
            count += 1;
        }

        return String.format("Largest number of doors: %d\n", far);
    }

    public static class Pos {
        int x;
        int y;

        public Pos(int a, int b) {
            x = a;
            y = b;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            Pos o = (Pos) obj;
            return x == o.x && y == o.y;
        }

        @Override
        public int hashCode() {
            return x + 10000 * y;
        }

        public Pos clone() {
            return new Pos(this.x, this.y);
        }
    }

    public static void helper(HashMap<Integer, Integer> closes,
                             HashMap<Integer, List<Integer>> bars,
                             HashMap<Integer, Integer> parents,
                             HashSet<Map.Entry> seen,
                             int cursor,
                             int x, int y) {

        while (true) {
            if (seen.contains(Map.entry(cursor, new Pos(x, y)))) {
                // don't duplicate work
                return;
            }
            seen.add(Map.entry(cursor, new Pos(x, y)));

            char c = s.charAt(cursor);
            if (c == 'N') {
                // Update next room
                Pos next = new Pos(x, y + 1);
                HashSet<Pos> newCons = map.get(next);
                if (map.get(next) == null) {
                    newCons = new HashSet<>();
                    map.put(next, newCons);
                }
                newCons.add(new Pos(x, y));

                // Update this room
                HashSet<Pos> oldCons = map.get(new Pos(x, y));
                oldCons.add(next);

                y++;
                cursor++;
            } else if (c == 'S') {
                // Update next room
                Pos next = new Pos(x, y - 1);
                HashSet<Pos> newCons = map.get(next);
                if (map.get(next) == null) {
                    newCons = new HashSet<>();
                    map.put(next, newCons);
                }
                newCons.add(new Pos(x, y));

                // Update this room
                HashSet<Pos> oldCons = map.get(new Pos(x, y));
                oldCons.add(next);

                y--;
                cursor++;
            } else if (c == 'E') {
                // Update next room
                Pos next = new Pos(x+1, y);
                HashSet<Pos> newCons = map.get(next);
                if (map.get(next) == null) {
                    newCons = new HashSet<>();
                    map.put(next, newCons);
                }
                newCons.add(new Pos(x, y));

                // Update this room
                HashSet<Pos> oldCons = map.get(new Pos(x, y));
                oldCons.add(next);

                x++;
                cursor++;
            } else if (c == 'W') {
                // Update next room
                Pos next = new Pos(x-1, y);
                HashSet<Pos> newCons = map.get(next);
                if (map.get(next) == null) {
                    newCons = new HashSet<>();
                    map.put(next, newCons);
                }
                newCons.add(new Pos(x, y));

                // Update this room
                HashSet<Pos> oldCons = map.get(new Pos(x, y));
                oldCons.add(next);

                x--;
                cursor++;
            } else if (c == '|') {
                // branch finished, jump to end
                cursor = closes.get(parents.get(cursor));
            } else if (c == '(') {
                // Explore each branch
                for (Integer bar : bars.get(cursor)) {
                    helper(closes, bars, parents, seen, bar+1, x, y);
                }
                cursor++;
            } else if (c == ')') {
                cursor++;
            } else if (c == '$') {
                break;
            }
        }
    }

    public static String problem2(long x) {
        return "1";
    }
}

