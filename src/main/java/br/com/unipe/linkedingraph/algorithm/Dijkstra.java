
package br.com.unipe.linkedingraph.algorithm;

import br.com.unipe.linkedingraph.graph.Edge;
import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import java.util.*;

public class Dijkstra implements GraphAlgorithm {

    private final Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String getName() {
        return "Campus Route - Dijkstra's Algorithm";
    }

    @Override
    public PathResult execute(String sourceVName, String targetVName) {

        if (targetVName == null) {
            throw new IllegalArgumentException(
                    "Dijkstra's algorithm requires a destination.");
        }

        Vertex sourceVertex = graph.findVertex(sourceVName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Starting location not found: " + sourceVName));

        Vertex targetVertex = graph.findVertex(targetVName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Destination not found: " + targetVName));

        Map<Vertex, Integer> distances = new HashMap<>();
        Map<Vertex, Vertex> previousVertices = new HashMap<>();
        Set<Vertex> visitedVertices = new HashSet<>();

        PriorityQueue<DijkstraNode> queue = new PriorityQueue<>(
                Comparator.comparingInt(DijkstraNode::distance));

        for (Vertex vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }

        distances.put(sourceVertex, 0);
        queue.offer(new DijkstraNode(sourceVertex, 0));

        while (!queue.isEmpty()) {
            DijkstraNode currentNode = queue.poll();
            Vertex current = currentNode.vertex();

            if (visitedVertices.contains(current)) {
                continue;
            }

            visitedVertices.add(current);

            if (current.equals(targetVertex)) {
                break;
            }

            for (Edge edge : graph.getIncidentEdges(current)) {

                if (edge.getWeight() < 0) {
                    throw new IllegalArgumentException(
                            "Dijkstra's algorithm cannot process negative edge weights.");
                }

                Vertex neighbour =
                        graph.getOppositeVertex(edge, current);

                if (visitedVertices.contains(neighbour)) {
                    continue;
                }

                int currentDistance = distances.get(current);
                int edgeWeight = edge.getWeight();

                if (currentDistance > Integer.MAX_VALUE - edgeWeight) {
                    continue;
                }

                int newDistance = currentDistance + edgeWeight;

                if (newDistance < distances.get(neighbour)) {
                    distances.put(neighbour, newDistance);
                    previousVertices.put(neighbour, current);

                    queue.offer(new DijkstraNode(neighbour, newDistance));
                }
            }
        }

        if (distances.get(targetVertex) == Integer.MAX_VALUE) {
            return PathResult.empty();
        }

        LinkedList<String> path = new LinkedList<>();

        for (Vertex current = targetVertex;
             current != null;
             current = previousVertices.get(current)) {
            path.addFirst(current.getProfileName());
        }

        return new PathResult(path, distances.get(targetVertex));
    }

    private record DijkstraNode(Vertex vertex, int distance) {}
}
