package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class InteractionRayTraceData
    implements Comparable<InteractionRayTraceData> {

  private final double distanceSq;
  private final RayTraceResult rayTraceResult;
  private final IInteraction interaction;

  InteractionRayTraceData(double distanceSq, RayTraceResult rayTraceResult, IInteraction interaction) {

    this.distanceSq = distanceSq;
    this.rayTraceResult = rayTraceResult;
    this.interaction = interaction;
  }

  @Override
  public int compareTo(@Nonnull InteractionRayTraceData o) {

    return Double.compare(this.distanceSq, o.distanceSq);
  }

  public double getDistanceSq() {

    return this.distanceSq;
  }

  public RayTraceResult getRayTraceResult() {

    return this.rayTraceResult;
  }

  public IInteraction getInteraction() {

    return this.interaction;
  }

  public static class List
      extends ArrayList<InteractionRayTraceData> {
    //
  }
}
