package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day9 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day9.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day9.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        int players = 413;
        long[] playerScores = new long[players];
        Arrays.fill(playerScores, 0);
        int lastMarble = 7108200;

        LinkedList game = new LinkedList(0);
        long currentMarbleScore = 1;
        int currentPlayer = 0;
        while (currentMarbleScore <= lastMarble) {
            // next marble loc
            if (currentMarbleScore % 23 == 0) {
                playerScores[currentPlayer] += currentMarbleScore;
                game = ccw(game);
                playerScores[currentPlayer] += game.val;
                game = game.prev;
                game.removeAfter();
                game = game.next;
            } else {
                game = game.next;
                game.addAfter(currentMarbleScore);
                game = game.next;
            }

            currentMarbleScore++;
            currentPlayer++;
            currentPlayer = currentPlayer % players;
        }

        return playerScores[maxLoc(playerScores)];
    }

    private static Integer maxLoc(long[] arr) {
        int loc = 0;
        long maxVal = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > maxVal) {
                maxVal = arr[i];
                loc = i;
            }
        }
        return loc;
    }

    private static LinkedList ccw(LinkedList l) {
        return l.getBefore(7);
    }

    private static String problem1(long x) {
        return "1";
    }


    public static long helper() {
        return 1;
    }

    private static String problem2(long x) {
        return "1";
    }

    private static class LinkedList {
        LinkedList prev;
        LinkedList next;
        long val;

        public LinkedList(long val) {
            this.prev = this;
            this.next = this;
            this.val = val;
        }

        public LinkedList getBefore(int dist) {
            if (dist == 0) {
                return this;
            } else {
                return this.prev.getBefore(dist - 1);
            }
        }

        public LinkedList getAfter(int dist) {
            if (dist == 0) {
                return this;
            } else {
                return this.next.getAfter(dist - 1);
            }
        }

        public void removeAfter() {
            this.next.next.prev = this;
            this.next = this.next.next;
        }

        public void addAfter(long val) {
            LinkedList newNode = new LinkedList(val);
            newNode.next = this.next;
            newNode.prev = this;
            this.next.prev = newNode;
            this.next = newNode;
        }
    }
}

