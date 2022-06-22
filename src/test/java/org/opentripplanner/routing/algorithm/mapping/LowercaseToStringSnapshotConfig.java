package org.opentripplanner.routing.algorithm.mapping;

import au.com.origin.snapshots.SnapshotConfig;
import au.com.origin.snapshots.comparators.SnapshotComparator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NumericNode;
import java.lang.reflect.Method;
import java.util.Comparator;

public class LowercaseToStringSnapshotConfig implements SnapshotConfig {

  @Override
  public Class<?> getTestClass() {
    return null;
  }

  @Override
  public Method getTestMethod(Class<?> aClass) {
    return null;
  }

  public class NumericNodeComparator implements Comparator<JsonNode> {

    @Override
    public int compare(JsonNode o1, JsonNode o2) {
      if (o1.equals(o2)) {
        return 0;
      }
      if ((o1 instanceof NumericNode) && (o2 instanceof NumericNode)) {
        Double d1 = o1.asDouble();
        Double d2 = o2.asDouble();
        if (d1.compareTo(d2) == 0) {
          return 0;
        }
      }
      return 1;
    }
  }

  public SnapshotComparator getComparator() {
    return new SnapshotComparator() {
      @Override
      public boolean matches(String snapshotName, String rawSnapshot, String currentObject) {
        ObjectMapper mapper = new ObjectMapper();
        try {
          JsonNode actualObj1 = mapper.readTree(rawSnapshot);
          JsonNode actualObj2 = mapper.readTree(currentObject);

          return actualObj1.equals(new NumericNodeComparator(), actualObj2);
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
}
