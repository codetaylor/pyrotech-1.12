package com.codetaylor.mc.pyrotech.library;

import com.codetaylor.mc.athenaeum.util.FileHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

public class JsonInitializer {

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  public static <T> T generateAndReadCustom(Path configurationPath, String generatedFilename, String customFilename, Class<T> dataClass, Supplier<? extends T> generatedDataClassSupplier, Logger logger) {

    if (!JsonInitializer.create(configurationPath, generatedFilename, customFilename, generatedDataClassSupplier, logger)) {
      return null;
    }

    return JsonInitializer.read(configurationPath, customFilename, dataClass, logger);
  }

  @Nullable
  public static <T> T read(Path configurationPath, String customFilename, Class<T> dataClass, @Nullable Logger logger) {

    Path customPath = Paths.get(configurationPath.toString(), customFilename);
    BufferedReader reader = null;

    try {
      reader = Files.newBufferedReader(customPath);
      return GSON.fromJson(reader, dataClass);

    } catch (IOException e) {

      if (logger != null) {
        logger.error("", e);
      }

    } finally {
      FileHelper.closeSilently(reader);
    }

    return null;
  }

  public static <T> boolean create(Path configurationPath, String generatedFilename, String customFilename, Supplier<? extends T> generatedDataClassSupplier, Logger logger) {

    Path generatedPath = Paths.get(configurationPath.toString(), generatedFilename);
    Path customPath = Paths.get(configurationPath.toString(), customFilename);

    if (!Files.exists(configurationPath)) {

      try {
        Files.createDirectories(configurationPath);

      } catch (IOException e) {
        logger.error("", e);
        return false;
      }
    }

    // Delete the generated file if it exists.
    if (Files.exists(generatedPath)) {

      try {
        Files.delete(generatedPath);

      } catch (IOException e) {
        logger.error("", e);
        return false;
      }
    }

    // Create and write the generated file.
    BufferedWriter writer = null;

    try {
      writer = Files.newBufferedWriter(generatedPath);
      GSON.toJson(generatedDataClassSupplier.get(), writer);
      writer.close();

    } catch (IOException e) {
      logger.error("", e);
      return false;

    } finally {
      FileHelper.closeSilently(writer);
    }

    // Copy the generated file to the custom file if the custom file doesn't exist.
    if (!Files.exists(customPath)) {

      try {
        Files.copy(generatedPath, customPath);

      } catch (IOException e) {
        logger.error("", e);
        return false;
      }
    }

    return true;
  }

  private JsonInitializer() {
    //
  }

}
