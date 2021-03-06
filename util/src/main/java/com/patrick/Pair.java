package com.patrick;

public class Pair<S,T> {
    private S a;
    private T b;

    public Pair(S s, T t) {
        a = s;
        b = t;
    }

    public S fst() {
        return a;
    }

    public T snd() {
        return b;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair that = (Pair) o;
        return (a.equals(that.a) && b.equals(that.b));
    }

    // A horrible hashCode implementation. Should work for nums < 5000
    public int hashCode() {
        return b.hashCode() * 10000 + a.hashCode();
    }
}
