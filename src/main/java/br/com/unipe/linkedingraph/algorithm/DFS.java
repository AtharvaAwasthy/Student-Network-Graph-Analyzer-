
package br.com.unipe.linkedingraph.algorithm;

import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

public class DFS implements GraphAlgorithm {

    private final Graph graph;

    public DFS(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String getName() {
        return "Campus Network Depth-First Search";
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

        Stack<Vertex> stack = new Stack<>();
        Set<Vertex> visited = new HashSet<>();
        Map<Vertex, Vertex> parentMap = new HashMap<>();

        visited.add(sourceVertex);
        stack.push(sourceVertex);
        parentMap.put(sourceVertex, null);

        while (!stack.isEmpty()) {
            Vertex current = stack.peek();

            if (targetVertex != null && current.equals(targetVertex)) {
                List<String> path = new ArrayList<>();

                for (Vertex vertex : stack) {
                    path.add(vertex.getProfileName());
                }

                return new PathResult(path, path.size() - 1);
            }

            List<Vertex> neighbours =
                    new ArrayList<>(current.getAdjacency());

            neighbours.sort(
                    Comparator.comparing(Vertex::getProfileName));

            Optional<Vertex> nextVertex = neighbours.stream()
                    .filter(vertex -> !visited.contains(vertex))
                    .findFirst();

            if (nextVertex.isPresent()) {
                Vertex next = nextVertex.get();

                visited.add(next);
                parentMap.put(next, current);
                stack.push(next);
            } else {
                stack.pop();
            }
        }

        if (targetVertex == null) {
            List<String> reachableVertices = visited.stream()
                    .map(Vertex::getProfileName)
                    .toList();

            return new PathResult(
                    reachableVertices,
                    Math.max(0, reachableVertices.size() - 1));
        }

        return PathResult.empty();
    }
}
