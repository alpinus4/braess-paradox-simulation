package me.mgrzonka;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static List<Connection> generateOptimalRoute(List<Connection> connections, Node startNode, Node endNode) {
        List<Connection> route = new ArrayList<>();

        Node currentNode = startNode;
        while(currentNode != endNode) {
            Connection connectionToGo = null;
            for (Connection connection :
                    connections) {
                if (connection.from != currentNode) {
                    continue;
                }

                if (connectionToGo == null) {
                    connectionToGo = connection;
                    continue;
                }

                if (connection.travel_time < connectionToGo.travel_time && !connection.is_full()) {
                    connectionToGo = connection;
                }
            }

            route.add(connectionToGo);
            currentNode = connectionToGo.to;
        }
        return route;
    }

    static double runSimulation(int numberOfAgents, long timeBetweenAgents, List<Connection> connections, Node startingNode, Node endingNode) throws InterruptedException {
        List<Agent> agents = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numberOfAgents; i++) {
            Agent agent = new Agent(generateOptimalRoute(connections, startingNode, endingNode));
            Thread thread = new Thread(agent);
            agents.add(agent);
            threads.add(thread);
            thread.start();
            Thread.sleep(timeBetweenAgents);
        }

        for (Thread thread :
                threads) {
            thread.join();
        }

        return agents.stream().map(Agent::getTotalTime).mapToDouble(value -> value).average().getAsDouble() / Math.pow(10, 9);
    }

    public static void main(String[] args) throws InterruptedException {
        int numberOfAgents = 1000;
        long timeBetweenAgents = 5;

        Node node_A = new Node("A");
        Node node_X = new Node("X");
        Node node_Y = new Node("Y");
        Node node_B = new Node("B");

        List<Connection> connections = new ArrayList<>();
        connections.add(new LocalConnection(node_A, node_Y));
        connections.add(new LocalConnection(node_X, node_B));
        connections.add(new HighwayConnection(node_A, node_X));
        connections.add(new HighwayConnection(node_Y, node_B));

        double averageTime = runSimulation(numberOfAgents, timeBetweenAgents, connections, node_A, node_B);
        connections.add(new Connection(node_Y, node_X, 45, 20));
        double averageTimeWithAdditionalRoad = runSimulation(numberOfAgents, timeBetweenAgents, connections, node_A, node_B);
        System.out.printf("Average time: %5.6fs%n", averageTime);
        System.out.printf("Average time with additional road: %5.6fs%n", averageTimeWithAdditionalRoad);
        System.out.printf("Difference: %5.3fms, %5.3f %% time increase", (averageTimeWithAdditionalRoad - averageTime) * 1000, averageTimeWithAdditionalRoad / averageTime);
    }
}