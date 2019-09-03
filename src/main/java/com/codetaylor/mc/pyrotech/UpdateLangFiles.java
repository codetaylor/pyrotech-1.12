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
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Updates all lang files from one master lang file.
 * <p>
 * Overwrites each lang file with a master lang file while translating any values
 * that already exist in the overwritten lang file.
 */
public class UpdateLangFiles {

  public static final Logger LOGGER = LogManager.getLogger(UpdateLangFiles.class);

  public static void main(String... args) {

    new LangFileUpdater(
        "pyrotech",
        "en_us.lang",
        ".",
        new FileLocator(),
        new Converter(),
        new Writer()
    ).update();
  }

  public static class LangFileUpdater {

    private final Path langPath;
    private final Path masterFilePath;
    private final Path outputPath;
    private FileLocator fileLocator;
    private Converter converter;
    private Writer writer;

    public LangFileUpdater(String modId, String masterLangFile, String outputPath, FileLocator fileLocator, Converter converter, Writer writer) {

      this.langPath = Paths.get("src/main/resources/assets/" + modId + "/lang/");
      this.masterFilePath = this.langPath.resolve(masterLangFile);
      this.outputPath = Paths.get(outputPath);
      this.fileLocator = fileLocator;
      this.converter = converter;
      this.writer = writer;
    }

    public void update() {

      // Read master file
      List<String> masterList;

      try {
        masterList = new Reader().read(this.masterFilePath, new ArrayList<>());

      } catch (IOException e) {
        LOGGER.error("Unable to read master file: " + this.masterFilePath, e);
        return;
      }

      List<Path> langFileList = this.fileLocator.locate(
          this.langPath,
          new PathFilter(this.masterFilePath.toString())
      );

      for (Path path : langFileList) {
        this.converter.convert(path, this.outputPath, masterList, this.writer);
      }
    }

  }

  public static class Converter {

    public void convert(Path subFilePath, Path outputPath, List<String> masterList, Writer writer) {

      Properties subProperties;

      try {
        subProperties = new PropertiesReader().read(subFilePath);

      } catch (IOException e) {
        LOGGER.error("Unable to load lang file: " + subFilePath, e);
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
        writer.write(outputPath, outputList);
        LOGGER.info("Wrote " + newKeys.size() + " new lines to " + outputPath + "\n" + String.join("\n", newKeys));

      } catch (IOException e) {
        LOGGER.error("Failed to write file: " + outputPath, e);
      }
    }
  }

  public static class PathFilter
      implements Predicate<Path> {

    private final String masterFilePathString;

    public PathFilter(String masterFilePathString) {

      this.masterFilePathString = masterFilePathString;
    }

    @Override
    public boolean test(Path path) {

      String pathString = path.toString();
      return pathString.endsWith(".lang")
          && !pathString.endsWith(this.masterFilePathString);
    }
  }

  public static class FileLocator {

    public List<Path> locate(Path langPath, Predicate<? super Path> filter) {

      try (Stream<Path> paths = Files.walk(langPath)) {
        return paths
            .filter(Files::isRegularFile)
            .filter(filter)
            .sorted()
            .collect(Collectors.toList());

      } catch (IOException e) {
        LOGGER.error("", e);
        return Collections.emptyList();
      }
    }
  }

  public static class PropertiesReader {

    public Properties read(Path path) throws IOException {

      // Read sub file
      Properties subProperties = new Properties();
      subProperties.load(Files.newBufferedReader(path));
      return subProperties;
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
