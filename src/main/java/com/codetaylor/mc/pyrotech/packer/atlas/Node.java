package com.codetaylor.mc.pyrotech.packer.atlas;

/* package */ class Node {

  /* package */ Node parent;
  /* package */ Node childA;
  /* package */ Node childB;
  /* package */ int imageId;
  /* package */ int posX;
  /* package */ int posY;
  /* package */ int width;
  /* package */ int height;

  /* package */ Node() {

    this.initDefaults();
  }

  /* package */ void initDefaults() {

    this.init(null, 0, 0, 0, 0);
  }

  /* package */ Node init(
      Node parent,
      int posX,
      int posY,
      int width,
      int height
  ) {

    this.parent = parent;
    this.posX = posX;
    this.posY = posY;
    this.width = width;
    this.height = height;
    this.childA = null;
    this.childB = null;
    this.imageId = -1;

    return this;
  }

}