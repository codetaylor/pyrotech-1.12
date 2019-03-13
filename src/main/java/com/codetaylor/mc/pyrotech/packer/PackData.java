package com.codetaylor.mc.pyrotech.packer;

import com.google.gson.annotations.SerializedName;

import java.nio.file.Path;
import java.util.Map;

public class PackData {

  public PathsData paths;
  public Map<String, AtlasData> atlas_definitions;

  public static class AtlasData {

    public int width = 256;
    public int height = 256;
    public String path;
  }

  public static class PathsData {

    @SerializedName("output")
    public String outputString;

    @SerializedName("resource_path")
    public String resourcePath;

    public PathData[] input;
  }

  public static class PathData {

    public transient Path path;

    @SerializedName("path")
    public String pathString;

    public String atlas;
  }

}
