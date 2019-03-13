package com.codetaylor.mc.pyrotech.packer.atlas;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Atlas {

  private NodeHandler nodeHandler;
  private NodePool nodePool;
  private List<Node> pageNodeList;
  private ILeaseMap leaseMap;
  private List<Lease> toSort;
  private List<Lease> leasePool;

  private final int width;
  private final int height;
  private final int depth;

  private int nextLeaseId;

  public Atlas(int width, int height, int depth) {

    if (width <= 0) {
      throw new IllegalArgumentException("Atlas width must be greater than zero");
    }

    if (height <= 0) {
      throw new IllegalArgumentException("Atlas height must be greater than zero");
    }

    if (depth <= 0) {
      throw new IllegalArgumentException("Depth must be greater than zero");
    }

    ArrayDeque<Node> nodePool = new ArrayDeque<>(4096);

    for (int i = 0; i < 4096; i++) {
      nodePool.push(new Node());
    }

    this.nodePool = new NodePool(nodePool);

    this.nodeHandler = new NodeHandler(this.nodePool);

    this.width = width;
    this.height = height;
    this.depth = depth;

    this.pageNodeList = new ArrayList<>();
    this.leaseMap = ILeaseMap.withExpectedSize(128);
    this.toSort = new ArrayList<>();

    this.leasePool = new ArrayList<>();

    for (int i = 0; i < 128; i++) {
      this.leasePool.add(new Lease());
    }

    this.createPageNode();
  }

  public int leaseIdCreate() {

    if (this.nextLeaseId == Integer.MAX_VALUE) {
      this.nextLeaseId = 0; // TODO: can this be improved?

      /*
      Maybe this could be improved by keeping an int[] stack of values that have been reclaimed
      and an index to the last pushed value.
       */
    }

    return this.nextLeaseId++;
  }

  /**
   * Adds all the leases to the given list in no particular order.
   *
   * @param list the list to add to
   * @return the given list
   */
  public List<Lease> leaseListGet(List<Lease> list) {

    list.addAll(this.leaseMap.values());
    return list;
  }

  public boolean leaseDispose(int id) {

    Lease lease = this.leaseMap.remove(id);

    if (lease != null) {
      this.nodeHandler.dispose(lease.getNode());
      lease.initDefault();
      this.leasePool.add(lease);
      return true;
    }

    return false;
  }

  public Lease leaseGet(int id) {

    return this.leaseMap.get(id);
  }

  public void repack(Comparator<Lease> comparator) {

    this.toSort.addAll(this.leaseMap.values());
    this.toSort.sort(comparator);

    for (Node node : this.pageNodeList) {
      this.nodePool.releaseNode(node);
    }

    this.pageNodeList.clear();

    /*
    Here we are re-using the existing leases because the only thing that will change
    when we do a repack is the leases' x and y positions.
     */
    for (Lease lease : this.toSort) {
      this._insert(lease.getId(), lease.getWidth(), lease.getHeight(), lease);
    }

    this.toSort.clear();
  }

  public Lease insert(
      int id,
      int width,
      int height
  ) {

    if (this.leaseMap.containsKey(id)) {
      throw new IllegalArgumentException(String.format("Atlas image id [%s] already in use", id));
    }

    Lease lease;

    if (this.leasePool.isEmpty()) {
      lease = new Lease();

    } else {
      lease = this.leasePool.remove(this.leasePool.size() - 1);
    }

    lease = this._insert(id, width, height, lease);

    this.leaseMap.put(id, lease);

    return lease;
  }

  private Lease _insert(
      int id,
      int width,
      int height,
      Lease lease
  ) throws AtlasOutOfRoomException {

    if (width > this.width) {
      throw new IllegalStateException(String.format(
          "Image width [%d] is too large for the atlas width [%d]",
          width,
          this.width
      ));

    } else if (height > this.height) {
      throw new IllegalStateException(String.format(
          "Image height [%d] is too large for the atlas height [%d]",
          height,
          this.height
      ));
    }

    // try to fit it into an existing page node
    Node resultNode = null;
    int pageId = 0;

    for (int i = 0; i < this.pageNodeList.size(); i++) {
      Node pageNode = this.pageNodeList.get(i);
      resultNode = this.nodeHandler.insert(pageNode, width, height);

      if (resultNode != null) {
        pageId = i;
        break;
      }
    }

    if (resultNode == null) {
      // couldn't fit on any page
      // try to make a new page and fit it there

      // will making a new page node exceed the page depth?
      if (this.pageNodeList.size() == this.depth) {
        throw new AtlasOutOfRoomException(String.format(
            "Page depth of [%d] exceeded",
            this.depth
        ));
      }

      pageId = this.pageNodeList.size();

      Node pageNode = this.createPageNode();

      resultNode = this.nodeHandler.insert(pageNode, width, height);
    }

    assert resultNode != null;

    if (resultNode.imageId == -1) {

      resultNode.imageId = id;

      lease.init(
          id,
          resultNode.posX,
          resultNode.posY,
          resultNode.width,
          resultNode.height,
          resultNode.posX / (float) this.width,
          resultNode.posY / (float) this.height,
          (resultNode.posX / (float) this.width) + (resultNode.width / (float) this.width),
          (resultNode.posY / (float) this.height) + (resultNode.height / (float) this.height),
          pageId,
          resultNode
      );

      return lease;

    }

    throw new IllegalStateException("Something is awry");
  }

  private Node createPageNode() {

    Node pageNode = this.nodePool.getNode(null, 0, 0, this.width, this.height);
    this.pageNodeList.add(pageNode);
    return pageNode;
  }

}
