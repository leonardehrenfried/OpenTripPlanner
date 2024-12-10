package org.opentripplanner.routing.graph.kryosupport;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.opentripplanner.routing.api.request.StreetMode;

@SuppressWarnings("rawtypes")
class JavaImmutableEnumSetSerializer extends Serializer<Set> {

  @Override
  public void write(Kryo kryo, Output output, Set set) {
    kryo.writeObject(output, new ImmSerSet(set.stream().toList()));
  }

  @Override
  public Set read(Kryo kryo, Input input, Class<? extends Set> type) {
    return kryo.readObject(input, ImmSerSet.class).toUnmodifableEnumSet();
  }

  private static class ImmSerSet implements Serializable {

    private final Object[] array;

    private ImmSerSet(List array) {
      this.array = array.toArray();
    }

    private Set toUnmodifableEnumSet() {
      if(array.length == 0){
        return Collections.unmodifiableSet(EnumSet.noneOf(StreetMode.class));
      }
      var set = EnumSet.noneOf((Class)array[0].getClass());
      for(var i: array){
        var e =(Enum)i;
        set.add(e);
      }

      return Collections.unmodifiableSet(set);
    }
  }
}
