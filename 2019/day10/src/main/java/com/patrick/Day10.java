package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day10 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day10.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day10.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Particle[] handler(Stream<String> lines) {
        String[] strs = lines.toArray(String[]::new);
        Particle[]  events = new Particle[strs.length];
        for (int i = 0; i < strs.length; i++) {
            events[i] = new Particle(strs[i]);
        }

        return events;
    }

    public static class Particle {
        long posX;
        long posY;
        long velX;
        long velY;

        public Particle(String s) {
            String regex = "position=<\\s*(-?[0-9]+),\\s+(-?[0-9]+)> velocity=<\\s*(-?[0-9]+),\\s+(-?[0-9]+)>";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(s);
            if (m.matches()) {
                this.posX = Integer.parseInt(m.group(1));
                this.posY = Integer.parseInt(m.group(2));
                this.velX = Integer.parseInt(m.group(3));
                this.velY = Integer.parseInt(m.group(4));
            } else {
                System.out.println("Failed on");
                System.out.println(s);
            }
        }

        public void tick() {
            this.posX += velX;
            this.posY += velY;
        }
    }

    public static String problem1(Particle[] ps) {
        int rowSize = 200;
        int gridSize = rowSize * rowSize;
        Boolean[] points = new Boolean[gridSize];


        int minIter = 0;
        long minSize = 1000000000;
        Long minX;
        Long maxX;
        Long minY;
        Long maxY;
        for (int i = 0; i < 10470; i++) {
            minX = null;
            maxX = null;
            minY = null;
            maxY = null;

            for (Particle p : ps) {
                p.tick();
                if (minX == null || p.posX < minX) {
                    minX = p.posX;
                }
                if (maxX == null || p.posX > maxX) {
                    maxX = p.posX;
                }
                if (minY == null || p.posY < minY) {
                    minY = p.posY;
                }
                if (maxY == null || p.posY > maxY) {
                    maxY = p.posY;
                }
            }

            long size = Math.abs(maxX - minX) * Math.abs(maxY - minY);
            if (size < minSize) {
                minSize = size;
                minIter = i;
            }
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("Iteration: ");
            System.out.println(i);
            Arrays.fill(points, false);
            for (Particle p : ps) {
                p.tick();
                long loc = p.posX + p.posY * rowSize;
                if (0 <= loc && loc < gridSize) {
                    points[(int) (p.posX + p.posY * rowSize)] = true;
                }
            }
            printGrid(points, rowSize);
        }

        return "Advent";
    }

    public static void printGrid(Boolean[] g, int rowSize) {
        String output;
        for (int j = 0; j < rowSize; j++) {
            output = "";
            for (int i = 0; i < rowSize; i++) {
                if (g[i + rowSize * j]) {
                    output += "# ";
                } else {
                    output += ". ";
                }
            }
            System.out.println(output);
        }
    }


    public static long helper() {
        return 1;
    }

    public static String problem2(long x) {
        return "1";
    }
}

