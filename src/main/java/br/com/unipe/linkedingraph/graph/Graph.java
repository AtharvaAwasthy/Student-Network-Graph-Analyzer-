
package br.com.unipe.linkedingraph.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Represents an undirected weighted graph.
 * Vertices represent locations or profiles, while edges represent
 * connections between them.
 */
public class Graph {

    private final List<Edge> edges;
    private final List<Vertex> vertices;
    private int order;
    private int size;

    public Graph() {
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    public void addVertices(String... names) {
        for (String name : names) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(
                        "Vertex name cannot be empty.");
            }

            if (findVertex(name).isPresent()) {
                throw new IllegalArgumentException(
                        "Vertex already exists: " + name);
            }

            vertices.add(new Vertex(name));
            order++;
        }
    }

    public void addEdge(String v1, String v2, int weight) {
        edges.add(createEdge(v1, v2, weight));
    }

    private Edge createEdge(
            String vertex1Name,
            String vertex2Name,
            Integer weight) {

        Vertex v1 = findVertex(vertex1Name)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vertex not found: " + vertex1Name));

        Vertex v2 = findVertex(vertex2Name)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vertex not found: " + vertex2Name));

        if (v1.equals(v2)) {
            throw new IllegalArgumentException(
                    "Self-connections are not supported.");
        }

        Edge edge = new Edge(v1, v2, weight);

        if (edges.contains(edge)) {
            throw new IllegalArgumentException(
                    "Connection already exists between these vertices.");
        }

        updateAdjacencies(v1, v2);
        size++;

        return edge;
    }

    private void updateAdjacencies(Vertex v1, Vertex v2) {
        v1.addAdjacency(v2);
        v2.addAdjacency(v1);
    }

    public Optional<Vertex> findVertex(String name) {
        if (name == null) {
            return Optional.empty();
        }

        for (Vertex vertex : vertices) {
            if (vertex.getProfileName().equalsIgnoreCase(name)) {
                return Optional.of(vertex);
            }
        }

        return Optional.empty();
    }

    public String formatVerticesDegreeInfo() {
        StringBuilder degrees = new StringBuilder();

        for (Vertex vertex : vertices) {
            degrees.append(vertex.getDegreeInfo());
        }

        return degrees.toString();
    }

    public String formatAdjacencyInfo() {
        StringBuilder adjacencyInfo = new StringBuilder();

        for (Vertex vertex : vertices) {
            adjacencyInfo.append("\n")
                    .append(vertex.getProfileName())
                    .append(": ")
                    .append(vertex.getAdjacency());
        }

        return adjacencyInfo.toString();
    }

    public void displayAdjacencyMatrix() {
        List<Vertex> orderedVertices = vertices.stream()
                .sorted(Comparator.comparing(Vertex::getProfileName))
                .toList();

        StringBuilder matrix =
                new StringBuilder("\nAdjacency Matrix\n");

        matrix.append("\t");

        orderedVertices.forEach(vertex ->
                matrix.append(vertex.getProfileName()).append("\t"));

        matrix.append("\n");

        for (Vertex vertex : orderedVertices) {
            matrix.append(vertex.getProfileName()).append("\t");

            List<Vertex> adjacency = vertex.getAdjacency();

            for (Vertex otherVertex : orderedVertices) {
                matrix.append(adjacency.contains(otherVertex) ? "1" : "0")
                        .append("\t");
            }

            matrix.append("\n");
        }

        System.out.println(matrix);
    }

    public void displayIncidenceMatrix() {
        List<Vertex> orderedVertices = vertices.stream()
                .sorted(Comparator.comparing(Vertex::getProfileName))
                .toList();

        StringBuilder matrix =
                new StringBuilder("\nIncidence Matrix\n\t");

        edges.forEach(edge ->
                matrix.append("%s-%s".formatted(
                        edge.getSourceVertex().getProfileName(),
                        edge.getTargetVertex().getProfileName()
                )).append("\t"));

        matrix.append("\n");

        for (Vertex vertex : orderedVertices) {
            matrix.append(vertex.getProfileName()).append("\t");

            for (Edge edge : edges) {
                boolean isSource =
                        edge.getSourceVertex().equals(vertex);
                boolean isTarget =
                        edge.getTargetVertex().equals(vertex);

                String value;

                if (isSource && isTarget) {
                    value = "2";
                } else if (isSource || isTarget) {
                    value = "1";
                } else {
                    value = "0";
                }

                matrix.append(value).append("\t");
            }

            matrix.append("\n");
        }

        System.out.println(matrix);
    }

 
    public int getPathSize(String... path) {
        int totalWeight = 0;
        List<Edge> visitedEdges = new ArrayList<>();

        for (int i = 0; i < path.length - 1; i++) {
            String sourceName = path[i];
            String targetName = path[i + 1];

            Vertex source = findVertex(sourceName)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Vertex not found: " + sourceName));

            Vertex target = findVertex(targetName)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Vertex not found: " + targetName));

            Edge edge = getEdgesToNeighbor(source, target).stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No connection exists between "
                                    + sourceName + " and " + targetName));

            if (visitedEdges.contains(edge)) {
                throw new IllegalArgumentException(
                        "The path contains a repeated edge.");
            }

            visitedEdges.add(edge);
            totalWeight += edge.getWeight();
        }

        return totalWeight;
    }


    private List<Edge> getEdgesToNeighbor(
            Vertex current,
            Vertex neighbor) {

        return edges.stream()
                .filter(edge ->
                        (edge.getSourceVertex().equals(current)
                                && edge.getTargetVertex().equals(neighbor))
                        || (edge.getTargetVertex().equals(current)
                                && edge.getSourceVertex().equals(neighbor)))
                .toList();
    }

    public List<Vertex> getNeighbors(Vertex vertex) {
        return vertex.getAdjacency();
    }

    public Vertex getOppositeVertex(Edge edge, Vertex vertex) {
        if (edge.getSourceVertex().equals(vertex)) {
            return edge.getTargetVertex();
        }

        if (edge.getTargetVertex().equals(vertex)) {
            return edge.getSourceVertex();
        }

        throw new IllegalArgumentException(
                "The vertex does not belong to this edge.");
    }

    public List<Edge> getOutgoingEdges(Vertex vertex) {
        return edges.stream()
                .filter(edge -> edge.getSourceVertex().equals(vertex))
                .toList();
    }

    public List<Edge> getIncidentEdges(Vertex vertex) {
        return edges.stream()
                .filter(edge -> edge.getSourceVertex().equals(vertex)
                        || edge.getTargetVertex().equals(vertex))
                .toList();
    }

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    @Override
    public String toString() {
        return """
                {
                order = %d,
                size = %d,
                vertices = %s,
                edges = %s,
                degrees = %s,
                adjacency = %s
                }
                """.formatted(
                order,
                size,
                vertices,
                edges,
                formatVerticesDegreeInfo(),
                formatAdjacencyInfo());
    }
}
