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
public class Day13 {
    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day13.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(handler(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day13.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            //System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        HashMap<Position, Track> board = new HashMap();
        int numCarts = 0;
        Cart[] carts = new Cart[17];
        for (int j = 0; j < events.length; j++) {
            for (int i = 0; i < events[j].length(); i++) {
                Position p = new Position(i,j);
                switch (events[j].charAt(i)) {
                    case '|':
                        board.put(p, Track.V);
                        break;
                    case '/':
                        board.put(p, Track.F);
                        break;
                    case '-':
                        board.put(p, Track.H);
                        break;
                    case '\\':
                        board.put(p, Track.B);
                        break;
                    case '+':
                        board.put(p, Track.INT);
                        break;
                    case '^':
                        board.put(p, Track.V);
                        carts[numCarts] = new Cart(new Position(i,j), Direction.UP);
                        numCarts++;
                        break;
                    case '>':
                        board.put(p, Track.H);
                        carts[numCarts] = new Cart(new Position(i,j), Direction.RIGHT);
                        numCarts++;
                        break;
                    case '<':
                        board.put(p, Track.H);
                        carts[numCarts] = new Cart(new Position(i,j), Direction.LEFT);
                        numCarts++;
                        break;
                    case 'v':
                        board.put(p, Track.V);
                        carts[numCarts] = new Cart(new Position(i,j), Direction.DOWN);
                        numCarts++;
                        break;
                }
            }
        }

        return problem1(carts, board);
    }

    public static String problem1(Cart[] carts, HashMap<Position, Track> tracks) {
        Position crash = null;
        Comparator<Cart> comparator =
                (Cart a, Cart b) -> ((a.p.y - b.p.y) * 400) + (a.p.x - b.p.x);

        HashSet<Position> newPos = new HashSet();
        while (true) {
            PriorityQueue<Cart> q = new PriorityQueue<>(carts.length, comparator);
            for (Cart c : carts) {
                if (!c.crashed) {
                    q.add(c);
                }
            }

            if (q.size() == 1) {
                Cart c = q.poll();
                return String.format("Last cart pos: %d,%d\n", c.p.x, c.p.y);
            }

            while (!q.isEmpty()) {
                Cart c = q.poll();
                Position newCrash = c.move(newPos, tracks);
                if (newCrash != null) {
                    System.out.printf("Saw crash: %d,%d\n", newCrash.x, newCrash.y);

                    // Remove all carts on crashed square
                    if (!newPos.remove(newCrash)) {
                        System.out.println("Nothing removed after crash");
                    }

                    for (Cart cart : carts) {
                        if (cart.p.equals(newCrash)) {
                            cart.crashed = true;
                        }
                    }

                    if (crash == null) {
                        crash = newCrash;
                    }
                }
            }
        }
    }

    private enum Track {V, F, H, B, INT}

    private static class Cart {
        Turn next;
        Direction facing;
        Position p;
        boolean crashed;

        public Cart(Position pos, Direction d) {
            next = Turn.LEFT;
            facing = d;
            p = pos;
            crashed = false;
        }

        public void turn() {
            switch (next) {
                case RIGHT:
                    switch (facing) {
                        case RIGHT:
                            facing = Direction.DOWN;
                            break;
                        case DOWN:
                            facing = Direction.LEFT;
                            break;
                        case LEFT:
                            facing = Direction.UP;
                            break;
                        case UP:
                            facing = Direction.RIGHT;
                            break;
                    }
                    break;
                case LEFT:
                    switch (facing) {
                        case RIGHT:
                            facing = Direction.UP;
                            break;
                        case DOWN:
                            facing = Direction.RIGHT;
                            break;
                        case LEFT:
                            facing = Direction.DOWN;
                            break;
                        case UP:
                            facing = Direction.LEFT;
                            break;
                    }
                    break;
            }
        }

        public void turnAtIntersection() {
            turn();

            switch (next) {
                case LEFT:
                    next = Turn.STRAIGHT;
                    break;
                case STRAIGHT:
                    next = Turn.RIGHT;
                    break;
                case RIGHT:
                    next = Turn.LEFT;
                    break;
            }
        }

        public Position move(Set<Position> pos, HashMap<Position, Track> board) {

            // Remove ourselves from the positions before we move
            pos.remove(p);

            // Someone crashed into us, so don't move
            if (crashed) {
                return null;
            }

            switch (facing) {
                case UP:
                    p.y--;
                    break;
                case DOWN:
                    p.y++;
                    break;
                case LEFT:
                    p.x--;
                    break;
                case RIGHT:
                    p.x++;
                    break;
            }

            // Ensure we moved to a legal place
            if (board.get(p) == null) {
                System.out.printf("Failed to get track at position: %d,%d", p.x, p.y);
            }

            // Possibly turn cart
            switch (board.get(p)) {
                case B:
                    if (facing == Direction.UP) {
                        facing = Direction.LEFT;
                    } else if (facing == Direction.RIGHT) {
                        facing = Direction.DOWN;
                    } else if (facing == Direction.DOWN) {
                        facing = Direction.RIGHT;
                    } else if (facing == Direction.LEFT) {
                        facing = Direction.UP;
                    }
                    break;
                case F:
                    if (facing == Direction.UP) {
                        facing = Direction.RIGHT;
                    } else if (facing == Direction.LEFT) {
                        facing = Direction.DOWN;
                    } else if (facing == Direction.DOWN) {
                        facing = Direction.LEFT;
                    } else if (facing == Direction.RIGHT) {
                        facing = Direction.UP;
                    }
                    break;
                case INT:
                    turnAtIntersection();
                    break;
            }

            // If adding fails, then we crashed
            if (pos.add(p)) {
                return null;
            } else {
                return p;
            }
        }
    }

    private static class Position {
        int x;
        int y;

        Position (int xpos, int ypos) {
            x = xpos;
            y = ypos;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position that = (Position) o;
            return (x == that.x && y == that.y);
        }

        public int hashCode() {
            return 10000 * y + x;
        }
    }

    private enum Turn {LEFT, STRAIGHT, RIGHT}
    private enum Direction {LEFT, RIGHT, UP, DOWN}

    public static long helper() {
        return 1;
    }

    public static String problem2(long x) {
        return "1";
    }
}

