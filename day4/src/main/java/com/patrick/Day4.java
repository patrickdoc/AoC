package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day4 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day4.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long problem1(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        Arrays.sort(events);

        String time = "\\[(\\d+)\\-(\\d+)\\-(\\d+) (\\d+):(\\d+)\\] ";

        HashMap<Integer, Integer[]> guards = new HashMap<>();
        String guard = time + "Guard #(\\d+).*";
        Pattern guardP = Pattern.compile(guard);
        String wake = time + "wakes up";
        Pattern wakeP = Pattern.compile(wake);
        String sleep = time + "falls asleep";
        Pattern sleepP = Pattern.compile(sleep);

        Integer currentGuard = null;
        Integer lastMinute = null;

        for (String s : events) {
            // New Guard
            Matcher gm = guardP.matcher(s);
            if (gm.matches()) {
                currentGuard = Integer.parseInt(gm.group(6));
                if (guards.get(currentGuard) == null) {
                    Integer[] newMins = new Integer[60];
                    for (int i = 0; i < 60; i++) {
                        newMins[i] = 0;
                    }
                    guards.put(currentGuard, newMins);
                }
                Integer currentHour = Integer.parseInt(gm.group(4));
                Integer currentMinute = Integer.parseInt(gm.group(5));
                if (currentHour == 11) {
                    currentMinute = 0;
                }
                lastMinute = currentMinute;
                continue;
            }

            // Guard falls asleep
            Matcher sm = sleepP.matcher(s);
            if (sm.matches()) {
                Integer currentHour = Integer.parseInt(sm.group(4));
                Integer currentMinute = Integer.parseInt(sm.group(5));
                if (currentHour == 11) {
                    currentMinute = 0;
                }
                lastMinute = currentMinute;
                continue;
            }

            // Guard wakes up
            Matcher wm = wakeP.matcher(s);
            if (wm.matches()) {
                Integer currentMinute = Integer.parseInt(wm.group(5));
                Integer[] minutes = guards.get(currentGuard);
                for (int i = lastMinute; i < currentMinute; i++) {
                    minutes[i]++;
                }
                lastMinute = currentMinute;
                continue;
            }
        }

        Integer highGuard = null;
        Integer highMinute = 0;
        Integer highSleeps = 0;
        for (Map.Entry e : guards.entrySet()) {
            Integer[] minutes = (Integer[]) e.getValue();
            for (int i = 0; i < 60; i++) {
                if (minutes[i] > highSleeps) {
                    highSleeps = minutes[i];
                    highMinute = i;
                    highGuard = (Integer) e.getKey();
                }
            }
        }
        return highGuard * highMinute;
    }
}
