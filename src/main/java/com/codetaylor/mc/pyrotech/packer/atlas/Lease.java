package com.codetaylor.mc.pyrotech.packer.atlas;

/**
 * The lease represents an area on the texture atlas that is spoken for. Leases
 * are re-used so be careful when keeping references to leases.
 */
public class Lease {

  private int id;
  private int posX;
  private int posY;
  private int width;
  private int height;
  private float u0;
  private float v0;
  private float u1;
  private float v1;
  private int page;
  private Node node;

  /* package */ Lease() {

    this.initDefault();
  }

  /* package */ void initDefault() {

    this.init(
        -1,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        null
    );
  }

  /* package */ void init(
      int id,
      int posX,
      int posY,
      int width,
      int height,
      float u0,
      float v0,
      float u1,
      float v1,
      int page,
      Node node
  ) {

    this.id = id;
    this.posX = posX;
    this.posY = posY;
    this.width = width;
    this.height = height;
    this.u0 = u0;
    this.v0 = v0;
    this.u1 = u1;
    this.v1 = v1;
    this.page = page;
    this.node = node;
  }

  public int getId() {

    return this.id;
  }

  public int getPosX() {

    return this.posX;
  }

  public int getPosY() {

    return this.posY;
  }

  public int getWidth() {

    return this.width;
  }

  public int getHeight() {

    return this.height;
  }

  public int getPage() {

    return this.page;
  }

  /* package */ Node getNode() {

    return this.node;
  }

  public float getU0() {

    return this.u0;
  }

  public float getV0() {

    return this.v0;
  }

  public float getU1() {

    return this.u1;
  }

  public float getV1() {

    return this.v1;
  }

}
