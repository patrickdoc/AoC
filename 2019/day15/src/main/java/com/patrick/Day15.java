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
public class Day15 {

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day15.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day15.class.getResourceAsStream("/board.txt"))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(helper(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        long[] codes = Arrays.stream(events[0].split(","))
                .mapToLong(Long::parseLong)
                .toArray();
        return problem1(codes);
    }

    public static String problem1(long[] codes) {

        int width = 50;
        long[] board = new long[width * width];
        for (int i = 0; i < board.length; i++) {
            board[i] = 0;
        }

        OpCode prog = new OpCode(codes);
        Deque<Integer> input = new ArrayDeque<>();
        Deque<Long> output = new ArrayDeque<>();

        int position = width/2 + (width / 2 * width);
        int targetPosition = 0;
        while (true) {
            // Choose input
            int dir = (int) (Math.random() * 4 + 1);
            switch (dir) {
                case 1:
                    targetPosition = position - width;
                    break;
                case 2:
                    targetPosition = position + width;
                    break;
                case 3:
                    targetPosition = position - 1;
                    break;
                case 4:
                    targetPosition = position + 1;
                    break;
                default:
                    break;
            }
            input.addFirst(dir);

            // Get output
            output = prog.run(input);

            // Update board
            long status = output.pollFirst();
            if (status == 0) {
                board[targetPosition] = 2;
            } else if (status == 1) {
                board[targetPosition] = 1;
                position = targetPosition;
            } else if (status == 2) {
                board[targetPosition] = 3;
                break;
            }
        }

        board[width/2 + (width / 2 * width)] = 4;
        printGrid(board, width);
        int count = 0;
        return "Commands: " + count;
    }

    public static void printGrid(long[] g, int rowSize) {
        String output;
        for (int j = 0; j < rowSize; j++) {
            output = "";
            for (int i = 0; i < rowSize; i++) {
                long p = g[i + rowSize * j];
                switch ((int) p) {
                    case 0:
                        output += " ";
                        break;
                    case 1:
                        output += ".";
                        break;
                    case 2:
                        output += "#";
                        break;
                    case 3:
                        output += "X";
                        break;
                    case 4:
                        output += "D";
                        break;
                    default:
                        break;
                }
            }
            System.out.println(output);
        }
    }

    public static String helper(Stream<String> input) {
        String[] events = input.toArray(String[]::new);
        int width = events[0].length();
        long[] board = new long[width*events.length];
        for (int i = 0; i < board.length; i++) {
            String row = events[i / width];
            char c = row.charAt(i % width);
            switch (c) {
                case '#':
                    board[i] = 2;
                    break;
                case '.':
                    board[i] = 1;
                    break;
                case 'X':
                    board[i] = 0;
                    break;
            }
        }
        return problem2(board, width);
    }

    public static String problem2(long[] board, int width) {
        int count = 0;
        while (true) {
            boolean marked = false;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 1) {
                    //Check neighbors
                    if (board[i+1] == 0 || board[i-1] == 0 || board[i + width] == 0 || board[i - width] == 0) {
                        board[i] = 3;
                        marked = true;
                    }
                }
            }

            // Check if any spread
            if (!marked) {
                break;
            }

            // Confirm spread
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 3) {
                    board[i] = 0;
                }
            }
            count++;
        }
        return "Minutes: " + count;
    }
}

