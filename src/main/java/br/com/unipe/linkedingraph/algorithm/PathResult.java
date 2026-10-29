
package br.com.unipe.linkedingraph.algorithm;

import java.util.Collections;
import java.util.List;

/**
 * Stores the result of a graph traversal or pathfinding operation.
 *
 * @param path      Ordered list of vertex names in the result
 * @param totalCost Total path cost, or -1 if no path exists
 */
public record PathResult(
        List<String> path,
        int totalCost
) {

    public boolean hasPath() {
        return path != null && !path.isEmpty();
    }

    public static PathResult empty() {
        return new PathResult(Collections.emptyList(), -1);
    }
}
