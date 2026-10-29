
package br.com.unipe.linkedingraph.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a vertex in the network.
 * Each vertex stores its name and its adjacent vertices.
 */
public class Vertex {

    private final String profileName;
    private final List<Vertex> adjacency;

    public Vertex(String profileName) {
        if (profileName == null || profileName.isBlank()) {
            throw new IllegalArgumentException(
                    "Vertex name cannot be empty.");
        }

        this.profileName = profileName.trim();
        this.adjacency = new ArrayList<>();
    }

    // Adds a neighbouring vertex within the graph package.
    void addAdjacency(Vertex vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException(
                    "Adjacent vertex cannot be null.");
        }

        if (vertex.equals(this)) {
            throw new IllegalArgumentException(
                    "A vertex cannot be adjacent to itself.");
        }

        if (!adjacency.contains(vertex)) {
            adjacency.add(vertex);
        }
    }

    @Override
    public String toString() {
        return profileName;
    }

    public String getProfileName() {
        return profileName;
    }

    public int getDegree() {
        return adjacency.size();
    }

    public List<Vertex> getAdjacency() {
        return Collections.unmodifiableList(adjacency);
    }

    public String getDegreeInfo() {
        return "\n%s: degree %d".formatted(
                profileName, getDegree());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Vertex other = (Vertex) obj;

        return profileName.equalsIgnoreCase(other.profileName);
    }

    @Override
    public int hashCode() {
        return profileName.toLowerCase(
                java.util.Locale.ROOT).hashCode();
    }
}
