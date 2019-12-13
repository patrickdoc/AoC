package com.patrick;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpCode {

    private int[] codes;

    public OpCode(int[] codes) {
        this.codes = Arrays.copyOf(codes, codes.length);
    }

    public Deque<Integer> run(Deque<Integer> input) {
        // Instruction pointer
        int ip = 0;

        Deque<Integer> output = new ArrayDeque<>();

        // Loop
        while (true) {
            int opcode = codes[ip] % 100;

            if (opcode == 1) {
                codes[codes[ip + 3]] = add(ip);
                ip += 4;

            } else if (opcode == 2) {
                codes[codes[ip + 3]] = mult(ip);
                ip += 4;

            } else if (opcode == 3) {
                codes[codes[ip + 1]] = input.removeFirst();
                ip += 2;

            } else if (opcode == 4) {
                output.addLast(getParam(ip, 1));
                ip += 2;

            } else if (opcode == 5) {
                ip = jneq(ip);

            } else if (opcode == 6) {
                ip = jeq(ip);

            } else if (opcode == 7) {
                codes[codes[ip + 3]] = lt(ip);
                ip += 4;

            } else if (opcode == 8) {
                codes[codes[ip + 3]] = eq(ip);
                ip += 4;

            } else if (opcode == 99) {
                break;
            }
        }

        return output;
    }

    private int getParam(int ip, int pos) {
        int mode = ((int) (codes[ip] % Math.pow(10, 2+pos))) / (int) Math.pow(10, 1+pos);
        return codes[mode == 1 ? ip + pos : codes[ip + pos]];
    }

    private int add(int ip) {
        int p1 = getParam(ip, 1);
        int p2 = getParam(ip, 2);
        return p1 + p2;
    }

    private int mult(int ip) {
        int p1 = getParam(ip, 1);
        int p2 = getParam(ip, 2);
        return p1 * p2;
    }

    private int lt(int ip) {
        int p1 = getParam(ip, 1);
        int p2 = getParam(ip, 2);
        return p1 < p2 ? 1 : 0;
    }

    private int jeq(int ip) {
        return getParam(ip, 1) == 0 ? getParam(ip, 2) : ip + 3;
    }

    private int jneq(int ip) {
        return getParam(ip, 1) != 0 ? getParam(ip, 2) : ip + 3;
    }

    private int eq(int ip) {
        int p1 = getParam(ip, 1);
        int p2 = getParam(ip, 2);
        return p1 == p2 ? 1 : 0;
    }
}
