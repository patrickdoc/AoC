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
public class Day12 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day12.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day12.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        Rule[] rules = Arrays.stream(events, 2, events.length)
                .map(Rule::new)
                .filter(x -> x.pattern.charAt(2) != x.result)
                .toArray(Rule[]::new);

        LinkedList<Pot> plants = null;
        String inputRegex = "initial state: ([#\\.]+)";
        Pattern ip = Pattern.compile(inputRegex);
        Matcher im = ip.matcher(events[0]);
        if (im.matches()) {
            Pot p;
            for (int i = 0; i < im.group(1).length(); i++) {
                p = new Pot(im.group(1).charAt(i), i);

                // Add pot to list
                if (plants == null) {
                    plants = new LinkedList<Pot>(p);
                } else {
                    plants.addAfter(p);
                    plants = plants.getLast();
                }
            }
        }

        return problem1(plants, rules);
    }

    public static String problem1(LinkedList<Pot> plants, Rule[] rules) {
        long last = 0;
        for (int i = 0; i < 200; i++) {
            System.out.printf("Generation: %d\n", i);
            helper(plants, rules);
            long sum = 0;
            LinkedList<Pot> p = plants;
            p = p.getFirst();
            while (p != null) {
                if (p.val.plant == '#') {
                    sum += p.val.ordinal;
                }
                p = p.next;
            }
            System.out.printf("Sum: %d\n", sum);
            System.out.printf("Diff: %d\n", sum - last);
            last = sum;
        }

        return String.format("Final sum: %d", 1);
    }

    public static class Pot {
        char plant;
        int ordinal;
        char next;

        public Pot(char p, int ord) {
            plant = p;
            ordinal = ord;
            next = '.';
        }

        public void age() {
            plant = next;
        }
    }

    public static class Rule {
        String pattern;
        Character result;

        public Rule(String input) {
            String inputRegex = "([#\\.]+) => ([#\\.])";
            Pattern ip = Pattern.compile(inputRegex);
            Matcher im = ip.matcher(input);
            if (im.matches()) {
                pattern = im.group(1);
                result = im.group(2).charAt(0);
            }
        }

        public boolean check(String input) {
            return input.equals(pattern);
        }
    }

    public static void evolve(LinkedList<Pot> p, Rule[] rules) {
        String state = "";

        // backward
        if (p.prev != null) {
            if (p.prev.prev != null) {
                state += p.prev.prev.val.plant;
            } else {
                state += ".";
            }
            state += p.prev.val.plant;
        } else {
            state += "src/main";
        }

        state += p.val.plant;

        // forward
        if (p.next != null) {
            state += p.next.val.plant;
            if (p.next.next != null) {
                state += p.next.next.val.plant;
            } else {
                state += ".";
            }
        } else {
            state += "src/main";
        }

        // Check state against rules
        boolean found = false;
        for (Rule r : rules) {
            if (r.check(state)) {
                p.val.next = r.result;
                found = true;
                break;
            }
        }

        if (!found) {
            p.val.next = p.val.plant;
        }
    }

    public static void extend(LinkedList<Pot> pots) {
        // Add two at beginning
        LinkedList<Pot> p = pots.getFirst();

        // Add 1 before
        p.addBefore(new Pot('.', p.val.ordinal - 1));

        // Add 2 before
        p = p.prev;
        p.addBefore(new Pot('.', p.val.ordinal - 1));

        // Add 1 after
        p = p.getLast();
        p.addAfter(new Pot('.', p.val.ordinal + 1));

        // Add 2 after
        p = p.next;
        p.addAfter(new Pot('.', p.val.ordinal + 1));
    }

    public static void helper(LinkedList<Pot> pots, Rule[] rules) {
        extend(pots);
        pots = pots.getFirst();
        LinkedList<Pot> p = pots;
        while (p != null) {
            evolve(p, rules);
            p = p.next;
        }

        p = pots;
        while (p != null) {
            p.val.age();
            p = p.next;
        }
    }

    public static String problem2(long x) {
        return "1";
    }

}

