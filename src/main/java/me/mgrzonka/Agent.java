package me.mgrzonka;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Agent implements Runnable{
    List<Connection> route;
    Iterator<Connection> routeIter;

    Connection currentConnection;

    Node currentNode;

    double startTime;
    double endTime;

    public Agent(List<Connection> route) {
        this.route = route;
        routeIter = route.iterator();
        currentConnection = routeIter.next();
        currentNode = currentConnection.from;
    }

    @Override
    public void run() {
        startTime = System.nanoTime();
        while (currentNode != route.get(route.size() - 1).to) {
            try {
                currentConnection.travel();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            currentNode = currentConnection.to;
            try {
                currentConnection = routeIter.next();
            } catch (NoSuchElementException e) {
                break;
            }
        }
        endTime = System.nanoTime();
    }

    public double getTotalTime() {
        return endTime - startTime;
    }
}
