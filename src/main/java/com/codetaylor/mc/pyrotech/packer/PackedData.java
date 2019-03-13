package com.codetaylor.mc.pyrotech.packer;

import java.util.Map;
import java.util.TreeMap;

public class PackedData {

  public Map<String, AtlasData> atlas;
  public Map<String, ImageData> image;

  public PackedData() {

    this.atlas = new TreeMap<>();
    this.image = new TreeMap<>();
  }

  public static class ImageData {

    public String atlas;
    public int u, v;
    public int width, height;
  }

  public static class AtlasData {

    public int width, height;
  }

}
