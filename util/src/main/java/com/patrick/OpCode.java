package com.patrick;

import java.util.*;

public class OpCode {

    private long[] codes;
    private long ip;
    private long relBase;

    public OpCode(long[] codes) {
        this.codes = Arrays.copyOf(codes, 10000);
        this.ip = 0;
        this.relBase = 0;
    }

    public Deque<Long> run(Deque<Integer> input) {

        Deque<Long> output = new ArrayDeque<>();

        // Loop
        while (true) {
            long opcode = codes[(int) ip] % 100;

            if (opcode == 1) {
                codes[(int) codes[(int) ip + 3]] = add(ip);
                ip += 4;

            } else if (opcode == 2) {
                codes[(int) codes[(int) ip + 3]] = mult(ip);
                ip += 4;

            } else if (opcode == 3) {
                codes[(int) codes[(int) ip + 1]] = input.removeFirst();
                ip += 2;

            } else if (opcode == 4) {
                output.addLast(getParam(ip, 1));
                ip += 2;
                break;

            } else if (opcode == 5) {
                ip = jneq(ip);

            } else if (opcode == 6) {
                ip = jeq(ip);

            } else if (opcode == 7) {
                codes[(int) codes[(int) ip + 3]] = lt(ip);
                ip += 4;

            } else if (opcode == 8) {
                codes[(int) codes[(int) ip + 3]] = eq(ip);
                ip += 4;

            } else if (opcode == 9) {
                relBase += getParam(ip, 1);
                ip += 2;

            } else if (opcode == 99) {
                break;
            } else {
                throw new RuntimeException("Unknown code");
            }
        }

        return output;
    }

    private long getParam(long ip, int pos) {
        int bot = (int) Math.pow(10, 1 + pos);
        int top = (int) Math.pow(10, 2 + pos);
        int val = (int) codes[(int) ip];
        int mode = (val % top) / bot;
        switch (mode) {
            case 0:
                return codes[(int) codes[(int) (ip + pos)]];
            case 1:
                return codes[(int) ip + pos];
            case 2:
                return codes[(int) relBase + (int) codes[(int) ip + pos]];
            default:
                return 1;
        }
    }

    private long add(long ip) {
        long p1 = getParam(ip, 1);
        long p2 = getParam(ip, 2);
        return p1 + p2;
    }

    private long mult(long ip) {
        long p1 = getParam(ip, 1);
        long p2 = getParam(ip, 2);
        return p1 * p2;
    }

    private long lt(long ip) {
        long p1 = getParam(ip, 1);
        long p2 = getParam(ip, 2);
        return p1 < p2 ? 1 : 0;
    }

    private long jeq(long ip) {
        return getParam(ip, 1) == 0 ? getParam(ip, 2) : ip + 3;
    }

    private long jneq(long ip) {
        return getParam(ip, 1) != 0 ? getParam(ip, 2) : ip + 3;
    }

    private long eq(long ip) {
        long p1 = getParam(ip, 1);
        long p2 = getParam(ip, 2);
        return p1 == p2 ? 1 : 0;
    }
}
