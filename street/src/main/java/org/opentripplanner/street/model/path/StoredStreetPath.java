package org.opentripplanner.street.model.path;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.geometry.GeometryUtils;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.request.StreetSearchRequest;
import org.opentripplanner.street.search.state.EdgeTraverser;

/// A path through the street network which is kept, like a transfer between two stops stored in
/// the graph. It consists only of the edges in chronological order, not the states of the search
/// which found it, so it is serializable and holds no references to a search.
///
/// The times and costs of a stored path depend on the preferences of a specific search. Use
/// [#reTraverse(StreetSearchRequest)] to get a complete [StreetPath] for a request.
public final class StoredStreetPath implements Serializable {

  /// The path of something which was not found by a street search, like a stop found by a
  /// straight-line search. It has no edges and no vertices.
  public static final StoredStreetPath EMPTY = new StoredStreetPath(null, null, List.of());

  @Nullable
  private final Vertex fromVertex;

  @Nullable
  private final Vertex toVertex;

  private final List<Edge> edges;

  private StoredStreetPath(
    @Nullable Vertex fromVertex,
    @Nullable Vertex toVertex,
    List<Edge> edges
  ) {
    this.fromVertex = fromVertex;
    this.toVertex = toVertex;
    this.edges = List.copyOf(edges);
  }

  static StoredStreetPath of(Vertex fromVertex, Vertex toVertex, List<Edge> edges) {
    return new StoredStreetPath(
      Objects.requireNonNull(fromVertex),
      Objects.requireNonNull(toVertex),
      edges
    );
  }

  /// True if the path has no edges, like a straight-line path or a path which starts and ends at
  /// the same vertex.
  public boolean isEmpty() {
    return edges.isEmpty();
  }

  /// The distance of the path in meters, the sum of the distances of its edges.
  public double distanceMeters() {
    return edges.stream().mapToDouble(Edge::getDistanceMeters).sum();
  }

  /// The geometry of the path.
  public LineString geometry() {
    var geometries = edges
      .stream()
      .filter(Edge::includeGeometryInPath)
      .map(Edge::getGeometry)
      .filter(Objects::nonNull);
    return GeometryUtils.concatenateLineStrings(geometries::iterator);
  }

  /// Traverse the edges of this path, starting at its first vertex, using the given request. The
  /// traversal runs forward in time, so the returned path is chronological without reversing any
  /// states.
  ///
  /// @return Empty if the edges can't be traversed using the given request, or
  ///         [StreetPath#EMPTY] if this path is [#EMPTY]
  public Optional<StreetPath> reTraverse(StreetSearchRequest request) {
    if (fromVertex == null) {
      return Optional.of(StreetPath.EMPTY);
    }
    return EdgeTraverser.createPath(fromVertex, edges, request);
  }

  /// The edges of the path in chronological order.
  List<Edge> edges() {
    return edges;
  }

  /// Two stored paths are equal if they traverse the same edges.
  @Override
  public boolean equals(Object o) {
    return (
      o instanceof StoredStreetPath other &&
      Objects.equals(fromVertex, other.fromVertex) &&
      edges.equals(other.edges)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromVertex, edges);
  }

  @Override
  public String toString() {
    return "StoredStreetPath{" + fromVertex + " -> " + toVertex + ", " + edges.size() + " edges}";
  }
}
