package com.codetaylor.mc.pyrotech.packer;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class PackAPI {

  private static final Logger LOGGER = LogManager.getLogger(PackAPI.class);

  private static final Map<String, PackedData> PACKED_DATA_MAP = new HashMap<>();

  public static PackedData.ImageData getImageData(ResourceLocation resourceLocation) {

    PackedData packedData = PACKED_DATA_MAP.get(resourceLocation.getResourceDomain());

    if (packedData == null) {
      throw new RuntimeException("Missing packed data for: " + resourceLocation);
    }

    PackedData.ImageData imageData = packedData.image.get(resourceLocation.getResourcePath());

    if (imageData == null) {
      throw new RuntimeException("Missing packed image data for: " + resourceLocation);
    }

    return imageData;
  }

  public static PackedData.AtlasData getAtlasData(ResourceLocation resourceLocation) {

    PackedData packedData = PACKED_DATA_MAP.get(resourceLocation.getResourceDomain());

    if (packedData == null) {
      throw new RuntimeException("Missing packed data for: " + resourceLocation);
    }

    PackedData.AtlasData atlasData = packedData.atlas.get(resourceLocation.getResourcePath());

    if (atlasData == null) {
      throw new RuntimeException("Missing atlas data for: " + resourceLocation);
    }

    return atlasData;
  }

  /**
   * Loads a packed.json file. Must be called during pre-init.
   *
   * @param resourceLocation the location of the packed.json
   */
  public static void register(ResourceLocation resourceLocation) {

    try {
      Minecraft minecraft = Minecraft.getMinecraft();
      IResourceManager resourceManager = minecraft.getResourceManager();
      IResource resource = resourceManager.getResource(resourceLocation);
      InputStream inputStream = resource.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      PackedData packedData = new Gson().fromJson(inputStreamReader, PackedData.class);
      inputStreamReader.close();

      if (PACKED_DATA_MAP.get(resourceLocation.getResourceDomain()) != null) {
        throw new RuntimeException("Duplicate packed.json registered for mod: " + resourceLocation.getResourceDomain());
      }

      PACKED_DATA_MAP.put(resourceLocation.getResourceDomain(), packedData);

    } catch (Exception e) {
      LOGGER.error("Error loading packed data for resource location: " + resourceLocation, e);
    }
  }

  private PackAPI() {
    //
  }
}
