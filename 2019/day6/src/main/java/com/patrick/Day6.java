package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Math.abs;

/**
 * Hello world!
 *
 */
public class Day6 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day6.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long problem1(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        String regex = "(.+)\\)(.+)";
        Pattern coord = Pattern.compile(regex);

        List<DoubleTrie> orbits = new ArrayList<>();

        // Parse
        String parent;
        String child;
        for (String event : events) {
            Matcher c = coord.matcher(event);
            if (c.matches()) {
                parent = c.group(1);
                child = c.group(2);


                DoubleTrie p = null;
                DoubleTrie ch = null;
                for (DoubleTrie orbit : orbits) {
                    if (orbit.self.equals(parent)) {
                        p = orbit;
                    }
                    if (orbit.self.equals(child)) {
                        ch = orbit;
                    }
                }

                if (p == null) {
                    p = new DoubleTrie(parent);
                    orbits.add(p);
                }
                if (ch == null) {
                    ch = new DoubleTrie(child);
                    orbits.add(ch);
                }

                p.children.add(ch);
                ch.parent = p;
            }
        }

        DoubleTrie COM = null;
        for (DoubleTrie d : orbits) {
            if ("COM".equals(d.self)) {
                COM = d;
            }
        }

        return p2Helper(orbits);
    }

    private static int p1Helper(DoubleTrie d, int parents) {
        int count = parents;
        for (DoubleTrie child : d.children) {
            count += p1Helper(child, parents + 1);
        }
        return count;
    }

    private static int p2Helper(List<DoubleTrie> orbits) {
        DoubleTrie you = null;
        DoubleTrie san = null;

        for (DoubleTrie d : orbits) {
            if ("YOU".equals(d.self)) {
                you = d;
            }
            if ("SAN".equals(d.self)) {
                san = d;
            }
        }

        List<String> prevs = new ArrayList<>();
        prevs.add("COM");
        DoubleTrie cur = san;
        while (!"COM".equals(cur.self)) {
            prevs.add(cur.self);
            cur = cur.parent;
        }

        DoubleTrie sharedParent = null;
        cur = you;
        int count = 0;
        while (true) {
            if (prevs.contains(cur.self)) {
                sharedParent = cur;
                break;
            }
            cur = cur.parent;
            count++;
        }

        cur = san;
        while (cur != sharedParent) {
            cur = cur.parent;
            count++;
        }

        return count;
    }

    private static class DoubleTrie {
        String self;
        DoubleTrie parent;
        List<DoubleTrie> children;

        DoubleTrie(String self) {
            this.self = self;
            this.parent = null;
            this.children = new ArrayList<>();
        }
    }
}

