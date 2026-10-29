
package br.com.unipe.linkedingraph.algorithm;

import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BFS implements GraphAlgorithm {

    private final Graph graph;

    public BFS(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String getName() {
        return "Campus Network Breadth-First Search";
    }

    @Override
    public PathResult execute(String sourceVName, String targetVName) {

        Vertex sourceVertex = graph.findVertex(sourceVName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Starting location not found: " + sourceVName));

        Vertex targetVertex = targetVName == null
                ? null
                : graph.findVertex(targetVName)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Destination not found: " + targetVName));

        Queue<Vertex> queue = new ArrayDeque<>();
        Set<Vertex> visited = new HashSet<>();
        Map<Vertex, Vertex> parentMap = new HashMap<>();

        visited.add(sourceVertex);
        queue.offer(sourceVertex);
        parentMap.put(sourceVertex, null);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();

            if (targetVertex != null && current.equals(targetVertex)) {
                List<String> path = buildPath(targetVertex, parentMap);
                return new PathResult(path, path.size() - 1);
            }

            List<Vertex> neighbours = new ArrayList<>(current.getAdjacency());
            neighbours.sort(Comparator.comparing(Vertex::getProfileName));

            for (Vertex neighbour : neighbours) {
                if (visited.add(neighbour)) {
                    parentMap.put(neighbour, current);
                    queue.offer(neighbour);
                }
            }
        }

        if (targetVertex == null) {
            List<String> reachableVertices = new ArrayList<>();

            for (Vertex vertex : visited) {
                reachableVertices.add(vertex.getProfileName());
            }

            return new PathResult(
                    reachableVertices,
                    Math.max(0, reachableVertices.size() - 1));
        }

        return PathResult.empty();
    }

    private List<String> buildPath(
            Vertex target,
            Map<Vertex, Vertex> parentMap) {

        LinkedList<String> path = new LinkedList<>();

        for (Vertex current = target;
             current != null;
             current = parentMap.get(current)) {
            path.addFirst(current.getProfileName());
        }

        return path;
    }
}
