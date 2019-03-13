package com.codetaylor.mc.pyrotech.packer.atlas;

import java.util.HashMap;
import java.util.Map;

public interface ILeaseMap
    extends Map<Integer, Lease> {

  static ILeaseMap withExpectedSize(int expectedSize) {

    return new LeaseMap(expectedSize);
  }

  class LeaseMap
      extends HashMap<Integer, Lease>
      implements ILeaseMap {

    /* package */ LeaseMap(int initialCapacity) {

      super(initialCapacity);
    }
  }
}
