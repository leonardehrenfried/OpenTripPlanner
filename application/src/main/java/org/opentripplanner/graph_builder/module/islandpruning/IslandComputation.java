package org.opentripplanner.graph_builder.module.islandpruning;

import java.util.List;
import java.util.Set;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.search.TraverseMode;

/**
 * The outcome of {@link IslandFinder#computeIslands}: the islands found for a single traverse
 * mode and the edges found to be unreachable, ready to be handed to {@link IslandPruningModule}'s
 * pruning step.
 */
record IslandComputation(TraverseMode traverseMode, List<Subgraph> islands, Set<Edge> isolated) {}
