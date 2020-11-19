package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.pyrotech.modules.core.block.BlockCharcoalBlock;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;

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
        "pyrotech:redstone_sword:*"
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

    return result;
  }

}
