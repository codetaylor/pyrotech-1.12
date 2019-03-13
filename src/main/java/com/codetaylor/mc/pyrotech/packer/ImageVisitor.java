package com.codetaylor.mc.pyrotech.packer;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

public class ImageVisitor
    extends SimpleFileVisitor<Path> {

  private final Gson gson;
  private final Path path;
  private final Map<String, ImageCollector.ImageData> imageMap;

  public ImageVisitor(Gson gson, Path path, Map<String, ImageCollector.ImageData> imageMap) {

    this.gson = gson;
    this.path = path;
    this.imageMap = imageMap;
  }

  // Print information about
  // each type of file.
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {

    String fileString = file.toString();
    String id = fileString;

    if (id.startsWith(this.path.toString())) {
      id = id.substring(this.path.toString().length() + 1);
    }

    if (attr.isRegularFile()) {

      if (id.endsWith(".png")) {
        System.out.format("  -> %s", id);
        BufferedImage image = ImageIO.read(file.toFile());
        System.out.format(" [%d x %d]", image.getWidth(), image.getHeight());
        id = id.replaceAll("\\\\", "/").substring(0, id.length() - 4);

        ImageCollector.ImageData imageData = this.imageMap.computeIfAbsent(id, s -> new ImageCollector.ImageData());
        imageData.image = image;

      } else if (id.endsWith(".json")) {

        System.out.format("  -> %s", id);
        id = id.replaceAll("\\\\", "/").substring(0, id.length() - 9);

        String jsonFileString = fileString.substring(0, fileString.length() - 5);
        Path imagePath = Paths.get(jsonFileString);

        if (Files.exists(imagePath)) { // check that the image exists
          ImageMetaData imageMetaData = this.gson.fromJson(new FileReader(Paths.get(jsonFileString + ".json").toFile()), ImageMetaData.class);
          ImageCollector.ImageData imageData = this.imageMap.computeIfAbsent(id, s -> new ImageCollector.ImageData());
          imageData.metaData = imageMetaData;
        }
      }
    }

    System.out.println(" (" + attr.size() + " b)");
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

    System.out.format("  -> %s%n", dir);
    return FileVisitResult.CONTINUE;
  }
}