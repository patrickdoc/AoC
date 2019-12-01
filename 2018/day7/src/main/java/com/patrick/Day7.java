package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day7 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day7.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem1(handler(stream)));
            System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, HashSet<String>> handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        HashMap<String, HashSet<String>> g = new HashMap();
        String regex = "Step ([A-Z]) must be finished before step ([A-Z]) can begin.";
        Pattern letters = Pattern.compile(regex);
        String from = null;
        String to = null;
        for (String s : events) {
            Matcher c = letters.matcher(s);
            if (c.matches()) {
                from = c.group(1);
                to = c.group(2);
            }

            if (!g.containsKey(from)) {
                g.put(from, new HashSet());
            }

            HashSet<String> ins;
            if (!g.containsKey(to)) {
                ins = new HashSet<>();
            } else {
                ins = g.get(to);
            }
            ins.add(from);
            g.put(to, ins);
        }

        return g;
    }

    public static String problem1(HashMap<String, HashSet<String>> g) {
        PriorityQueue<String> q = new PriorityQueue();
        HashSet<String> seen = new HashSet();

        findOpen(g, q);
        String current = helper(q, seen);
        String output = "";
        while (current != null) {
            output += current;
            for (Map.Entry e : g.entrySet()) {
                HashSet<String> vals = (HashSet<String>) e.getValue();
                if (vals.remove(current)) {
                    if (vals.isEmpty()) {
                        q.add((String) e.getKey());
                    }
                }
            }
            current = q.poll();
        }

        return output;
    }

    public static void findOpen(HashMap<String, HashSet<String>> g, PriorityQueue<String> q) {
        for (Map.Entry e : g.entrySet()) {
            HashSet<String> vals = (HashSet<String>) e.getValue();
            if (vals.isEmpty()) {
                q.add((String) e.getKey());
            }
        }
    }

    public static String helper(PriorityQueue<String> q, HashSet<String> seen) {
        String next = q.poll();

        if (next == null) {
            return null;
        }

        if (seen.contains(next)) {
            return helper(q, seen);
        } else {
            seen.add(next);
            return next;
        }
    }

    public static int problem2(HashMap<String, HashSet<String>> g) {
        int worker1 = 0;
        Character worker1Letter = null;
        int worker2 = 0;
        Character worker2Letter = null;
        int worker3 = 0;
        Character worker3Letter = null;
        int worker4 = 0;
        Character worker4Letter = null;
        int worker5 = 0;
        Character worker5Letter = null;

        boolean working = false;
        int time = 0;

        PriorityQueue<String> q = new PriorityQueue();
        HashSet<String> seen = new HashSet();

        findOpen(g, q);

        while (working || !q.isEmpty()) {
            // Assign work if we can
            if (!q.isEmpty()) {
                if (worker1 == 0) {
                    if (q.peek() != null) {
                        Character c = q.poll().charAt(0);
                        worker1 = 61 + c - 'A';
                        worker1Letter = c;
                    }
                }
                if (worker2 == 0) {
                    if (q.peek() != null) {
                        Character c = q.poll().charAt(0);
                        worker2 = 61 + c - 'A';
                        worker2Letter = c;
                    }
                }
                if (worker3 == 0) {
                    if (q.peek() != null) {
                        Character c = q.poll().charAt(0);
                        worker3 = 61 + c - 'A';
                        worker3Letter = c;
                    }
                }
                if (worker4 == 0) {
                    if (q.peek() != null) {
                        Character c = q.poll().charAt(0);
                        worker4 = 61 + c - 'A';
                        worker4Letter = c;
                    }
                }
                if (worker5 == 0) {
                    if (q.peek() != null) {
                        Character c = q.poll().charAt(0);
                        worker5 = 61 + c - 'A';
                        worker5Letter = c;
                    }
                }
            }

            working = false;
            if (worker1 > 0) {
                working = true;
                if (--worker1 == 0) {
                    clearLetter(worker1Letter, g, q);
                    worker1Letter = null;
                }
            }
            if (worker2 > 0) {
                working = true;
                if (--worker2 == 0) {
                    clearLetter(worker2Letter, g, q);
                    worker2Letter = null;
                }
            }
            if (worker3 > 0) {
                working = true;
                if (--worker3 == 0) {
                    clearLetter(worker3Letter, g, q);
                    worker3Letter = null;
                }
            }
            if (worker4 > 0) {
                working = true;
                if (--worker4 == 0) {
                    clearLetter(worker4Letter, g, q);
                    worker4Letter = null;
                }
            }
            if (worker5 > 0) {
                working = true;
                if (--worker5 == 0) {
                    clearLetter(worker5Letter, g, q);
                    worker5Letter = null;
                }
            }
            time++;
        }
        return time;

    }

    public static void clearLetter(Character c, HashMap<String, HashSet<String>> g, PriorityQueue<String> q) {
        String current = Character.toString(c);
        Set<Map.Entry<String, HashSet<String>>> es = g.entrySet();
        for (Map.Entry e : es) {
            HashSet<String> vals = (HashSet<String>) e.getValue();
            if (vals.remove(current)) {
                g.put((String) e.getKey(), vals);
                if (vals.isEmpty()) {
                    q.add((String) e.getKey());
                }
            }
        }
    }
}

