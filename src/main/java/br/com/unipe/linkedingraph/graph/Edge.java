
package br.com.unipe.linkedingraph.graph;

import java.util.Objects;

/**
 * Represents a weighted connection between two locations
 * in the network.
 */
public class Edge {

    private final Vertex sourceVertex;
    private final Vertex targetVertex;
    private final int weight;

    public Edge(Vertex sourceVertex, Vertex targetVertex, int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException(
                    "Edge weight must be greater than zero.");
        }

        this.sourceVertex = Objects.requireNonNull(
                sourceVertex, "Source vertex cannot be null.");

        this.targetVertex = Objects.requireNonNull(
                targetVertex, "Target vertex cannot be null.");

        this.weight = weight;
    }

    public Vertex getSourceVertex() {
        return sourceVertex;
    }

    public Vertex getTargetVertex() {
        return targetVertex;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "%s -> %s (%d)".formatted(
                sourceVertex.getProfileName(),
                targetVertex.getProfileName(),
                weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Edge other = (Edge) obj;

        return sourceVertex.equals(other.sourceVertex)
                && targetVertex.equals(other.targetVertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceVertex, targetVertex);
    }
}
