package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

public class InteractionBounds {

  public static final InteractionBounds INFINITE = new InteractionBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

  private final double minX;
  private final double maxX;
  private final double minY;
  private final double maxY;

  public InteractionBounds(double minX, double maxX, double minY, double maxY) {

    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
  }

  public boolean contains(double x, double y) {

    if (x > this.minX && x < this.maxX) {

      return (y > this.minY && y < this.maxY);

    } else {
      return false;
    }
  }
}
