package org.opentripplanner.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.protobuf.ByteString;
import crosby.binary.Osmformat;
import crosby.binary.file.CompressFlags;
import crosby.binary.file.FileBlock;
import crosby.binary.file.FileBlockPosition;
import java.io.ByteArrayOutputStream;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.opentripplanner.graph_builder.issue.api.DataImportIssueStore;
import org.opentripplanner.graph_builder.module.osm.OsmDatabase;

/**
 * OTP's {@link OsmParser} reads a PBF file three times, once per {@link OsmParserPhase}, and
 * skips decompressing blocks it has learned (during the first read) cannot contain anything
 * relevant to the current phase. The PBF spec guarantees that a {@link Osmformat.PrimitiveGroup}
 * only contains a single primitive type, but not that a whole block does - so this test builds a
 * single block that mixes nodes, ways and relations and checks that it is never skipped for any
 * of the phases it's actually relevant to.
 */
class OsmParserBlockSkippingTest {

  @Test
  void mixedBlockIsNeverSkipped() {
    OsmDatabase osmdb = new OsmDatabase(DataImportIssueStore.NOOP);
    DefaultOsmProvider provider = new DefaultOsmProvider(new File("unused.osm.pbf"), true);
    OsmParser parser = new OsmParser(osmdb, provider);

    Osmformat.PrimitiveBlock block = mixedTypeBlock();
    FileBlockPosition dataBlockPosition = dataBlockPosition();

    for (OsmParserPhase phase : OsmParserPhase.values()) {
      parser.setPhase(phase);
      boolean skipped = parser.skipBlock(dataBlockPosition);
      assertFalse(skipped, "a block containing " + phase + " must never be skipped for it");
      if (!skipped) {
        parser.parse(block);
      }
      // Mirrors DefaultOsmProvider#readOsm(), which runs these between phases so that
      // way-derived node references are known before addNode() decides what to keep.
      switch (phase) {
        case Relations -> osmdb.doneFirstPhaseRelations();
        case Ways -> osmdb.doneSecondPhaseWays();
        case Nodes -> osmdb.doneThirdPhaseNodes();
      }
    }

    assertNotNull(osmdb.getNode(1L), "the node from the mixed block must have been parsed");
    assertEquals(1, osmdb.wayCount());
  }

  /** A single PrimitiveBlock with one group each of dense nodes, ways and relations. */
  private static Osmformat.PrimitiveBlock mixedTypeBlock() {
    // Two nodes (ids delta-encoded: 1, then +3 = 4) so the way has the >1 node refs that
    // markNodesForKeeping() requires before it will keep either of them around.
    Osmformat.DenseNodes denseNodes = Osmformat.DenseNodes.newBuilder()
      .addId(1)
      .addLat(0)
      .addLon(0)
      .addId(3)
      .addLat(0)
      .addLon(0)
      .build();

    // Tagged as a routable way so that doneSecondPhaseWays() keeps its nodes around for the
    // node phase - an untagged way's nodes would otherwise be purged as irrelevant.
    Osmformat.Way way = Osmformat.Way.newBuilder()
      .setId(2)
      .addRefs(1)
      .addRefs(3)
      .addKeys(1)
      .addVals(2)
      .build();

    Osmformat.Relation relation = Osmformat.Relation.newBuilder().setId(3).build();

    // Index 0 is reserved by convention.
    Osmformat.StringTable stringtable = Osmformat.StringTable.newBuilder()
      .addS(ByteString.copyFromUtf8(""))
      .addS(ByteString.copyFromUtf8("highway"))
      .addS(ByteString.copyFromUtf8("residential"))
      .build();

    return Osmformat.PrimitiveBlock.newBuilder()
      .setStringtable(stringtable)
      .addPrimitivegroup(Osmformat.PrimitiveGroup.newBuilder().setDense(denseNodes).build())
      .addPrimitivegroup(Osmformat.PrimitiveGroup.newBuilder().addWays(way).build())
      .addPrimitivegroup(Osmformat.PrimitiveGroup.newBuilder().addRelations(relation).build())
      .build();
  }

  /** An "OSMData" {@link FileBlockPosition}, obtained the only way the library exposes one. */
  private static FileBlockPosition dataBlockPosition() {
    FileBlock fileBlock = FileBlock.newInstance("OSMData", ByteString.EMPTY, null);
    try {
      return fileBlock.writeTo(new ByteArrayOutputStream(), CompressFlags.NONE);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
