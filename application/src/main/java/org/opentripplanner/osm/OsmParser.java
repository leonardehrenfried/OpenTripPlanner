package org.opentripplanner.osm;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat;
import gnu.trove.list.array.TLongArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.opentripplanner.graph_builder.module.osm.OsmDatabase;
import org.opentripplanner.osm.model.OsmMemberType;
import org.opentripplanner.osm.model.OsmNode;
import org.opentripplanner.osm.model.OsmRelation;
import org.opentripplanner.osm.model.OsmRelationMember;
import org.opentripplanner.osm.model.OsmWay;

/**
 * Parser for the OpenStreetMap PBF Format.
 *
 * It intentionally skips the builder classes to keep allocations low.
 *
 */
class OsmParser extends BinaryParser {

  private final OsmDatabase osmdb;
  private final Map<String, String> stringTable = new HashMap<>();
  private final DefaultOsmProvider provider;
  private OsmParserPhase parsePhase;

  public OsmParser(OsmDatabase osmdb, DefaultOsmProvider provider) {
    this.osmdb = Objects.requireNonNull(osmdb);
    this.provider = Objects.requireNonNull(provider);
  }

  // The strings are already being pulled from a string table in the PBF file,
  // but there appears to be a separate string table per 8k-entry PBF file block.
  // String.intern grinds to a halt on large PBF files (as it did on GTFS import), so
  // we implement our own.
  public String internalize(String s) {
    String fromTable = stringTable.get(s);
    if (fromTable == null) {
      stringTable.put(s, s);
      return s;
    }
    return fromTable;
  }

  @Override
  public void complete() {
    // Jump in circles
  }

  /**
   * Set the phase to be parsed
   */
  public void setPhase(OsmParserPhase phase) {
    this.parsePhase = phase;
  }

  @Override
  protected void parseRelations(List<Osmformat.Relation> rels) {
    if (parsePhase != OsmParserPhase.Relations) {
      return;
    }

    for (Osmformat.Relation i : rels) {
      var tags = new HashMap<String, String>(i.getKeysCount());
      for (int j = 0; j < i.getKeysCount(); j++) {
        tags.put(
          internalize(getStringById(i.getKeys(j))),
          internalize(getStringById(i.getVals(j)))
        );
      }

      var members = new ArrayList<OsmRelationMember>(i.getMemidsCount());
      long lastMid = 0;
      for (int j = 0; j < i.getMemidsCount(); j++) {
        OsmRelationMember relMember = new OsmRelationMember();
        long mid = lastMid + i.getMemids(j);

        relMember.setRef(mid);
        lastMid = mid;

        relMember.setRole(internalize(getStringById(i.getRolesSid(j))));

        if (i.getTypes(j) == Osmformat.Relation.MemberType.NODE) {
          relMember.setType(OsmMemberType.NODE);
        } else if (i.getTypes(j) == Osmformat.Relation.MemberType.WAY) {
          relMember.setType(OsmMemberType.WAY);
        } else if (i.getTypes(j) == Osmformat.Relation.MemberType.RELATION) {
          relMember.setType(OsmMemberType.RELATION);
        } else {
          // TODO; Illegal file?
          assert false;
        }

        members.add(relMember);
      }

      var relation = new OsmRelation(i.getId(), tags, provider, members);
      osmdb.addRelation(relation);
    }
  }

  @Override
  protected void parseDense(Osmformat.DenseNodes nodes) {
    long lastId = 0;
    long lastLat = 0;
    long lastLon = 0;
    // Index into the keysvals array.
    int j = 0;

    if (parsePhase != OsmParserPhase.Nodes) {
      return;
    }

    // because it's a hot loop we don't use the builder
    for (int i = 0; i < nodes.getIdCount(); i++) {
      long lat = nodes.getLat(i) + lastLat;
      lastLat = lat;
      long lon = nodes.getLon(i) + lastLon;
      lastLon = lon;
      long id = nodes.getId(i) + lastId;
      lastId = id;
      double latf = parseLat(lat);
      double lonf = parseLon(lon);

      var tags = new HashMap<String, String>(nodes.getKeysValsCount());
      // If empty, assume that nothing here has keys or vals.
      if (nodes.getKeysValsCount() > 0) {
        while (nodes.getKeysVals(j) != 0) {
          int keyid = nodes.getKeysVals(j++);
          int valid = nodes.getKeysVals(j++);

          String key = internalize(getStringById(keyid));
          String value = internalize(getStringById(valid));
          tags.put(key, value);
        }
        // Skip over the '0' delimiter.
        j++;
      }

      var node = new OsmNode(id, latf, lonf, tags, provider);
      osmdb.addNode(node);
    }
  }

  @Override
  protected void parseNodes(List<Osmformat.Node> nodes) {
    if (parsePhase != OsmParserPhase.Nodes) {
      return;
    }

    for (Osmformat.Node i : nodes) {
      var tags = new HashMap<String, String>(i.getKeysCount());
      for (int j = 0; j < i.getKeysCount(); j++) {
        tags.put(
          internalize(getStringById(i.getKeys(j))),
          internalize(getStringById(i.getVals(j)))
        );
      }
      var node = new OsmNode(i.getId(), parseLat(i.getLat()), parseLon(i.getLon()), tags, provider);
      osmdb.addNode(node);
    }
  }

  @Override
  protected void parseWays(List<Osmformat.Way> ways) {
    if (parsePhase != OsmParserPhase.Ways) {
      return;
    }

    for (Osmformat.Way i : ways) {
      var tags = new HashMap<String, String>(i.getKeysCount());
      for (int j = 0; j < i.getKeysCount(); j++) {
        tags.put(
          internalize(getStringById(i.getKeys(j))),
          internalize(getStringById(i.getVals(j)))
        );
      }

      var nodeRefs = new TLongArrayList(i.getRefsCount());
      long lastId = 0;
      for (long j : i.getRefsList()) {
        lastId += j;
        nodeRefs.add(lastId);
      }

      var way = new OsmWay(i.getId(), tags, provider, nodeRefs);
      osmdb.addWay(way);
    }
  }

  @Override
  public void parse(Osmformat.HeaderBlock block) {
    for (String s : block.getRequiredFeaturesList()) {
      if (s.equals("OsmSchema-V0.6")) {
        // We can parse this.
        continue;
      }
      if (s.equals("DenseNodes")) {
        // We can parse this.
        continue;
      }
      throw new IllegalStateException("File requires unknown feature: " + s);
    }
  }
}
