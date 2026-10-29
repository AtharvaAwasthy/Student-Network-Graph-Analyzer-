
package br.com.unipe.linkedingraph.algorithm;

/**
 * Defines the common operations supported by graph algorithms.
 */
public interface GraphAlgorithm {

    String getName();

    PathResult execute(String sourceProfile, String targetProfile);
}
