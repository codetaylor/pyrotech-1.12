package com.codetaylor.mc.pyrotech.library;

import com.codetaylor.mc.athenaeum.util.FileHelper;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

public class JsonInitializer {

  public static <T> T generateAndReadCustom(Path configurationPath, String generatedFilename, String customFilename, Class<T> dataClass, Supplier<? extends T> generatedDataClassSupplier, Logger logger) {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    if (!Files.exists(configurationPath)) {

      try {
        Files.createDirectories(configurationPath);

      } catch (IOException e) {
        ModuleBloomery.LOGGER.error("", e);
      }
    }

    Path generatedPath = Paths.get(configurationPath.toString(), generatedFilename);
    Path customPath = Paths.get(configurationPath.toString(), customFilename);

    // Delete the generated file if it exists.
    if (Files.exists(generatedPath)) {

      try {
        Files.delete(generatedPath);

      } catch (IOException e) {
        logger.error("", e);
      }
    }

    // Create and write the generated file.
    BufferedWriter writer = null;

    try {
      writer = Files.newBufferedWriter(generatedPath);
      gson.toJson(generatedDataClassSupplier.get(), writer);
      writer.close();

    } catch (IOException e) {
      logger.error("", e);

    } finally {
      FileHelper.closeSilently(writer);
    }

    // Copy the generated file to the custom file if the custom file doesn't exist.
    if (!Files.exists(customPath)) {

      try {
        Files.copy(generatedPath, customPath);

      } catch (IOException e) {
        logger.error("", e);
      }
    }

    BufferedReader reader = null;

    try {
      reader = Files.newBufferedReader(customPath);
      return gson.fromJson(reader, dataClass);

    } catch (IOException e) {
      logger.error("", e);

    } finally {
      FileHelper.closeSilently(reader);
    }

    return null;
  }

}
