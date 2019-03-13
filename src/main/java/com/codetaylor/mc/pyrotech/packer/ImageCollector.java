package com.codetaylor.mc.pyrotech.packer;

import com.google.gson.Gson;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ImageCollector {

  private final Gson gson;

  public ImageCollector(Gson gson) {

    this.gson = gson;
  }

  public Map<String, Map<String, ImageData>> collect(PackData.PathData[] input) throws IOException {

    Map<String, Map<String, ImageData>> map = new HashMap<>();

    for (PackData.PathData pathData : input) {

      Path path = pathData.path;
      String atlas = pathData.atlas;

      Map<String, ImageData> imageMap = map.computeIfAbsent(atlas, k -> new TreeMap<>());
      Files.walkFileTree(path, new ImageVisitor(this.gson, path, imageMap));
    }

    return map;
  }

  public static class ImageData {

    public BufferedImage image;
    public ImageMetaData metaData;
  }

}
