package com.codetaylor.mc.pyrotech.packer.atlas;

import java.util.Deque;

/* package */ class NodePool {

  private Deque<Node> availableNodePool;

  /* package */ NodePool(Deque<Node> availableNodePool) {

    this.availableNodePool = availableNodePool;
  }

  /* package */ Node getNode(
      Node parent,
      int posX,
      int posY,
      int width,
      int height
  ) {

    if (this.availableNodePool.isEmpty()) {
      this.availableNodePool.add(new Node());
    }

    return this.availableNodePool.pop().init(parent, posX, posY, width, height);
  }

  /* package */ void releaseNode(Node node) {

    if (node == null) {
      return;
    }

    if (node.childA != null) {
      this.releaseNode(node.childA);
    }

    if (node.childB != null) {
      this.releaseNode(node.childB);
    }

    node.initDefaults();
    this.availableNodePool.push(node);
  }

}
