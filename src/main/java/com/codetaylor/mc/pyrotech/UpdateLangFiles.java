package com.codetaylor.mc.pyrotech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Updates all lang files from one master lang file.
 *
 * Overwrites each lang file with a master lang file while translating any values
 * that already exist in the overwritten lang file.
 */
public class UpdateLangFiles {

  public static final Logger LOGGER = LogManager.getLogger(UpdateLangFiles.class);

  public static void main(String... args) {

    String modId = "pyrotech";

    Path langPath = Paths.get("src/main/resources/assets/" + modId + "/lang/");
    Path masterFilePath = langPath.resolve("en_us.lang");
    Path outputPath = Paths.get(".");
    List<Path> langFileList;

    try (Stream<Path> paths = Files.walk(langPath)) {
      langFileList = paths
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".lang")
              && !path.toString().endsWith(masterFilePath.toString()))
          .sorted()
          .collect(Collectors.toList());

    } catch (IOException e) {
      LOGGER.error("", e);
      return;
    }

    // Read master file
    List<String> masterList;

    try {
      masterList = new Reader().read(masterFilePath, new ArrayList<>());

    } catch (IOException e) {
      LOGGER.error("Unable to read master file: " + masterFilePath, e);
      return;
    }

    for (Path path : langFileList) {
      process(path, outputPath, masterList);
    }
  }

  protected static void process(Path subFilePath, Path outputPath, List<String> masterList) {

    // Read sub file
    Properties subProperties = new Properties();

    try {
      subProperties.load(Files.newBufferedReader(subFilePath));

    } catch (IOException e) {
      LOGGER.error("Unable to read sub file as property file: " + subFilePath, e);
      return;
    }

    // Output
    List<String> outputList = new ArrayList<>(masterList.size());
    List<String> newKeys = new ArrayList<>();

    for (String masterLine : masterList) {
      String masterLineTrimmed = masterLine.trim();

      // master line is empty
      if (masterLineTrimmed.length() == 0) {
        outputList.add("");
        continue;
      }

      // master line is comment
      if (masterLineTrimmed.startsWith("#")) {
        outputList.add(masterLineTrimmed);
        continue;
      }

      // master line is not a comment
      // look up the translated value
      String[] split = masterLineTrimmed.split("=");
      String property = subProperties.getProperty(split[0]);

      if (property == null) {
        // has no translated value
        // add the master line
        outputList.add(masterLineTrimmed);
        newKeys.add(split[0]);
        continue;
      }

      // has translated value
      // concatenate and add the translated line
      outputList.add(split[0] + "=" + property);
    }

    try {
      outputPath = outputPath.resolve(subFilePath);
      new Writer().write(outputPath, outputList);
      LOGGER.info("Wrote " + newKeys.size() + " new lines to " + outputPath + "\n" + String.join("\n", newKeys));

    } catch (IOException e) {
      LOGGER.error("Failed to write file: " + outputPath, e);
    }
  }

  public static class Reader {

    public List<String> read(Path path, List<String> result) throws IOException {

      try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
        return br.lines().collect(Collectors.toCollection(() -> result));
      }
    }
  }

  public static class Writer {

    public void write(Path path, List<String> toWrite) throws IOException {

      Files.write(path, toWrite);
    }
  }
}
