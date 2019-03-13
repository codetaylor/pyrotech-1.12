package com.codetaylor.mc.pyrotech.packer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PackDataPathResolver {

  public void resolve(PackData.PathData[] input) {

    for (PackData.PathData pathData : input) {
      pathData.path = this.resolve(pathData.pathString);
    }
  }

  private Path resolve(String pathString) {

    Path path = Paths.get(pathString);

    if (Files.notExists(path)) {
      throw new RuntimeException("Missing path: " + path);
    }

    if (!Files.isDirectory(path)) {
      throw new RuntimeException("Not a path: " + path);
    }

    return path;
  }

}
