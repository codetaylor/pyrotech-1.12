package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;

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

    result.oreDict.put("twine", new String[]{
        "minecraft:string",
        "pyrotech:material:" + ItemMaterial.EnumType.TWINE.getMeta()
    });

    result.oreDict.put("stickStone", new String[]{
        "pyrotech:material:27" // stick_stone
    });

    result.oreDict.put("dustLimestone", new String[]{
        "pyrotech:material:28" // dust_limestone
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

    return result;
  }

}
