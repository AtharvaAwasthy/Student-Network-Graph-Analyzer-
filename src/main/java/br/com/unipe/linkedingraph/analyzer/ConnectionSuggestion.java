
package br.com.unipe.linkedingraph.analyzer;

import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Recommends campus community connections using a two-level
 * graph traversal and mutual-connection counts.
 */
public class ConnectionSuggestion {

    private final Graph graph;

    public ConnectionSuggestion(Graph graph) {
        this.graph = graph;
    }

    /**
     * Suggests profiles connected through shared acquaintances.
     *
     * Rules:
     * - Exclude the source profile.
     * - Exclude existing direct connections.
     * - Rank candidates by the number of mutual connections.
     *
     * @param profileName Name of the profile being analyzed.
     * @return Suggested profiles and their mutual connection counts.
     */
    public List<SuggestionResult> suggestConnections(String profileName) {

        Vertex sourceVertex = graph.findVertex(profileName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Profile not found: " + profileName));

        List<Vertex> directConnections = sourceVertex.getAdjacency();
        Map<Vertex, Integer> mutualConnectionCounts = new HashMap<>();

        // Explore the connections of each direct connection.
        for (Vertex connection : directConnections) {

            List<Vertex> secondLevelConnections =
                    connection.getAdjacency().stream()
                            .sorted(Comparator.comparing(
                                    Vertex::getProfileName))
                            .toList();

            for (Vertex candidate : secondLevelConnections) {

                if (candidate.equals(sourceVertex)) {
                    continue;
                }

                if (directConnections.contains(candidate)) {
                    continue;
                }

                mutualConnectionCounts.merge(candidate, 1, Integer::sum);
            }
        }

        List<SuggestionResult> suggestions = new ArrayList<>();

        for (Map.Entry<Vertex, Integer> entry
                : mutualConnectionCounts.entrySet()) {

            suggestions.add(new SuggestionResult(
                    entry.getKey().getProfileName(),
                    entry.getValue()));
        }

        // Rank by mutual connections, using the name to break ties.
        suggestions.sort(
                Comparator.comparingInt(
                                SuggestionResult::mutualConnections)
                        .reversed()
                        .thenComparing(SuggestionResult::profileName));

        return suggestions;
    }

    /**
     * Stores a suggested profile and its mutual connection count.
     */
    public record SuggestionResult(
            String profileName,
            int mutualConnections) {
    }
}
