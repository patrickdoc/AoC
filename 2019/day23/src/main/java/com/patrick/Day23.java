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
public class Day23 {

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day23.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day23.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Nanobot[] handler(Stream<String> lines) {
        Nanobot[] events = lines
                .map(Nanobot::new)
                .toArray(Nanobot[]::new);

        return events;
    }

    public static class Nanobot {
        int x;
        int y;
        int z;
        int r;
        int overlapsAmount;
        int distToOrigin;

        public Nanobot(int xPos, int yPos, int zPos, int radius) {
            x = xPos;
            y = yPos;
            z = zPos;
            r = radius;
            distToOrigin = Math.abs(x) + Math.abs(y) + Math.abs(z);
        }

        public Nanobot(String s) {
            String regex = "pos=<(-?[0-9]+),(-?[0-9]+),(-?[0-9]+)>, r=(\\d+)";
            Matcher m = Pattern.compile(regex).matcher(s);

            if (m.matches()) {
                x = Integer.parseInt(m.group(1));
                y = Integer.parseInt(m.group(2));
                z = Integer.parseInt(m.group(3));
                r = Integer.parseInt(m.group(4));
            }
            distToOrigin = Math.abs(x) + Math.abs(y) + Math.abs(z);
        }

        public void setOverlapsAmount(Nanobot[] bots) {
            overlapsAmount = 0;
            for (Nanobot n : bots) {
                long distance = dist(n, this);
                if (distance > 0 && distance <= n.r) {
                    overlapsAmount++;
                }
            }
        }

        public boolean contains(Nanobot n) {
            return dist(this, n) <= r;
        }

    }

    public static String problem1(Nanobot[] bots) {

        int maxRange = 0;
        Nanobot maxBot = null;
        for (Nanobot n : bots) {
            if (n.r > maxRange) {
                maxRange = n.r;
                maxBot = n;
            }
        }

        int count = 0;
        for (Nanobot n : bots) {
            long d = dist(n, maxBot);
            if (d <= maxBot.r) {
                count++;
            }
        }

        // Best bot at 376
        return String.format("The strongest bot has %d other bots in range\n", count);
    }

    public static long dist(Nanobot a, Nanobot b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
    }

    public static long helper() {
        return 1;
    }

    public static String problem2(Nanobot[] bots) {
        // Boring stuff reading input
        for (Nanobot n : bots) {
            n.setOverlapsAmount(bots);
        }
        Nanobot startOctohedron = generateStartOcothedron(bots);
        Queue<Nanobot> pQ = new PriorityQueue<>(10, new Comparator<Nanobot>() {
            public int compare(Nanobot x, Nanobot y) {
                if (x.overlapsAmount == y.overlapsAmount) {
                    return Integer.compare(x.distToOrigin, y.distToOrigin);
                } else
                    return Integer.compare(-1 * x.overlapsAmount, y.overlapsAmount);
            };
        });
        pQ.add(startOctohedron);
        while (!pQ.isEmpty()) {
            Nanobot n = pQ.poll();
            if (n.r == 0) {
                return String.format("Found: " + n.overlapsAmount + " (" + n.x + "," + n.y + "," + n.z + ") dist: " + n.distToOrigin);
            }
            pQ.addAll(splitNanobot(n, bots));
        }

        return String.format("Failed to find thing\n");
    }

    public static List<Nanobot> splitNanobot(Nanobot src, Nanobot[] bots) {
        List<Nanobot> result = new ArrayList<>();
        int newR = 0;
        int offset = 1;
        if (src.r == 1) {
            result.add(new Nanobot(src.x, src.y, src.z, newR));
        } else if (src.r == 2){
            newR = 1;
        } else {
            newR = (int) Math.ceil(0.66 * src.r);
            offset = src.r - newR;
        }
        result.add(new Nanobot(src.x - offset, src.y, src.z, newR));
        result.add(new Nanobot(src.x + offset, src.y, src.z, newR));
        result.add(new Nanobot(src.x, src.y + offset, src.z, newR));
        result.add(new Nanobot(src.x, src.y - offset, src.z, newR));
        result.add(new Nanobot(src.x, src.y, src.z + offset, newR));
        result.add(new Nanobot(src.x, src.y, src.z - offset, newR));

        for (Nanobot n : result) {
            n.setOverlapsAmount(bots);
        }
        return result;
    }

    public static Nanobot generateStartOcothedron(Nanobot[] bots) {
        int lowestX = 0;
        int lowestY = 0;
        int lowestZ = 0;
        int highestX = 0;
        int highestY = 0;
        int highestZ = 0;
        for (Nanobot n : bots) {
            if (n.x < lowestX) {
                lowestX = n.x;
            }
            if (n.x > highestX) {
                highestX = n.x;
            }
            if (n.y < lowestY) {
                lowestY = n.y;
            }
            if (n.y > highestY) {
                highestY = n.y;
            }
            if (n.z < lowestZ) {
                lowestZ = n.z;
            }
            if (n.z > highestZ) {
                highestZ = n.z;
            }
            }
        Nanobot result = new Nanobot(lowestX + (highestX-lowestX)/2,lowestY + (highestY-lowestY)/2,lowestZ + (highestZ-lowestZ)/2,1);
        boolean containsAll = false;
        while (!containsAll) {
            result.r *= 2;
            containsAll = true;
            for (int i=0; i < bots.length; i++) {
                containsAll &= result.contains(bots[i]);
            }
        }
        return new Nanobot(result.x, result.y, result.z, result.r);
    }
}


