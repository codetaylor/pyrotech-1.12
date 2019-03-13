package com.codetaylor.mc.pyrotech.packer.atlas;

import java.util.Comparator;

public final class LeaseComparator {

  public static final SmallFirst SMALL_FIRST = new SmallFirst();

  public static final class SmallFirst
      implements Comparator<Lease> {

    @Override
    public int compare(
        Lease o1,
        Lease o2
    ) {

      return Integer.compare(o1.getWidth() * o1.getHeight(), o2.getWidth() * o2.getHeight());
    }

  }

  public static final LargeFirst LARGE_FIRST = new LargeFirst();

  public static final class LargeFirst
      implements Comparator<Lease> {

    @Override
    public int compare(
        Lease o1,
        Lease o2
    ) {

      return Integer.compare(o2.getWidth() * o2.getHeight(), o1.getWidth() * o1.getHeight());
    }

  }

  private LeaseComparator() {
    //
  }

}
