package com.codetaylor.mc.pyrotech.modules.core.init;

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
        "pyrotech:obsidian_axe:*"
    });

    result.oreDict.put("twine", new String[]{
        "minecraft:string",
        "pyrotech:material:" + ItemMaterial.EnumType.TWINE.getMeta()
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
        "pyrotech:rock:" + BlockRock.EnumType.LIMESTONE.getMeta()
    });

    result.oreDict.put("mulchNitrogen", new String[]{
        "minecraft:mutton",
        "minecraft:rabbit",
        "minecraft:rotten_flesh",
        "minecraft:chicken",
        "minecraft:beef",
        "minecraft:fish:0",
        "minecraft:fish:1",
        "minecraft:fish:2",
        "minecraft:fish:3",
        "minecraft:porkchop",
        "minecraft:dye:15"
    });

    {
      List<String> list = new ArrayList<>(16);

      for (int i = 0; i < 16; i++) {
        list.add("minecraft:wool:" + i);
      }

      result.oreDict.put("blockWool", list.toArray(new String[0]));
    }

    return result;
  }

}
