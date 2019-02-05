package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init;

import com.codetaylor.mc.athenaeum.util.CloneStateMapper;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.library.JsonInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.ItemSlag;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper.PROPERTY_STRING_MAPPER;

public final class SlagInitializer {

  public static void initializeSlag(File configurationDirectory) {

    Path configurationPath = Paths.get(configurationDirectory.toString(), ModuleBloomery.MOD_ID);

    JsonSlagList jsonSlagList = JsonInitializer.generateAndReadCustom(
        configurationPath,
        "module.tech.Bloomery.Slag-Generated.json",
        "module.tech.Bloomery.Slag-Custom.json",
        JsonSlagList.class,
        () -> SlagInitializer.createGeneratedDataSlagList(new JsonSlagList()),
        ModuleBloomery.LOGGER
    );

    if (jsonSlagList == null) {
      return;
    }

    for (JsonSlagList.JsonSlagListEntry entry : jsonSlagList.getList()) {

      String colorString = entry.getColor();

      if (colorString.length() != 6) {
        ModuleBloomery.LOGGER.error("Malformed hex-color code: " + colorString);
        continue;
      }

      int color;

      try {
        color = Integer.decode("0x" + colorString);

      } catch (Exception e) {
        ModuleBloomery.LOGGER.error("Unable to decode hex-color string: " + colorString, e);
        continue;
      }

      String registryName = entry.getRegistryName()
          .replace("[^A-Za-z0-9_]", "_");

      SlagInitializer.generateSlag(registryName, entry.getLangKey(), color);
    }
  }

  private static JsonSlagList createGeneratedDataSlagList(JsonSlagList jsonSlagList) {

    List<JsonSlagList.JsonSlagListEntry> list = jsonSlagList.getList();

    // Vanilla
    list.add(new JsonSlagList.JsonSlagListEntry("iron", "tile.oreIron", "d8af93"));
    list.add(new JsonSlagList.JsonSlagListEntry("gold", "tile.oreGold", "fcee4b"));

    // TODO: hook for auto-generation of modded ore slag

    return jsonSlagList;
  }

  private static void generateSlag(String registryName, String langKey, int color) {

    ItemSlag slagItem = new ItemSlag();
    {
      ResourceLocation resourceLocation = new ResourceLocation(
          ModuleBloomery.MOD_ID,
          "generated_" + ItemSlag.NAME + "_" + registryName
      );

      String unlocalizedName = ModuleBloomery.MOD_ID + "." + ItemSlag.NAME;
      registerItem(slagItem, resourceLocation, unlocalizedName);
      ModuleBloomery.Items.GENERATED_SLAG.put(slagItem, new ItemSlag.Properties(langKey, color));
    }

    BlockPileSlag slagBlock = new BlockPileSlag();
    {
      ResourceLocation resourceLocation = new ResourceLocation(
          ModuleBloomery.MOD_ID,
          "generated_" + BlockPileSlag.NAME + "_" + registryName
      );

      String unlocalizedName = ModuleBloomery.MOD_ID + "." + BlockPileSlag.NAME;
      BlockPileSlag.ItemBlockPileSlag item = new BlockPileSlag.ItemBlockPileSlag(slagBlock);
      registerItem(item, resourceLocation, unlocalizedName);
      registerBlock(slagBlock, resourceLocation, unlocalizedName);
      ModuleBloomery.Blocks.GENERATED_PILE_SLAG.put(slagBlock, new BlockPileSlag.Properties(langKey, color, slagItem));
    }

  }

  private static void registerItem(Item item, ResourceLocation registryName, String unlocalizedName) {

    item.setRegistryName(registryName);
    item.setUnlocalizedName(unlocalizedName);
    item.setCreativeTab(ModuleBloomery.CREATIVE_TAB);
    ForgeRegistries.ITEMS.register(item);
  }

  private static void registerBlock(Block block, ResourceLocation registryName, String unlocalizedName) {

    block.setRegistryName(registryName);
    block.setUnlocalizedName(unlocalizedName);
    block.setCreativeTab(ModuleBloomery.CREATIVE_TAB);
    ForgeRegistries.BLOCKS.register(block);
  }

  public static void initializeSlagModels() {

    ModuleBloomery.Items.GENERATED_SLAG.forEach((itemSlag, properties) -> {
      ResourceLocation resourceLocation = new ResourceLocation(ModuleBloomery.MOD_ID, ItemSlag.NAME);
      ModelResourceLocation modelResourceLocation = new ModelResourceLocation(resourceLocation, null);
      ModelRegistrationHelper.registerItemModel(itemSlag, 0, modelResourceLocation);
    });

    ModuleBloomery.Blocks.GENERATED_PILE_SLAG.forEach((blockPileSlag, properties) -> {
      ModelRegistrationHelper.registerItemModel(
          Item.getItemFromBlock(blockPileSlag),
          new ModelResourceLocation(
              new ResourceLocation(ModuleBloomery.MOD_ID, BlockPileSlag.NAME),
              PROPERTY_STRING_MAPPER.getPropertyString(blockPileSlag.getDefaultState().getProperties())
          )
      );
      ModelLoader.setCustomStateMapper(
          blockPileSlag,
          CloneStateMapper.forBlock(ModuleBloomery.Blocks.PILE_SLAG)
      );
    });
  }

  public static void initializeSlagColors() {

    Minecraft minecraft = Minecraft.getMinecraft();
    ItemColors itemColors = minecraft.getItemColors();
    BlockColors blockColors = minecraft.getBlockColors();

    ModuleBloomery.Items.GENERATED_SLAG.forEach((itemSlag, properties) -> {
      itemColors.registerItemColorHandler(new ItemColor(properties.color, 1), itemSlag);
    });

    ModuleBloomery.Blocks.GENERATED_PILE_SLAG.forEach((blockPileSlag, properties) -> {
      Item itemBlock = Item.getItemFromBlock(blockPileSlag);
      itemColors.registerItemColorHandler(new ItemColor(properties.color, 0), itemBlock);
      blockColors.registerBlockColorHandler(new BlockColor(properties.color), blockPileSlag);
    });

    int defaultColor = Integer.decode("0x676767");

    // Slag Item
    itemColors.registerItemColorHandler(
        new ItemColor(defaultColor, 1),
        ModuleBloomery.Items.SLAG
    );

    // Slag Block Item
    itemColors.registerItemColorHandler(
        new ItemColor(defaultColor, 0),
        Item.getItemFromBlock(ModuleBloomery.Blocks.PILE_SLAG)
    );
    blockColors.registerBlockColorHandler(new BlockColor(defaultColor), ModuleBloomery.Blocks.PILE_SLAG);
  }

  private static class ItemColor
      implements IItemColor {

    private final int color;
    private int tintIndex;

    private ItemColor(int color, int tintIndex) {

      this.color = color;
      this.tintIndex = tintIndex;
    }

    @Override
    public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {

      if (tintIndex == this.tintIndex) {
        return this.color;
      }

      return 0xFFFFFF;
    }

  }

  private static class BlockColor
      implements IBlockColor {

    private final int color;

    private BlockColor(int color) {

      this.color = color;
    }

    @Override
    public int colorMultiplier(@Nonnull IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {

      if (tintIndex == 0) {
        return this.color;
      }

      return 0xFFFFFF;
    }

  }

  private SlagInitializer() {
    //
  }
}
