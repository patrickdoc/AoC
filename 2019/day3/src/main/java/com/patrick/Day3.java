package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day3 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day3.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long problem1(Stream<String> lines) {
        HashMap<Pair, Integer> land = new HashMap();
        HashSet<Integer> landOwners = new HashSet();
        lines.map(Claim::new)
                .mapToInt(x -> addClaim(x, land, landOwners))
                .sum();

        System.out.println(landOwners.iterator().next());
        return 1;
    }

    private static int addClaim(Claim c, HashMap<Pair, Integer> land, HashSet<Integer> landOwners) {
        boolean open = true;
        for (int i = c.origin.a; i < c.origin.a + c.width; i++) {
            for (int j = c.origin.b; j < c.origin.b + c.height; j++) {
                Integer other = land.get(new Pair(i, j));
                if (other != null) {
                    // Remove other if in owners
                    landOwners.remove(other);
                    open = false;
                }
                land.put(new Pair(i,j), c.id);
            }
        }
        if (open) {
            landOwners.add(c.id);
        }
        return 1;
    }

    private static class Claim {
        public int id;
        public Pair origin;
        public int width;
        public int height;

        public Claim(String line) {
            String re = "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)";
            Pattern p = Pattern.compile(re);
            Matcher m = p.matcher(line);
            if (m.matches()) {
                id = Integer.parseInt(m.group(1));
                origin = new Pair(Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(3)));
                width = Integer.parseInt(m.group(4));
                height = Integer.parseInt(m.group(5));
            }
        }
    }

    private static class Pair {
        public int a;
        public int b;

        public Pair(Integer one, Integer two) {
            a = one;
            b = two;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair that = (Pair) o;
            return (a == that.a && b == that.b);
        }

        public int hashCode() {
            return 10000 * a + b;
        }
    }
}
