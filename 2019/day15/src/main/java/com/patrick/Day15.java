package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day15 {

    private static int ELF_POWER = 26;

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day15.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day15.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        Board board = new Board(events[0].length(), events.length);
        Unit[] units = new Unit[30];


        int u = 0;
        // Process board input
        for (int j = 0; j < events.length; j++) {
            for (int i = 0; i < events[j].length(); i++) {
                char c = events[j].charAt(i);
                if (c == '#') {
                    board.setPos(new Pair<>(i, j), Terrain.W);
                } else if (c == '.') {
                    board.setPos(new Pair<>(i, j), Terrain.G);
                } else if (c == 'G') {
                    board.setPos(new Pair<>(i, j), Terrain.G);
                    units[u] = new Unit(UnitType.G, new Pair<>(i,j));
                    u++;
                } else if (c == 'E') {
                    board.setPos(new Pair<>(i, j), Terrain.G);
                    units[u] = new Unit(UnitType.E, new Pair<>(i,j));
                    u++;
                } else {
                    return String.format("Bad input: %c\n", c);
                }
            }
        }

        return problem1(board, units);
    }

    public enum Terrain {G, W}

    public static class Board {
        int width;
        int height;
        Terrain[] board;

        public Board(int w, int h) {
            width = w;
            height = h;
            board = new Terrain[width * height];
        }

        public void setPos(Pair<Integer,Integer> pos, Terrain t) {
            board[pos.snd() * width + pos.fst()] = t;
        }

        public Terrain getPos(Pair<Integer, Integer> pos) {
            if (pos.fst() >= 0 && pos.fst() < width
                    && pos.snd() >= 0 && pos.snd() < height) {
                return board[pos.snd() * width + pos.fst()];
            } else {
                return Terrain.W;
            }
        }
    }

    public enum UnitType {G, E};

    public static class Unit {
        Pair<Integer, Integer> pos;
        UnitType t;
        int hp;
        int attack;

        public Unit(UnitType ut, Pair<Integer,Integer> p) {
            this.pos = p;
            this.t = ut;
            if (t == UnitType.E) {
                attack = ELF_POWER;
            } else {
                attack = 3;
            }
            this.hp = 200;
        }

        public void move(Pair<Integer, Integer> newPos) {
            pos = newPos;
        }

        public void attack(Unit e) {
            e.hp -= attack;
        }
    }

    public static String problem1(Board board, Unit[] units) {
        boolean fighting = true;
        int rounds = 0;

        Comparator<Unit> c = Comparator.comparingInt(o -> o.pos.hashCode());

        while (fighting) {
            System.out.printf("Round: %d\n", rounds);
            // Add units to q
            PriorityQueue<Unit> q = new PriorityQueue<>(c);
            for (Unit u : units) {
                if (u != null) {
                    q.add(u);
                }
            }
            System.out.printf("Units remaining: %d\n", q.size());

            while (!q.isEmpty()) {
                Unit u = q.poll();
                if (u.hp < 0) {
                    continue;
                }

                // Find targets
                ArrayList<Unit> targets = new ArrayList<>();
                for (Unit o : units) {
                    if (o != null && o.t != u.t) {
                        targets.add(o);
                    }
                }

                // No more enemies remain, end the game
                if (targets.size() == 0) {
                    fighting = false;
                    break;
                }

                // Check if target next to unit
                boolean canAttack = false;
                for (Unit e : targets) {
                    if (dist(u.pos, e.pos) == 1) {
                        canAttack = true;
                    }
                }

                // If I can't attack, try to move
                Pair<Integer,Integer> moveTarget = null;
                if (!canAttack) {
                    // Find squares in range of targets
                    ArrayList<Pair<Integer, Integer>> inRange = new ArrayList<>();
                    for (Unit e : targets) {
                        Pair<Integer, Integer> up = new Pair<>(e.pos.fst(), e.pos.snd() - 1);
                        Pair<Integer, Integer> down = new Pair<>(e.pos.fst(), e.pos.snd() + 1);
                        Pair<Integer, Integer> left = new Pair<>(e.pos.fst() - 1, e.pos.snd());
                        Pair<Integer, Integer> right = new Pair<>(e.pos.fst() + 1, e.pos.snd());

                        if (board.getPos(up) == Terrain.G && !occupied(up, units)) {
                            inRange.add(up);
                        }
                        if (board.getPos(down) == Terrain.G && !occupied(down, units)) {
                            inRange.add(down);
                        }
                        if (board.getPos(left) == Terrain.G && !occupied(left, units)) {
                            inRange.add(left);
                        }
                        if (board.getPos(right) == Terrain.G && !occupied(right, units)) {
                            inRange.add(right);
                        }
                    }

                    // Find closest square in range of targets
                    moveTarget = bfs(board, units, u.pos, inRange);
                }

                if (moveTarget != null) {
                    u.move(moveTarget);
                }

                // Look for someone to fight
                Unit attackTarget = null;
                for (Unit e : targets) {
                    if (dist(u.pos, e.pos) == 1) {
                        if (attackTarget == null) {
                            attackTarget = e;
                        } else {
                            if (e.hp < attackTarget.hp) {
                                attackTarget = e;
                            } else if (e.hp == attackTarget.hp && prio(e.pos, attackTarget.pos) < 0) {
                                attackTarget = e;
                            }
                        }
                    }
                }

                if (attackTarget != null) {
                    u.attack(attackTarget);
                    if (attackTarget.hp <= 0) {
                        for (int i = 0; i < units.length; i++) {
                            if (units[i] != null && units[i].hp < 0) {
                                units[i] = null;
                            }
                        }
                    }
                }

            }
            rounds++;
        }

        // Subtract one from rounds, because the last one didn't finish
        rounds--;
        int alive = 0;
        int health = 0;
        for (int i = 0; i < units.length; i++) {
            if (units[i] != null) {
                health += units[i].hp;
                alive++;
            }
        }
        return String.format("Outcome: (%d units remaining) %d * %d = %d", alive, rounds, health, rounds * health);
    }

    public static int dist(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        return Math.abs(a.fst() - b.fst()) + Math.abs(a.snd() - b.snd());
    }

    public static boolean occupied(Pair<Integer, Integer> p, Unit[] units) {
        for (Unit u : units) {
            if (u != null && u.pos.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public static Pair<Integer, Integer> bfs(Board b, Unit[] us, Pair<Integer, Integer> pos, ArrayList<Pair<Integer, Integer>> targets) {
        // Starting at pos, perform a breadth first search for the nearest target
        HashSet<Pair<Integer, Integer>> seen = new HashSet<>();
        ArrayDeque<Pair<Pair<Pair<Integer, Integer>, Integer>, Pair<Integer, Integer>>> toCheck = new ArrayDeque<>();
        toCheck.add(new Pair(new Pair(pos,0), null));

        Pair<Integer, Integer> result = null;
        Pair<Integer, Integer> bestMove = null;
        Integer resultDist = null;

        while (true) {

            if (toCheck.isEmpty()) {
                break;
            }

            Pair<Pair<Pair<Integer, Integer>, Integer>, Pair<Integer, Integer>> points = toCheck.pollFirst();
            Pair<Integer, Integer> from = points.snd();
            Pair<Integer, Integer> p = points.fst().fst();
            Integer dist = points.fst().snd();

            // Check if we are done
            if (result != null && dist > resultDist) {
                break;
            }
            
            if (seen.contains(p)) {
                continue;
            }

            if (targets.contains(p)) {
                if (result == null || prio(p, result) < 0) {
                    result = p;
                    resultDist = dist;
                    bestMove = from;
                }
            }

            Pair<Integer, Integer> up = new Pair<>(p.fst(), p.snd() - 1);
            Pair<Integer, Integer> down = new Pair<>(p.fst(), p.snd() + 1);
            Pair<Integer, Integer> left = new Pair<>(p.fst() - 1, p.snd());
            Pair<Integer, Integer> right = new Pair<>(p.fst() + 1, p.snd());

            if (!seen.contains(up) && b.getPos(up) == Terrain.G && !occupied(up, us)) {
                if (from == null) {
                    toCheck.addLast(new Pair<>(new Pair<>(up, dist + 1), up));
                } else {
                    toCheck.addLast(new Pair<>(new Pair<>(up, dist + 1), from));
                }
            }
            if (!seen.contains(left) && b.getPos(left) == Terrain.G && !occupied(left, us)) {
                if (from == null) {
                    toCheck.addLast(new Pair<>(new Pair<>(left, dist + 1), left));
                } else {
                    toCheck.addLast(new Pair<>(new Pair<>(left, dist + 1), from));
                }
            }
            if (!seen.contains(right) && b.getPos(right) == Terrain.G && !occupied(right, us)) {
                if (from == null) {
                    toCheck.addLast(new Pair<>(new Pair<>(right, dist + 1), right));
                } else {
                    toCheck.addLast(new Pair<>(new Pair<>(right, dist + 1), from));
                }
            }
            if (!seen.contains(down) && b.getPos(down) == Terrain.G && !occupied(down, us)) {
                if (from == null) {
                    toCheck.addLast(new Pair<>(new Pair<>(down, dist + 1), down));
                } else {
                    toCheck.addLast(new Pair<>(new Pair<>(down, dist + 1), from));
                }
            }

            seen.add(p);
        }

        return bestMove;
    }

    public static int prio(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        if (a.snd() < b.snd()) {
            return -1;
        } else if (a.snd() == b.snd()) {
            if (a.fst() < b.fst()) {
                return -1;
            } else if (a.fst() == b.fst()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public static long helper() {
        return 1;
    }

    public static String problem2(long x) {
        return "1";
    }
}

