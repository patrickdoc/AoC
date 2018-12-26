package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day11 {

    static int SERIAL_NUM = 7315;
    static int GRID_SIZE = 300;

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day11.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day11.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long handler(Stream<String> lines) {
        return 1;
    }

    public static String problem1(long grid) {
        int[] powers = new int[GRID_SIZE * GRID_SIZE];
        for (int i = 0; i < powers.length; i++) {
            powers[i] = powerLevel(i % GRID_SIZE, i / GRID_SIZE);
        }

        return maxSqare(powers);
    }

    private static int powerLevel(int x, int y) {
        int id = x + 10;
        int powerLevel = id * y;
        powerLevel += SERIAL_NUM;
        powerLevel *= id;
        powerLevel /= 100;
        powerLevel %= 10;
        powerLevel -= 5;
        return powerLevel;
    }

    private static String maxSqare(int[] powers) {
        int maxX = 0;
        int maxY = 0;
        int maxSize = 0;
        int maxVal = 0;
        int curVal = 0;
        for (int j = 0; j < GRID_SIZE; j++) {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int size = 1; size < GRID_SIZE - Math.max(i, j); size++) {
                    curVal = sumSqare(powers, i, j, size);
                    if (curVal > maxVal) {
                        maxX = i;
                        maxY = j;
                        maxSize = size;
                        maxVal = curVal;
                    }
                }
            }
        }

        return String.format("%d, %d, %d", maxX, maxY, maxSize);
    }

    private static int sumSqare(int[]powers, int x, int y, int size) {
        int sum = 0;

        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                sum += powers[x + i + GRID_SIZE * (y + j)];
            }
        }

        return sum;
    }

    public static String problem2(long x) {
        return "1";
    }
}

