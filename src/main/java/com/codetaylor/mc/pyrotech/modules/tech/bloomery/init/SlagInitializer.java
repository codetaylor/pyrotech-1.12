package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init;

import com.codetaylor.mc.athenaeum.util.CloneStateMapper;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.library.JsonInitializer;
import com.codetaylor.mc.pyrotech.modules.core.init.CompatInitializerOre;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper.PROPERTY_STRING_MAPPER;

public final class SlagInitializer {

  // These are used during Bloomery recipe registration in the ore compat system.
  public static final Map<String, ItemSlag> SLAG_BY_OREDICT = new HashMap<>();
  public static final Map<String, BlockPileSlag> SLAG_PILE_BY_OREDICT = new HashMap<>();

  public static void initializeSlag(File configurationDirectory) {

    Path configurationPath = Paths.get(configurationDirectory.toString(), ModuleTechBloomery.MOD_ID);

    JsonSlagList jsonSlagList = JsonInitializer.generateAndReadCustom(
        configurationPath,
        "module.tech.Bloomery.Slag-Generated.json",
        "module.tech.Bloomery.Slag-Custom.json",
        JsonSlagList.class,
        () -> SlagInitializer.createGeneratedDataSlagList(new JsonSlagList()),
        ModuleTechBloomery.LOGGER
    );

    if (jsonSlagList == null) {
      return;
    }

    for (JsonSlagList.JsonSlagListEntry entry : jsonSlagList.getList()) {

      String colorString = entry.getColor();

      if (colorString.length() != 6) {
        ModuleTechBloomery.LOGGER.error("Malformed hex-color code: " + colorString);
        continue;
      }

      int color;

      try {
        color = Integer.decode("0x" + colorString);

      } catch (Exception e) {
        ModuleTechBloomery.LOGGER.error("Unable to decode hex-color string: " + colorString, e);
        continue;
      }

      String registryName = entry.getRegistryName()
          .replace("[^A-Za-z0-9_]", "_");

      String langKey = entry.getLangKey();
      ItemSlag itemSlag = SlagInitializer.generateSlagItem(registryName + "_custom", langKey, color);
      SlagInitializer.generateSlagBlock(registryName + "_custom", langKey, color, itemSlag);
    }
  }

  public static void initializeSlagFromOreCompat(Path configurationPath) {

    CompatInitializerOre.OreCompatData data = CompatInitializerOre.read(configurationPath);

    if (data == null) {
      return;
    }

    for (Map.Entry<String, CompatInitializerOre.OreCompatOreDictEntry> entry : data.oredict.entrySet()) {
      String oreDictKey = entry.getKey();
      CompatInitializerOre.OreCompatOreDictEntry oreDictEntry = entry.getValue();

      // lang key

      if (oreDictEntry.langKey == null
          || oreDictEntry.langKey.length == 0) {
        continue;
      }

      List<String> langKeysToTest = new ArrayList<>(oreDictEntry.langKey.length);

      for (String itemString : oreDictEntry.langKey) {
        String[] split = itemString.split(":");
        StringBuilder langKey = new StringBuilder();

        if (split.length > 2) {

          for (int i = 1; i < split.length; i++) {
            langKey.append(":").append(split[i]);
          }
        } else if (split.length == 2) {
          langKey.append(split[1]);

        } else {
          langKey.append(split[0]);
        }

        langKeysToTest.add(langKey.toString());
      }

      String langKey = SlagInitializer.getFirstValidLangKey(langKeysToTest);

      if (langKey == null) {
        ModuleTechBloomery.LOGGER.error("Missing ore compat lang key for: " + oreDictKey);
        continue;
      }

      // slag color

      String colorString = oreDictEntry.slagColor;

      if (colorString.length() != 6) {
        ModuleTechBloomery.LOGGER.error("Malformed ore compat slag hex-color code: " + colorString);
        continue;
      }

      int color;

      try {
        color = Integer.decode("0x" + colorString);

      } catch (Exception e) {
        ModuleTechBloomery.LOGGER.error("Unable to decode ore compat slag hex-color string: " + colorString, e);
        continue;
      }

      String registryName = oreDictKey
          .replaceAll("ore", "")
          .toLowerCase()
          .replace("[^a-z0-9_]", "_");

      ItemSlag itemSlag = SlagInitializer.generateSlagItem(registryName, langKey, color);
      BlockPileSlag blockPileSlag = SlagInitializer.generateSlagBlock(registryName, langKey, color, itemSlag);
      SlagInitializer.SLAG_BY_OREDICT.put(oreDictKey, itemSlag);
      SlagInitializer.SLAG_PILE_BY_OREDICT.put(oreDictKey, blockPileSlag);
    }
  }

  @Nullable
  public static String getFirstValidLangKey(List<String> langKeysToTest) {

    for (String langKey : langKeysToTest) {

      if (I18n.canTranslate(langKey)) {
        return langKey;

      } else if (I18n.canTranslate(langKey + ".name")) {
        return langKey + ".name";
      }
    }

    return null;
  }

  private static JsonSlagList createGeneratedDataSlagList(JsonSlagList jsonSlagList) {

    // Currently empty.

    return jsonSlagList;
  }

  private static ItemSlag generateSlagItem(String registryName, String langKey, int color) {

    ItemSlag slagItem = new ItemSlag();
    ResourceLocation resourceLocation = new ResourceLocation(
        ModuleTechBloomery.MOD_ID,
        "generated_" + ItemSlag.NAME + "_" + registryName
    );

    String unlocalizedName = ModuleTechBloomery.MOD_ID + "." + ItemSlag.NAME;
    registerItem(slagItem, resourceLocation, unlocalizedName);
    ModuleTechBloomery.Items.GENERATED_SLAG.put(slagItem, new ItemSlag.Properties(langKey, color));
    return slagItem;
  }

  private static BlockPileSlag generateSlagBlock(String registryName, String langKey, int color, ItemSlag slagItem) {

    BlockPileSlag slagBlock = new BlockPileSlag();
    ResourceLocation resourceLocation = new ResourceLocation(
        ModuleTechBloomery.MOD_ID,
        "generated_" + BlockPileSlag.NAME + "_" + registryName
    );

    String unlocalizedName = ModuleTechBloomery.MOD_ID + "." + BlockPileSlag.NAME;
    BlockPileSlag.ItemBlockPileSlag item = new BlockPileSlag.ItemBlockPileSlag(slagBlock);
    registerItem(item, resourceLocation, unlocalizedName);
    registerBlock(slagBlock, resourceLocation, unlocalizedName);
    ModuleTechBloomery.Blocks.GENERATED_PILE_SLAG.put(slagBlock, new BlockPileSlag.Properties(langKey, color, slagItem));
    return slagBlock;
  }

  private static void registerItem(Item item, ResourceLocation registryName, String unlocalizedName) {

    item.setRegistryName(registryName);
    item.setUnlocalizedName(unlocalizedName);
    item.setCreativeTab(ModuleTechBloomery.CREATIVE_TAB);
    ForgeRegistries.ITEMS.register(item);
  }

  private static void registerBlock(Block block, ResourceLocation registryName, String unlocalizedName) {

    block.setRegistryName(registryName);
    block.setUnlocalizedName(unlocalizedName);
    block.setCreativeTab(ModuleTechBloomery.CREATIVE_TAB);
    ForgeRegistries.BLOCKS.register(block);
  }

  @SideOnly(Side.CLIENT)
  public static void initializeSlagModels() {

    ModuleTechBloomery.Items.GENERATED_SLAG.forEach((itemSlag, properties) -> {
      ResourceLocation resourceLocation = new ResourceLocation(ModuleTechBloomery.MOD_ID, ItemSlag.NAME);
      ModelResourceLocation modelResourceLocation = new ModelResourceLocation(resourceLocation, null);
      ModelRegistrationHelper.registerItemModel(itemSlag, 0, modelResourceLocation);
    });

    ModuleTechBloomery.Blocks.GENERATED_PILE_SLAG.forEach((blockPileSlag, properties) -> {
      ModelRegistrationHelper.registerItemModel(
          Item.getItemFromBlock(blockPileSlag),
          new ModelResourceLocation(
              new ResourceLocation(ModuleTechBloomery.MOD_ID, BlockPileSlag.NAME),
              PROPERTY_STRING_MAPPER.getPropertyString(blockPileSlag.getDefaultState().getProperties())
          )
      );
      ModelLoader.setCustomStateMapper(
          blockPileSlag,
          CloneStateMapper.forBlock(ModuleTechBloomery.Blocks.PILE_SLAG)
      );
    });
  }

  @SideOnly(Side.CLIENT)
  public static void initializeSlagColors() {

    Minecraft minecraft = Minecraft.getMinecraft();
    ItemColors itemColors = minecraft.getItemColors();
    BlockColors blockColors = minecraft.getBlockColors();

    ModuleTechBloomery.Items.GENERATED_SLAG.forEach((itemSlag, properties) -> {
      itemColors.registerItemColorHandler(new ItemColor(properties.color, 1), itemSlag);
    });

    ModuleTechBloomery.Blocks.GENERATED_PILE_SLAG.forEach((blockPileSlag, properties) -> {
      Item itemBlock = Item.getItemFromBlock(blockPileSlag);
      itemColors.registerItemColorHandler(new ItemColor(properties.color, 0), itemBlock);
      blockColors.registerBlockColorHandler(new BlockColor(properties.color), blockPileSlag);
    });

    int defaultColor = Integer.decode("0x676767");

    // Slag Item
    itemColors.registerItemColorHandler(
        new ItemColor(defaultColor, 1),
        ModuleTechBloomery.Items.SLAG
    );

    // Slag Block Item
    itemColors.registerItemColorHandler(
        new ItemColor(defaultColor, 0),
        Item.getItemFromBlock(ModuleTechBloomery.Blocks.PILE_SLAG)
    );
    blockColors.registerBlockColorHandler(new BlockColor(defaultColor), ModuleTechBloomery.Blocks.PILE_SLAG);
  }

  @SideOnly(Side.CLIENT)
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

  @SideOnly(Side.CLIENT)
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
