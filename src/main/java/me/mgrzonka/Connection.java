package me.mgrzonka;

public class Connection {
    Node from;
    Node to;

    long travel_time; // in ms

    int max_capacity;
    int current_capacity;

    public Connection(Node from, Node to, long duration, int max_capacity) {
        this.from = from;
        this.to = to;
        this.travel_time = duration;
        this.max_capacity = max_capacity;
        this.current_capacity = 0;
    }

    public void travel() throws InterruptedException {
        synchronized (this) {
            if(current_capacity == max_capacity) {
                wait();
            }
            current_capacity += 1;
        }
        Thread.sleep(travel_time);
        synchronized (this) {
            current_capacity -= 1;
            notifyAll();
        }
    }

    public boolean is_full() {
        return current_capacity == max_capacity;
    }
}
