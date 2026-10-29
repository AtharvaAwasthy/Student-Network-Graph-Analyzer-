
package br.com.unipe.linkedingraph.analyzer;

import br.com.unipe.linkedingraph.algorithm.BFS;
import br.com.unipe.linkedingraph.algorithm.DFS;
import br.com.unipe.linkedingraph.algorithm.Dijkstra;
import br.com.unipe.linkedingraph.algorithm.PathResult;
import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides analysis operations for a connected network.
 * Supports shortest-hop queries, minimum-cost paths,
 * and connected-component discovery.
 */
public class LinkedInAnalyzer {

    private final Graph graph;

    public LinkedInAnalyzer(Graph graph) {
        this.graph = graph;
    }

    /**
     * Calculates the minimum number of edges between two profiles.
     *
     * @param originName starting profile
     * @param targetName destination profile
     * @return minimum number of edges, or -1 if no path exists
     */
    public int degreeOfSeparation(
            String originName,
            String targetName) {

        BFS bfs = new BFS(graph);
        PathResult result = bfs.execute(originName, targetName);

        return result.totalCost();
    }

    /**
     * Finds the minimum-cost path between two profiles.
     * Edge weights must be non-negative.
     *
     * @param originName starting profile
     * @param targetName destination profile
     * @return path and accumulated cost, or an empty result
     */
    public PathResult bestAffinityRoute(
            String originName,
            String targetName) {

        Dijkstra dijkstra = new Dijkstra(graph);
        return dijkstra.execute(originName, targetName);
    }

    /**
     * Identifies the connected components of the network.
     *
     * @return a list containing the profiles in each component
     */
    public List<List<String>> isolatedGroups() {

        List<List<String>> connectedComponents = new ArrayList<>();
        Set<String> visitedProfiles = new HashSet<>();

        DFS dfs = new DFS(graph);

        for (Vertex vertex : graph.getVertices()) {

            String profileName = vertex.getProfileName();

            if (!visitedProfiles.contains(profileName)) {

                PathResult result = dfs.execute(profileName, null);
                List<String> group = result.path();

                connectedComponents.add(group);
                visitedProfiles.addAll(group);
            }
        }

        return connectedComponents;
    }
}
