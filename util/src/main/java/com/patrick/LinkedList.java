package com.patrick;

public class LinkedList<T> {
    LinkedList<T> prev;
    LinkedList<T> next;
    T val;

    public LinkedList(T val) {
        this.prev = null;
        this.next = null;
        this.val = val;
    }

    public LinkedList<T> getFirst() {
        LinkedList<T> current = this;
        LinkedList<T> seeker = current;
        while (seeker.prev != null) {
            seeker = seeker.prev;
            if (seeker == current) {
                return null;
            }
        }

        return seeker;
    }

    public LinkedList<T> getBefore() {
        return this.prev;
    }

    public LinkedList<T> getBefore(int dist) {
        if (dist == 0) {
            return this;
        } else {
            return this.prev.getBefore(dist - 1);
        }
    }

    public LinkedList<T> getLast() {
        LinkedList<T> current = this;
        LinkedList<T> seeker = current;
        while (seeker.next != null) {
            seeker = seeker.next;
            if (seeker == current) {
                return null;
            }
        }

        return seeker;
    }

    public LinkedList<T> getAfter() {
        return this.next;
    }

    public LinkedList<T> getAfter(int dist) {
        if (dist == 0) {
            return this;
        } else {
            return this.next.getAfter(dist - 1);
        }
    }

    public LinkedList<T> removeAfter() {
        if (this.next != null) {
            this.next.next.prev = this;
            this.next = this.next.next;
            return this;
        } else {
            return null;
        }
    }

    public void addBefore(T val) {
        LinkedList<T> newNode = new LinkedList(val);
        newNode.prev = this.prev;
        newNode.next = this;
        if (this.prev != null) {
            this.prev.next = newNode;
        }
        this.prev = newNode;
    }

    public void addAfter(T val) {
        LinkedList<T> newNode = new LinkedList(val);
        newNode.next = this.next;
        newNode.prev = this;
        if (this.next != null) {
            this.next.prev = newNode;
        }
        this.next = newNode;
    }

    public LinkedList<T> ring() {
        LinkedList<T> current = this;
        LinkedList<T> first = current;
        while (first.next != null) {
            first = first.next;
            if (first == current) {
                return null;
            }
        }

        LinkedList<T> last = current;
        while (last.next != null) {
            last = last.next;
            if (first == current) {
                return null;
            }
        }

        first.prev = last;
        last.next = first;
        return current;
    }
}
