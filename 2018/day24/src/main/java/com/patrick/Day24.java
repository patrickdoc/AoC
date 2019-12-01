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
public class Day24 {

    private static int BOOST = 46;

    public static void main(String[] args) {
        String fileName = "/input.txt";

        try (InputStreamReader in = new InputStreamReader(Day24.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem1(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStreamReader in = new InputStreamReader(Day24.class.getResourceAsStream(fileName))) {
            Stream<String> stream = new BufferedReader(in).lines();
            System.out.println(problem2(handler(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Group> handler(Stream<String> lines) {
        String[] events = lines.toArray(String[]::new);
        Side side = null;
        List<Group> groups = new ArrayList<>();
        for (String s : events) {
            if (s.equals("Immune System:")) {
                side = Side.IMMUNE;
            } else if (s.equals("Infection:")) {
                side = Side.INFECTION;
            } else if (s.equals("")) {
                continue;
            } else {
                groups.add(new Group(s, side));
            }
        }


        return groups;
    }

    private enum Side {IMMUNE, INFECTION}
    private enum AttackType {BLUDGEON, SLASH, FIRE, COLD, RADIATION}

    private static class Group {
        int count;
        int hp;
        int attack;
        AttackType at;
        int init;
        Set<AttackType> weak;
        Set<AttackType> immun;
        Side side;

        public Group(int c, int h, int a, AttackType t, int i, String[] w, String[] im, Side si) {
            count = c;
            hp = h;
            attack = a;
            at = t;
            init = i;
            weak = new HashSet<>();
            for (String s : w) {
                weak.add(strToAttack(s));
            }
            immun = new HashSet<>();
            for (String s : im) {
                immun.add(strToAttack(s));
            }
            side = si;
        }

        private Group(String s, Side si) {
            side = si;
            String noRes    = "(\\d+) units each with (\\d+) hit points with an attack that does (\\d+) ([^ ]+) damage at initiative (\\d+)";
            String weakStr  = "(\\d+) units each with (\\d+) hit points \\(weak to ([^);]+)\\) with an attack that does (\\d+) ([^ ]+) damage at initiative (\\d+)";
            String weakImmun = "(\\d+) units each with (\\d+) hit points \\(weak to ([^);]+); immune to ([^);]+)\\) with an attack that does (\\d+) ([^ ]+) damage at initiative (\\d+)";
            String immunWeak = "(\\d+) units each with (\\d+) hit points \\(immune to ([^);]+); weak to ([^);]+)\\) with an attack that does (\\d+) ([^ ]+) damage at initiative (\\d+)";
            String immunStr = "(\\d+) units each with (\\d+) hit points \\(immune to ([^);]+)\\) with an attack that does (\\d+) ([^ ]+) damage at initiative (\\d+)";

            Matcher noResM = Pattern.compile(noRes).matcher(s);
            Matcher weakM = Pattern.compile(weakStr).matcher(s);
            Matcher weakImmunM = Pattern.compile(weakImmun).matcher(s);
            Matcher immunWeakM = Pattern.compile(immunWeak).matcher(s);
            Matcher immunM = Pattern.compile(immunStr).matcher(s);


            // Things to setup
            int c = -1;
            int h = -1;
            int a = -1;
            AttackType t = null;
            int i = -1;
            String[] w = new String[0];
            String[] im = new String[0];

            if (noResM.matches()) {
                Matcher matcher = noResM;

                c = Integer.parseInt(matcher.group(1));
                h = Integer.parseInt(matcher.group(2));
                a = Integer.parseInt(matcher.group(3));
                t = strToAttack(matcher.group(4));
                i = Integer.parseInt(matcher.group(5));

            } else if (weakM.matches()) {
                Matcher matcher = weakM;

                c = Integer.parseInt(matcher.group(1));
                h = Integer.parseInt(matcher.group(2));
                a = Integer.parseInt(matcher.group(4));
                t = strToAttack(matcher.group(5));
                i = Integer.parseInt(matcher.group(6));
                w = matcher.group(3).split(", ");

            } else if (weakImmunM.matches()) {
                Matcher matcher = weakImmunM;

                c = Integer.parseInt(matcher.group(1));
                h = Integer.parseInt(matcher.group(2));
                a = Integer.parseInt(matcher.group(5));
                t = strToAttack(matcher.group(6));
                i = Integer.parseInt(matcher.group(7));
                w = matcher.group(3).split(", ");
                im = matcher.group(4).split(", ");

            } else if (immunWeakM.matches()) {
                Matcher matcher = immunWeakM;

                c = Integer.parseInt(matcher.group(1));
                h = Integer.parseInt(matcher.group(2));
                a = Integer.parseInt(matcher.group(5));
                t = strToAttack(matcher.group(6));
                i = Integer.parseInt(matcher.group(7));
                w = matcher.group(4).split(", ");
                im = matcher.group(3).split(", ");

            } else if (immunM.matches()) {
                Matcher matcher = immunM;

                c = Integer.parseInt(matcher.group(1));
                h = Integer.parseInt(matcher.group(2));
                a = Integer.parseInt(matcher.group(4));
                t = strToAttack(matcher.group(5));
                i = Integer.parseInt(matcher.group(6));
                im = matcher.group(3).split(", ");

            } else {
                System.out.printf("Could not parse input: %s\n", s);
            }

            count = c;
            hp = h;
            if (side == Side.IMMUNE) {
                a += BOOST;
            }
            attack = a;
            at = t;
            init = i;
            weak = new HashSet<>();
            for (String weakness : w) {
                weak.add(strToAttack(weakness));
            }
            immun = new HashSet<>();
            for (String immunity : im) {
                immun.add(strToAttack(immunity));
            }
        }

        public int effectivePower() {
            return count * attack;
        }

        public int dmg(Group enemy) {
            int factor = 1;
            if (enemy.weak.contains(at)) {
                factor = 2;
            } else if (enemy.immun.contains(at)) {
                factor = 0;
            }

            return factor * effectivePower();
        }

        // Return number of units to kill
        public int attackGroup(Group enemy) {
            int remainingUnits = enemy.count - (dmg(enemy) / enemy.hp);
            remainingUnits = remainingUnits > 0 ? remainingUnits : 0;

            return enemy.count - remainingUnits;
        }
    }

    private static AttackType strToAttack(String s) {
        if (s.equals("cold")) {
            return AttackType.COLD;
        } else if (s.equals("fire")) {
            return AttackType.FIRE;
        } else if (s.equals("slashing")) {
            return AttackType.SLASH;
        } else if (s.equals("radiation")) {
            return AttackType.RADIATION;
        } else if (s.equals("bludgeoning")) {
            return AttackType.BLUDGEON;
        } else {
            System.out.printf("Unknown attack type encountered %s\n", s);
            return null;
        }
    }

    private static String problem1(List<Group> groups) {

        Comparator<Group> c = Comparator.comparingInt(x -> -(x.effectivePower() * 100 + x.init));
        PriorityQueue<Group> selectQ = new PriorityQueue<>(c);

        Comparator<Group> attackC = Comparator.comparingInt(x -> -x.init);
        PriorityQueue<Group> attackQ = new PriorityQueue<>(attackC);
        Set<Group> chosen;

        HashMap<Group, Group> targets;

        // While there are people to fight
        while (helper(groups)) {
            // Select targets
            chosen = new HashSet<>();
            targets = new HashMap<>();
            selectQ.addAll(groups);
            while (!selectQ.isEmpty()) {
                Group g = selectQ.poll();
                Group target = null;
                int maxDmg = -1;
                for (Group e : groups) {
                    if (g.side != e.side && !chosen.contains(e)) {
                        int damage = g.dmg(e);
                        boolean update = false;
                        if (damage > maxDmg) {
                            update = true;
                        } else if (damage == maxDmg && damage > 0) {
                            if (e.effectivePower() > target.effectivePower()) {
                                update = true;
                            } else if (e.effectivePower() == target.effectivePower()) {
                                if (e.init > target.init) {
                                    update = true;
                                }
                            }
                        }
                        if (update) {
                            maxDmg = damage;
                            target = e;
                        }
                    }
                }
                // Target selected: e
                if (maxDmg > 0) {
                    chosen.add(target);
                    targets.put(g, target);
                }
            }

            // Attack
            attackQ.addAll(groups);
            while (!attackQ.isEmpty()) {
                Group attacking = attackQ.poll();

                Group target = targets.get(attacking);
                if (target != null) {
                    System.out.printf("enemy units killed: %d\n", attacking.attackGroup(target));
                    target.count -= attacking.attackGroup(target);
                }
            }

            // Clear dead groups
            for (int i = 0; i < groups.size(); i++) {
                if (groups.get(i).count <= 0) {
                    groups.remove(i);
                    i--;
                }
            }

        }

        int count = 0;
        for (Group g : groups) {
            count += g.count;
        }
        return String.format("Winning army has %d units remaining\n", count);
    }


    // Return true as long as both armies have someone to fight
    public static boolean helper(List<Group> groups) {

        // infection armies exist
        int infec = 0;
        // immune system armies exist
        int immun = 0;

        for (Group g : groups) {
            if (g.side == Side.INFECTION) {
                System.out.printf("Infection group has: %d units\n", g.count);
                infec += g.count;
            } else {
                System.out.printf("Immune system group has: %d units\n", g.count);
                immun += g.count;
            }
        }

        System.out.printf("Infection units: %d\n", infec);
        System.out.printf("Immune system units: %d\n\n", immun);
        return infec > 0 && immun > 0;
    }

    private static String problem2(List<Group> groups) {
        return "1";
    }
}

