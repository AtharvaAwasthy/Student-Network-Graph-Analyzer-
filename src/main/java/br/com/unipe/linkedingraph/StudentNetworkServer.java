
package br.com.unipe.linkedingraph;

import br.com.unipe.linkedingraph.analyzer.ConnectionSuggestion;
import br.com.unipe.linkedingraph.analyzer.LinkedInAnalyzer;
import br.com.unipe.linkedingraph.graph.Edge;
import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.unipe.linkedingraph.algorithm.BFS;
import br.com.unipe.linkedingraph.algorithm.DFS;

public class StudentNetworkServer {

    private static final Graph graph = createGraph();
    private static final LinkedInAnalyzer analyzer = new LinkedInAnalyzer(graph);

    private static Graph createGraph() {
        Graph g = new Graph();

        g.addVertices(
                "Aarav", "Diya", "Kabir", "Meera",
                "Rohan", "Ishita", "Arjun",
                "Nisha", "Vikram", "Priya", "Rahul"
        );

        g.addEdge("Aarav", "Diya", 1);
        g.addEdge("Aarav", "Kabir", 2);
        g.addEdge("Aarav", "Meera", 8);
        g.addEdge("Diya", "Rohan", 1);
        g.addEdge("Kabir", "Rohan", 1);
        g.addEdge("Meera", "Ishita", 5);
        g.addEdge("Rohan", "Ishita", 1);
        g.addEdge("Ishita", "Arjun", 1);
        g.addEdge("Nisha", "Vikram", 1);
        g.addEdge("Priya", "Rahul", 1);

        return g;
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(
                new InetSocketAddress("localhost", 8080), 0
        );

        server.createContext("/api/network", exchange -> {
            if (!handleMethod(exchange, "GET")) return;

            String vertices = graph.getVertices().stream()
                    .map(v -> "{\"id\":\"" + escape(v.getProfileName())
                            + "\",\"label\":\"" + escape(v.getProfileName()) + "\"}")
                    .collect(Collectors.joining(","));

            String edges = graph.getEdges().stream()
                    .map(e -> edgeJson(e))
                    .collect(Collectors.joining(","));

            String json = "{\"students\":[" + vertices
                    + "],\"connections\":[" + edges
                    + "],\"studentCount\":" + graph.getVertices().size()
                    + ",\"connectionCount\":" + graph.getEdges().size()
                    + ",\"groups\":" + analyzer.isolatedGroups().size() + "}";

            sendJson(exchange, 200, json);
        });

        server.createContext("/api/groups", exchange -> {
            if (!handleMethod(exchange, "GET")) return;

            String groups = analyzer.isolatedGroups().stream()
                    .map(group -> "[" + group.stream()
                            .map(name -> "\"" + escape(name) + "\"")
                            .collect(Collectors.joining(",")) + "]")
                    .collect(Collectors.joining(","));

            sendJson(exchange, 200, "{\"groups\":[" + groups + "]}");
        });

        server.createContext("/api/path", exchange -> {
            if (!handleMethod(exchange, "GET")) return;

            Map<String, String> params = queryParams(exchange);
            String from = params.get("from");
            String to = params.get("to");

            if (from == null || to == null
                    || graph.findVertex(from).isEmpty()
                    || graph.findVertex(to).isEmpty()) {
                sendJson(exchange, 400,
                        "{\"error\":\"Provide valid from and to student names.\"}");
                return;
            }

            int hops = analyzer.degreeOfSeparation(from, to);
            sendJson(exchange, 200,
                    "{\"from\":\"" + escape(from)
                            + "\",\"to\":\"" + escape(to)
                            + "\",\"hops\":" + hops + "}");
        });

        server.createContext("/api/route", exchange -> {
            if (!handleMethod(exchange, "GET")) return;

            Map<String, String> params = queryParams(exchange);
            String from = params.get("from");
            String to = params.get("to");

            if (from == null || to == null
                    || graph.findVertex(from).isEmpty()
                    || graph.findVertex(to).isEmpty()) {
                sendJson(exchange, 400,
                        "{\"error\":\"Provide valid from and to student names.\"}");
                return;
            }

            var result = analyzer.bestAffinityRoute(from, to);
            String path = result.path().stream()
                    .map(name -> "\"" + escape(name) + "\"")
                    .collect(Collectors.joining(","));

            sendJson(exchange, 200,
                    "{\"path\":[" + path + "],\"cost\":"
                            + result.totalCost() + ",\"hasPath\":"
                            + result.hasPath() + "}");
        });

        server.createContext("/api/recommendations", exchange -> {
            if (!handleMethod(exchange, "GET")) return;

            Map<String, String> params = queryParams(exchange);
            String student = params.get("student");

            if (student == null || graph.findVertex(student).isEmpty()) {
                sendJson(exchange, 400,
                        "{\"error\":\"Provide a valid student name.\"}");
                return;
            }

            ConnectionSuggestion suggester = new ConnectionSuggestion(graph);
            String recommendations = suggester.suggestConnections(student).stream()
                    .map(s -> "{\"name\":\"" + escape(s.profileName())
                            + "\",\"mutualConnections\":" + s.mutualConnections() + "}")
                    .collect(Collectors.joining(","));

            sendJson(exchange, 200,
                    "{\"student\":\"" + escape(student)
                            + "\",\"recommendations\":[" + recommendations + "]}");
        });


        server.createContext("/api/traversal", exchange -> {
            if (!handleMethod(exchange, "GET")) return;

            Map<String, String> params = queryParams(exchange);
            String start = params.get("start");
            String algorithm = params.get("algorithm");

            if (start == null || graph.findVertex(start).isEmpty()) {
                sendJson(exchange, 400,
                        "{\"error\":\"Provide a valid starting student.\"}");
                return;
            }

            if (algorithm == null
                    || !(algorithm.equalsIgnoreCase("BFS")
                    || algorithm.equalsIgnoreCase("DFS"))) {
                sendJson(exchange, 400,
                        "{\"error\":\"Algorithm must be BFS or DFS.\"}");
                return;
            }

            var result = algorithm.equalsIgnoreCase("BFS")
                    ? new BFS(graph).execute(start, null)
                    : new DFS(graph).execute(start, null);

            String order = result.path().stream()
                    .map(name -> "\"" + escape(name) + "\"")
                    .collect(Collectors.joining(","));

            sendJson(exchange, 200,
                    "{\"algorithm\":\"" + algorithm.toUpperCase()
                            + "\",\"start\":\"" + escape(start)
                            + "\",\"order\":[" + order + "]"
                            + ",\"visitedCount\":" + result.path().size()
                            + "}");
        });


        server.start();
        System.out.println("CampusConnect API running at http://localhost:8080");
        System.out.println("Press Ctrl+C to stop.");
    }

    private static String edgeJson(Edge edge) {
        return "{\"source\":\"" + escape(edge.getSourceVertex().getProfileName())
                + "\",\"target\":\"" + escape(edge.getTargetVertex().getProfileName())
                + "\",\"weight\":" + edge.getWeight() + "}";
    }

    private static boolean handleMethod(HttpExchange exchange, String method)
            throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(204, -1);
            return false;
        }

        if (!method.equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed.\"}");
            return false;
        }

        return true;
    }

    private static Map<String, String> queryParams(HttpExchange exchange) {
        String query = exchange.getRequestURI().getRawQuery();
        if (query == null || query.isBlank()) return Map.of();

        return java.util.Arrays.stream(query.split("&"))
                .map(part -> part.split("=", 2))
                .collect(Collectors.toMap(
                        parts -> decode(parts[0]),
                        parts -> parts.length > 1 ? decode(parts[1]) : ""
                ));
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private static void sendJson(
            HttpExchange exchange, int status, String json
    ) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, bytes.length);

        try (var output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
