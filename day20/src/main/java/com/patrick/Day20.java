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

    public enum Dir {N, E, S, W}

    public static Trie handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        Trie t = new Trie(events[0]);

        return t;
    }

    public static String problem1(Trie t) {
        HashMap<Pair<Integer, Integer>, Room> map = new HashMap<>();

        Pair<Integer, Integer> p = new Pair<>(0,0);
        Room start = new Room(p);
        map.put(p, start);
        Room current = start;

        return "1";
    }

    public static class Room {
        Pair<Integer, Integer> pos;
        List<Room> connections;

        public Room(Pair<Integer, Integer> p) {
            pos = p;
            connections = new ArrayList<>(2);
        }

        public void addConnection(Room r) {
            connections.add(r);
        }
    }

    public static class Trie {
        Dir d;
        ArrayList<Trie> next;

        private Trie(Character v) {
            switch (v) {
                case 'N':
                    d = Dir.N;
                    break;
                case 'E':
                    d = Dir.E;
                    break;
                case 'S':
                    d = Dir.S;
                    break;
                case 'W':
                    d = Dir.W;
                    break;
            }
        }

        public Trie(String s) {
            fromString(s).get(0);
        }

        private ArrayList<Trie> fromString(String s) {
            ArrayList<Trie> ts = new ArrayList<>(1);

            // Handle the '^' and '$' anchors
            int i = 0;
            char c = s.charAt(i);
            if (c == '^') {
                i++;
            } else if (c == '$') {
                return null;
            }
            c = s.charAt(i);

            if (c == 'N' || c == 'E' || c == 'S' || c == 'W') {
                Trie t = new Trie(c);
                t.next = fromString(s.substring(i + 1));
                ts.add(t);
                return ts;
            } else {
                // c is '('. We must break up the paths
                int counter = 1;
                while (counter != 0) {
                    c = s.charAt(i);
                    if (c == '(') {
                        counter++;
                    } else if (c == ')') {
                        counter--;
                    } else if (c == '|') {

                    } else ()

                }
            }

        }
    }



    public static long helper() {
        return 1;
    }

    public static String problem2(long x) {
        return "1";
    }
}

