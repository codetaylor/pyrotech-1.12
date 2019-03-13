package com.codetaylor.mc.pyrotech.packer;

import com.codetaylor.mc.pyrotech.packer.atlas.Atlas;
import com.codetaylor.mc.pyrotech.packer.atlas.Lease;
import com.codetaylor.mc.pyrotech.packer.atlas.LeaseComparator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Packer {

  public static void main(String[] args) throws IOException {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String pathname = "assets/book/atlas/pack.json";

    Packer packer = new Packer(
        gson,
        new GsonPackDataSupplier(gson, new FileReader(new File(pathname))),
        new PackDataPathResolver(),
        new ImageCollector(gson)
    );

    packer.run();
  }

  private final Gson gson;
  private final Supplier<PackData> packDataSupplier;
  private final PackDataPathResolver packDataPathResolver;
  private final ImageCollector imageCollector;

  public Packer(
      Gson gson,
      Supplier<PackData> packDataSupplier,
      PackDataPathResolver packDataPathResolver,
      ImageCollector imageCollector
  ) {

    this.gson = gson;
    this.packDataSupplier = packDataSupplier;
    this.packDataPathResolver = packDataPathResolver;
    this.imageCollector = imageCollector;
  }

  private void run() throws IOException {

    // Load pack.json
    System.out.println("Loading pack.json data...");
    PackData packData = this.packDataSupplier.get();
    this.packDataPathResolver.resolve(packData.paths.input);

    // Collect images
    System.out.println("Collecting images...");
    Map<String, Map<String, ImageCollector.ImageData>> atlasImageMap = this.imageCollector.collect(packData.paths.input);

    // Create logical atlases
    System.out.println("Creating logical atlases...");
    Map<String, Atlas> atlasMap = new HashMap<>();

    for (String atlasId : atlasImageMap.keySet()) {
      PackData.AtlasData atlasData = packData.atlas_definitions.get(atlasId);

      if (atlasData == null) {
        throw new RuntimeException("Missing atlas definition for: " + atlasId);
      }
      Atlas atlas = new Atlas(atlasData.width, atlasData.height, Integer.MAX_VALUE);
      atlasMap.put(atlasId, atlas);
      System.out.println("  -> " + atlasId);
    }

    // Create atlas leases and pack images
    System.out.println("Creating atlas leases and packing images...");
    Map<String, Map<String, Integer>> atlasLeaseMap = new HashMap<>();

    for (Map.Entry<String, Map<String, ImageCollector.ImageData>> entry : atlasImageMap.entrySet()) {
      int leaseId = 0;
      String atlasId = entry.getKey();
      Atlas atlas = atlasMap.get(atlasId);
      Map<String, ImageCollector.ImageData> images = entry.getValue();
      Map<String, Integer> leases = new HashMap<>();

      for (Map.Entry<String, ImageCollector.ImageData> imageEntry : images.entrySet()) {
        ImageCollector.ImageData imageData = imageEntry.getValue();
        BufferedImage image = imageData.image;
        int width = image.getWidth();
        int height = image.getHeight();
        atlas.insert(leaseId, width, height);
        String imageId = imageEntry.getKey();
        leases.put(imageId, leaseId);
        System.out.println("  -> " + atlasId + ":" + imageId + ", id:" + leaseId);
        leaseId += 1;
      }

      atlas.repack(LeaseComparator.LARGE_FIRST);
      atlasLeaseMap.put(atlasId, leases);
    }

    // Create atlas images
    System.out.println("Creating atlas images...");
    Map<String, List<BufferedImage>> atlasImageListMap = new HashMap<>();
    this.createAtlasImages(packData, atlasImageMap, atlasMap, atlasLeaseMap, atlasImageListMap);

    // Write atlas images
    System.out.println("Writing atlas images...");
    for (Map.Entry<String, List<BufferedImage>> entry : atlasImageListMap.entrySet()) {

      Path path;

      String atlasId = entry.getKey();
      PackData.AtlasData atlasData = packData.atlas_definitions.get(atlasId);

      if (atlasData.path != null) {
        path = Paths.get(packData.paths.outputString, atlasData.path);

      } else {
        path = Paths.get(packData.paths.outputString);
      }

      Files.createDirectories(path);

      List<BufferedImage> imageList = entry.getValue();

      for (int i = 0; i < imageList.size(); i++) {
        Path atlasFilePath = this.resolveAtlasImagePath(atlasId, i, packData.paths.outputString, atlasData.path);
        ImageIO.write(imageList.get(i), "PNG", atlasFilePath.toFile());
        System.out.format("  -> %s\n", atlasFilePath.toString().replaceAll("\\\\", "/"));
      }
    }

    // Collect packed data
    System.out.println("Collecting packed data...");
    PackedData packedData = this.collectPackedData(packData, atlasImageMap, atlasMap, atlasLeaseMap);

    // Write packed json data
    System.out.println("Writing packed.json data...");
    Path path = Paths.get(packData.paths.outputString, "packed.json");
    File file = path.toFile();
    FileWriter writer = new FileWriter(file);
    this.gson.toJson(packedData, writer);
    writer.close();
    System.out.format("  -> %s\n", path.toString().replaceAll("\\\\", "/"));
  }

  private void createAtlasImages(PackData packData, Map<String, Map<String, ImageCollector.ImageData>> atlasImageMap, Map<String, Atlas> atlasMap, Map<String, Map<String, Integer>> atlasLeaseMap, Map<String, List<BufferedImage>> atlasImageListMap) {

    for (Map.Entry<String, Atlas> entry : atlasMap.entrySet()) {

      String atlasId = entry.getKey();
      Atlas atlas = entry.getValue();
      Map<String, ImageCollector.ImageData> imageMap = atlasImageMap.get(atlasId);
      Map<String, Integer> leaseMap = atlasLeaseMap.get(atlasId);
      List<BufferedImage> atlasImageList = new ArrayList<>();
      atlasImageListMap.put(atlasId, atlasImageList);
      PackData.AtlasData atlasData = packData.atlas_definitions.get(atlasId);

      for (Map.Entry<String, ImageCollector.ImageData> imageEntry : imageMap.entrySet()) {
        String imageId = imageEntry.getKey();
        ImageCollector.ImageData collectorImageData = imageEntry.getValue();
        BufferedImage image = collectorImageData.image;
        int leaseId = leaseMap.get(imageId);
        Lease lease = atlas.leaseGet(leaseId);
        int page = lease.getPage();

        while (atlasImageList.size() <= page) {
          BufferedImage bufferedImage = new BufferedImage(atlasData.width, atlasData.height, BufferedImage.TYPE_INT_ARGB);
          atlasImageList.add(bufferedImage);
        }

        BufferedImage bufferedImage = atlasImageList.get(page);
        int[] rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        bufferedImage.setRGB(lease.getPosX(), lease.getPosY(), image.getWidth(), image.getHeight(), rgb, 0, image.getWidth());
        System.out.println("  -> " + atlasId + ":" + imageId + ", page:" + page + " @ (" + lease.getPosX() + ", " + lease.getPosY() + ")");
      }
    }
  }

  private PackedData collectPackedData(PackData packData, Map<String, Map<String, ImageCollector.ImageData>> atlasImageMap, Map<String, Atlas> atlasMap, Map<String, Map<String, Integer>> atlasLeaseMap) {

    PackedData packedData = new PackedData();

    for (Map.Entry<String, Atlas> entry : atlasMap.entrySet()) {

      String atlasId = entry.getKey();
      Atlas atlas = entry.getValue();
      Map<String, ImageCollector.ImageData> imageMap = atlasImageMap.get(atlasId);
      Map<String, Integer> leaseMap = atlasLeaseMap.get(atlasId);
      PackData.AtlasData atlasData = packData.atlas_definitions.get(atlasId);
      int totalPages = 0;

      for (Map.Entry<String, ImageCollector.ImageData> imageEntry : imageMap.entrySet()) {
        String imageId = imageEntry.getKey();
        ImageCollector.ImageData collectorImageData = imageEntry.getValue();
        int leaseId = leaseMap.get(imageId);
        Lease lease = atlas.leaseGet(leaseId);
        int page = lease.getPage();

        if (page + 1 > totalPages) {
          totalPages = page + 1;
        }

        ImageMetaData metaData = collectorImageData.metaData;

        if (metaData != null && metaData.subImages != null && metaData.subImages.size() > 0) {

          for (Map.Entry<String, ImageMetaData.ImageData> metaEntry : metaData.subImages.entrySet()) {
            String subImageString = metaEntry.getKey();
            ImageMetaData.ImageData imageData = metaEntry.getValue();

            PackedData.ImageData packedImageData = new PackedData.ImageData();
            packedImageData.atlas = this.resolveAtlasImagePathString(atlasId, page, packData.paths.resourcePath, atlasData.path);
            packedImageData.u = lease.getPosX() + imageData.x;
            packedImageData.v = lease.getPosY() + imageData.y;
            packedImageData.width = imageData.width;
            packedImageData.height = imageData.height;

            String actualImageId = this.getActualImageId(imageId, metaData) + "#" + subImageString;
            packedData.image.put(actualImageId, packedImageData);
          }

        } else {

          PackedData.ImageData packedImageData = new PackedData.ImageData();
          packedImageData.atlas = this.resolveAtlasImagePathString(atlasId, page, packData.paths.resourcePath, atlasData.path);
          packedImageData.u = lease.getPosX();
          packedImageData.v = lease.getPosY();
          packedImageData.width = lease.getWidth();
          packedImageData.height = lease.getHeight();

          String actualImageId = this.getActualImageId(imageId, metaData);
          packedData.image.put(actualImageId, packedImageData);
        }
      }

      PackedData.AtlasData packedAtlasData = new PackedData.AtlasData();
      packedAtlasData.width = atlasData.width;
      packedAtlasData.height = atlasData.height;

      for (int i = 0; i < totalPages; i++) {
        String imagePathString = this.resolveAtlasImagePathString(atlasId, i, packData.paths.resourcePath, atlasData.path);
        packedData.atlas.put(imagePathString, packedAtlasData);
      }
    }

    return packedData;
  }

  private String getActualImageId(String imageId, ImageMetaData metaData) {

    String actualImageId;

    if (metaData != null && metaData.id != null) {
      actualImageId = metaData.id;

    } else {
      actualImageId = imageId;
    }
    return actualImageId;
  }

  private String resolveAtlasImagePathString(String atlasId, int page, String outputString, String atlasPath) {

    return this.resolveAtlasImagePath(atlasId, page, outputString, atlasPath).toString().replaceAll("\\\\", "/");
  }

  private Path resolveAtlasImagePath(String atlasId, int page, String outputString, String atlasPath) {

    Path path;

    if (atlasPath != null) {
      path = Paths.get(outputString, atlasPath);

    } else {
      path = Paths.get(outputString);
    }

    return path.resolve(atlasId + "_" + page + ".png");
  }

}
