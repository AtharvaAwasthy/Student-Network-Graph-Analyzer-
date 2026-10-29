
package br.com.unipe.linkedingraph;

import br.com.unipe.linkedingraph.algorithm.PathResult;
import br.com.unipe.linkedingraph.analyzer.ConnectionSuggestion;
import br.com.unipe.linkedingraph.analyzer.LinkedInAnalyzer;
import br.com.unipe.linkedingraph.graph.Graph;

import java.util.List;

public class LinkedInApp {

    public static void main(String[] args) {

        System.out.println("====================================================");
        System.out.println("       STUDENT COMMUNITY NETWORK ANALYZER");
        System.out.println("====================================================");

        // Phase 1: Build a sample student network.
        Graph studentNetwork = new Graph();

        studentNetwork.addVertices(
                "Aarav", "Diya", "Kabir", "Meera",
                "Rohan", "Ishita", "Arjun",
                "Nisha", "Vikram", "Priya", "Rahul"
        );

        // Main student community.
        studentNetwork.addEdge("Aarav", "Diya", 1);
        studentNetwork.addEdge("Aarav", "Kabir", 2);
        studentNetwork.addEdge("Aarav", "Meera", 8);
        studentNetwork.addEdge("Diya", "Rohan", 1);
        studentNetwork.addEdge("Kabir", "Rohan", 1);
        studentNetwork.addEdge("Meera", "Ishita", 5);
        studentNetwork.addEdge("Rohan", "Ishita", 1);
        studentNetwork.addEdge("Ishita", "Arjun", 1);

        // Two separate student groups.
        studentNetwork.addEdge("Nisha", "Vikram", 1);
        studentNetwork.addEdge("Priya", "Rahul", 1);

        System.out.println("\n[1] NETWORK OVERVIEW");
        System.out.println("Total students: "
                + studentNetwork.getVertices().size());
        System.out.println("Total connections: "
                + studentNetwork.getEdges().size());

        LinkedInAnalyzer analyzer =
                new LinkedInAnalyzer(studentNetwork);

        // Phase 2: Find connected components using DFS.
        System.out.println("\n[2] CONNECTED COMPONENT ANALYSIS");
        System.out.println(
                "Identifying separate groups in the network.");

        List<List<String>> groups = analyzer.isolatedGroups();

        System.out.println("Groups detected: " + groups.size());

        for (int i = 0; i < groups.size(); i++) {
            System.out.println(
                    "Group " + (i + 1) + ": " + groups.get(i));
        }

        // Phase 3: Find the minimum number of connections using BFS.
        System.out.println("\n[3] BREADTH-FIRST SEARCH (BFS)");

        int degrees =
                analyzer.degreeOfSeparation("Aarav", "Arjun");

        System.out.println("From: Aarav");
        System.out.println("To: Arjun");

        if (degrees >= 0) {
            System.out.println(
                    "Minimum number of connections: " + degrees);
        } else {
            System.out.println("No path exists between these students.");
        }

        // Phase 4: Find the minimum-cost route using Dijkstra.
        System.out.println("\n[4] DIJKSTRA'S ALGORITHM");

        PathResult route =
                analyzer.bestAffinityRoute("Aarav", "Ishita");

        if (route.hasPath()) {
            System.out.println("From: Aarav");
            System.out.println("To: Ishita");
            System.out.println("Selected path: " + route.path());
            System.out.println("Total path cost: " + route.totalCost());
        } else {
            System.out.println("No path exists between these students.");
        }

        // Phase 5: Recommend second-degree connections.
        System.out.println("\n[5] CONNECTION RECOMMENDATIONS");

        ConnectionSuggestion suggester =
                new ConnectionSuggestion(studentNetwork);

        System.out.println("Recommendations for Aarav:");

        var recommendations =
                suggester.suggestConnections("Aarav");

        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available.");
        } else {
            recommendations.forEach(s ->
                    System.out.println(
                            "- " + s.profileName()
                                    + " (" + s.mutualConnections()
                                    + " mutual connection(s))"));
        }

        System.out.println("\n====================================================");
        System.out.println("                 ANALYSIS COMPLETE");
        System.out.println("====================================================");
    }
}
