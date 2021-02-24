package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.pyrotech.modules.core.block.BlockCharcoalBlock;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.hunting.item.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JsonOreDict {

  private String[] __comments = {
      "WARNING: All changes should be made to the file with the name Custom",
      "in the title. Changes made to the Generated file will be overwritten.",
      "",
      "This file defines the oreDict entries for this module.",
      "Item strings are in the format: (domain):(path):(meta)"
  };

  private Map<String, String[]> oreDict = new TreeMap<>();

  public Map<String, String[]> getOreDict() {

    return this.oreDict;
  }

  public static JsonOreDict generate() {

    JsonOreDict result = new JsonOreDict();

    result.oreDict.put("straw", new String[]{
        "pyrotech:material:" + ItemMaterial.EnumType.STRAW.getMeta(),
    });

    result.oreDict.put("blockCharcoal", new String[]{
        "pyrotech:" + BlockCharcoalBlock.NAME
    });

    result.oreDict.put("cobblestone", new String[]{
        "pyrotech:cobblestone:" + BlockCobblestone.EnumType.ANDESITE.getMeta(),
        "pyrotech:cobblestone:" + BlockCobblestone.EnumType.DIORITE.getMeta(),
        "pyrotech:cobblestone:" + BlockCobblestone.EnumType.GRANITE.getMeta(),
        "pyrotech:cobblestone:" + BlockCobblestone.EnumType.LIMESTONE.getMeta()
    });

    result.oreDict.put("cobblestoneAndesite", new String[]{
        "pyrotech:cobblestone:" + BlockCobblestone.EnumType.ANDESITE.getMeta(),
    });

    result.oreDict.put("cobblestoneDiorite", new String[]{
        "pyrotech:cobblestone:" + BlockCobblestone.EnumType.DIORITE.getMeta(),
    });

    result.oreDict.put("cobblestoneGranite", new String[]{
        "pyrotech:cobblestone:" + BlockCobblestone.EnumType.GRANITE.getMeta(),
    });

    result.oreDict.put("cobblestoneLimestone", new String[]{
        "pyrotech:cobblestone:" + BlockCobblestone.EnumType.LIMESTONE.getMeta()
    });

    result.oreDict.put("toolAxe", new String[]{
        "minecraft:stone_axe:*",
        "minecraft:iron_axe:*",
        "minecraft:golden_axe:*",
        "minecraft:diamond_axe:*",
        "pyrotech:crude_axe:*",
        "pyrotech:flint_axe:*",
        "pyrotech:bone_axe:*",
        "pyrotech:obsidian_axe:*",
        "pyrotech:redstone_axe:*"
    });

    result.oreDict.put("toolSharp", new String[]{
        "minecraft:stone_axe:*",
        "minecraft:iron_axe:*",
        "minecraft:golden_axe:*",
        "minecraft:diamond_axe:*",
        "pyrotech:crude_axe:*",
        "pyrotech:flint_axe:*",
        "pyrotech:bone_axe:*",
        "pyrotech:obsidian_axe:*",
        "pyrotech:redstone_axe:*",
        "minecraft:stone_sword:*",
        "minecraft:iron_sword:*",
        "minecraft:golden_sword:*",
        "minecraft:diamond_sword:*",
        "pyrotech:flint_sword:*",
        "pyrotech:bone_sword:*",
        "pyrotech:obsidian_sword:*",
        "pyrotech:redstone_sword:*",
        "pyrotech:" + ItemHuntingKnife.FLINT_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.BONE_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.STONE_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.IRON_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.GOLD_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.DIAMOND_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.OBSIDIAN_NAME + ":*"
    });

    result.oreDict.put("twine", new String[]{
        "minecraft:string",
        "pyrotech:material:" + ItemMaterial.EnumType.TWINE.getMeta(),
        "pyrotech:material:" + ItemMaterial.EnumType.TWINE_DURABLE.getMeta()
    });

    result.oreDict.put("fuelCoke", new String[]{
        "pyrotech:material:" + ItemMaterial.EnumType.COAL_COKE.getMeta()
    });

    result.oreDict.put("blockFuelCoke", new String[]{
        "pyrotech:coal_coke_block"
    });

    result.oreDict.put("oreNetherCoal", new String[]{
        "pyrotech:dense_nether_coal_ore"
    });

    result.oreDict.put("blockGlass", new String[]{
        "pyrotech:refractory_glass",
        "pyrotech:slag_glass"
    });

    result.oreDict.put("stickStone", new String[]{
        "pyrotech:material:27" // stick_stone
    });

    result.oreDict.put("dustLimestone", new String[]{
        "pyrotech:material:28" // dust_limestone
    });

    result.oreDict.put("stoneLimestone", new String[]{
        "pyrotech:limestone" // limestone
    });

    result.oreDict.put("dustFlint", new String[]{
        "pyrotech:material:31" // dust_flint
    });

    result.oreDict.put("rock", new String[]{
        "pyrotech:rock:" + BlockRock.EnumType.STONE.getMeta(),
        "pyrotech:rock:" + BlockRock.EnumType.DIORITE.getMeta(),
        "pyrotech:rock:" + BlockRock.EnumType.GRANITE.getMeta(),
        "pyrotech:rock:" + BlockRock.EnumType.ANDESITE.getMeta(),
        "pyrotech:rock:" + BlockRock.EnumType.SANDSTONE.getMeta(),
        "pyrotech:rock:" + BlockRock.EnumType.LIMESTONE.getMeta(),
        "pyrotech:rock:" + BlockRock.EnumType.SANDSTONE_RED.getMeta()
    });

    result.oreDict.put("rockLimestone", new String[]{
        "pyrotech:rock:" + BlockRock.EnumType.LIMESTONE.getMeta()
    });

    {
      List<String> list = new ArrayList<>(16);

      for (int i = 0; i < 16; i++) {
        list.add("minecraft:wool:" + i);
      }

      result.oreDict.put("blockWool", list.toArray(new String[0]));
    }

    result.oreDict.put("milk", new String[]{
        "minecraft:milk_bucket",
        "pyrotech:bucket_wood:1",
        "pyrotech:bucket_clay:1",
        "pyrotech:bucket_stone:1"
    });

    result.oreDict.put("toolShears", new String[]{
        "minecraft:shears:*",
        "pyrotech:clay_shears:*",
        "pyrotech:stone_shears:*",
        "pyrotech:bone_shears:*",
        "pyrotech:flint_shears:*",
        "pyrotech:gold_shears:*",
        "pyrotech:diamond_shears:*",
        "pyrotech:obsidian_shears:*"
    });

    result.oreDict.put("toolHuntingKnife", new String[]{
        "pyrotech:" + ItemHuntingKnife.FLINT_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.BONE_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.STONE_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.IRON_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.GOLD_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.DIAMOND_NAME + ":*",
        "pyrotech:" + ItemHuntingKnife.OBSIDIAN_NAME + ":*"
    });

    result.oreDict.put("hideScrapeable", new String[]{
        "pyrotech:" + ItemPelt.NAME_COW,
        "pyrotech:" + ItemPelt.NAME_HORSE,
        "pyrotech:" + ItemPelt.NAME_MOOSHROOM,
        "pyrotech:" + ItemPelt.NAME_POLAR_BEAR,
        "pyrotech:" + ItemPelt.NAME_SHEEP_BLACK,
        "pyrotech:" + ItemPelt.NAME_SHEEP_BLUE,
        "pyrotech:" + ItemPelt.NAME_SHEEP_BLUE_LIGHT,
        "pyrotech:" + ItemPelt.NAME_SHEEP_BROWN,
        "pyrotech:" + ItemPelt.NAME_SHEEP_CYAN,
        "pyrotech:" + ItemPelt.NAME_SHEEP_GRAY,
        "pyrotech:" + ItemPelt.NAME_SHEEP_GRAY_LIGHT,
        "pyrotech:" + ItemPelt.NAME_SHEEP_GREEN,
        "pyrotech:" + ItemPelt.NAME_SHEEP_LIME,
        "pyrotech:" + ItemPelt.NAME_SHEEP_MAGENTA,
        "pyrotech:" + ItemPelt.NAME_SHEEP_ORANGE,
        "pyrotech:" + ItemPelt.NAME_SHEEP_PINK,
        "pyrotech:" + ItemPelt.NAME_SHEEP_PURPLE,
        "pyrotech:" + ItemPelt.NAME_SHEEP_RED,
        "pyrotech:" + ItemPelt.NAME_SHEEP_WHITE,
        "pyrotech:" + ItemPelt.NAME_SHEEP_YELLOW,
        "pyrotech:" + ItemHide.NAME_PIG,
        "pyrotech:" + ItemHide.NAME_SHEEP_SHEARED,
        "pyrotech:" + ItemHide.NAME_LLAMA,
        "pyrotech:" + ItemPelt.NAME_LLAMA_WHITE,
        "pyrotech:" + ItemPelt.NAME_LLAMA_CREAMY,
        "pyrotech:" + ItemPelt.NAME_LLAMA_GRAY,
        "pyrotech:" + ItemPelt.NAME_LLAMA_BROWN,
        "pyrotech:" + ItemPelt.NAME_WOLF
    });

    result.oreDict.put("hideSmallScrapeable", new String[]{
        "pyrotech:" + ItemPelt.NAME_BAT,
        "minecraft:rabbit_hide"
    });

    result.oreDict.put("shard", new String[]{
        "pyrotech:material:" + ItemMaterial.EnumType.POTTERY_SHARD.getMeta(),
        "pyrotech:material:" + ItemMaterial.EnumType.BONE_SHARD.getMeta(),
        "pyrotech:material:" + ItemMaterial.EnumType.FLINT_SHARD.getMeta(),
        "pyrotech:material:" + ItemMaterial.EnumType.GLASS_SHARD.getMeta(),
        "pyrotech:material:" + ItemMaterial.EnumType.GOLD_SHARD.getMeta(),
        "pyrotech:material:" + ItemMaterial.EnumType.DIAMOND_SHARD.getMeta(),
        "pyrotech:material:" + ItemMaterial.EnumType.IRON_SHARD.getMeta(),
        "pyrotech:material:" + ItemMaterial.EnumType.OBSIDIAN_SHARD.getMeta()
    });

    result.oreDict.put("leatherDurable", new String[]{
        "pyrotech:material:" + ItemMaterial.EnumType.LEATHER_DURABLE.getMeta()
    });

    result.oreDict.put("kitRepairLeather", new String[]{
        "pyrotech:" + ItemLeatherRepairKit.NAME + ":*",
        "pyrotech:" + ItemLeatherDurableRepairKit.NAME + ":*"
    });

    return result;
  }

}
