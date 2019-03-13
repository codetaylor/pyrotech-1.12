package com.codetaylor.mc.pyrotech.packer;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ImageMetaData {

  public String id;

  @SerializedName("sub_images")
  public Map<String, ImageData> subImages;

  public static class ImageData {

    public int x, y;
    public int width, height;
  }
}
