package com.codetaylor.mc.pyrotech.packer.atlas;

/* package */ class NodeHandler {

  private NodePool nodePool;

  /* package */ NodeHandler(NodePool nodePool) {

    this.nodePool = nodePool;
  }

  /**
   * Returns null if the dimensions were unable to be inserted into this node
   * or any child of this node.
   * <p>
   * Returns the insertion node if the insertion is successful.
   *
   * @param width  the inserted width
   * @param height the inserted height
   * @return insertion node or null if insertion fails
   */
  /* package */ Node insert(Node node, int width, int height) {

    if (!this.isLeaf(node)) {

      // try inserting into first child
      Node newNode = this.insert(node.childA, width, height);

      if (newNode != null) {
        return newNode;
      }

      // no room, insert into second
      return this.insert(node.childB, width, height);

    } else {

      // if there's already a rect here, return
      if (node.imageId != -1) {
        return null;
      }

      // if too small, return
      if (width > node.width || height > node.height) {
        return null;
      }

      // if rect fits perfectly, accept
      if (width == node.width && height == node.height) {
        return node;
      }

      // otherwise, split node
      // decide which way to split
      int dw = node.width - width;
      int dh = node.height - height;

      int splitLeft, splitTop, splitWidth, splitHeight;

      if (dw > dh) {
        // split horizontally
        splitLeft = node.posX;
        splitTop = node.posY;
        splitWidth = width;
        splitHeight = node.height;
        node.childA = this.nodePool.getNode(node, splitLeft, splitTop, splitWidth, splitHeight);

        splitLeft = node.posX + splitWidth;
        splitTop = node.posY;
        splitWidth = node.width - splitWidth;
        splitHeight = node.height;
        node.childB = this.nodePool.getNode(node, splitLeft, splitTop, splitWidth, splitHeight);

      } else {
        // split vertically
        splitLeft = node.posX;
        splitTop = node.posY;
        splitWidth = node.width;
        splitHeight = height;
        node.childA = this.nodePool.getNode(node, splitLeft, splitTop, splitWidth, splitHeight);

        splitLeft = node.posX;
        splitTop = node.posY + splitHeight;
        splitWidth = node.width;
        splitHeight = node.height - splitHeight;
        node.childB = this.nodePool.getNode(node, splitLeft, splitTop, splitWidth, splitHeight);
      }

      return this.insert(node.childA, width, height);
    }

  }

  /* package */ void dispose(Node node) {

    node.imageId = -1;

    // we are now a leaf node with no image - we notify the parent
    if (node.parent != null) {
      this.onChildDisposed(node.parent, node);
    }
  }

  private void onChildDisposed(Node parent, Node child) {

    // we assume the child disposed of was a leaf child with
    // an image id, so we want to look at the other child
    Node otherChild = (parent.childA == child) ? parent.childB : parent.childA;

    if (this.isLeaf(otherChild)
        && otherChild.imageId == -1) {

      // the other child is empty ergo the parent's two child nodes can be removed

      this.nodePool.releaseNode(parent.childA);
      this.nodePool.releaseNode(parent.childB);

      parent.childA = null;
      parent.childB = null;

      // we are now a leaf node with no image which is the same
      // as being disposed - we notify the parent's parent
      if (parent.parent != null) {
        this.onChildDisposed(parent.parent, parent);
      }
    }

  }

  private boolean isLeaf(Node node) {

    return node.childA == null && node.childB == null;
  }

}
