package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.pyrotech.library.JsonInitializer;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.*;

public final class CompatInitializerOre {

  public static final Map<String, String> OREDICT_COLOR_MAP = new TreeMap<String, String>() {{
    this.put("oreAluminum", "e2e2e3");
    this.put("oreArdite", "e85e17");
    this.put("oreCobalt", "1c5bc1");
    this.put("oreCopper", "ffaa1c");
    this.put("oreGold", "fcee4b");
    this.put("oreIridium", "d5d4e7");
    this.put("oreIron", "d8af93");
    this.put("oreLead", "95afee");
    this.put("oreMithril", "9cf9ff");
    this.put("oreNickel", "e4e5d8");
    this.put("oreOctine", "ffaa22");
    this.put("oreOsmium", "9babc4");
    this.put("orePlatinum", "55f3ff");
    this.put("oreSilver", "e8f6fd");
    this.put("oreSyrmorite", "4455bb");
    this.put("oreTin", "c3e9ff");
    this.put("oreUranium", "5c734b");
  }};

  public static void create(Path configurationPath) {

    JsonInitializer.create(
        configurationPath.resolve(ModuleCore.MOD_ID),
        "core.compat.Ore-Generated.json",
        "core.compat.Ore-Custom.json",
        () -> CompatInitializerOre.createGeneratedData(new OreCompatData()),
        ModuleCore.LOGGER
    );
  }

  @Nullable
  public static OreCompatData read(Path configurationPath) {

    return JsonInitializer.read(
        configurationPath.resolve(ModuleTechMachine.MOD_ID),
        "core.compat.Ore-Custom.json",
        OreCompatData.class,
        null // prevent logging errors
    );
  }

  private static OreCompatData createGeneratedData(OreCompatData data) {

    for (Map.Entry<String, String> entry : OREDICT_COLOR_MAP.entrySet()) {

      String oreKey = entry.getKey();
      NonNullList<ItemStack> oreItemStacks = OreDictionary.getOres(oreKey);

      if (oreItemStacks.isEmpty()) {
        continue;
      }

      String nuggetKey = oreKey.replaceAll("ore", "nugget");
      NonNullList<ItemStack> nuggetItemStacks = OreDictionary.getOres(nuggetKey);

      if (nuggetItemStacks.isEmpty()) {
        continue;
      }

      // lang keys

      List<String> langKeys = new ArrayList<>();

      for (ItemStack itemStack : oreItemStacks) {
        Item item = itemStack.getItem();
        ResourceLocation registryName = item.getRegistryName();

        if (registryName == null) {
          continue;
        }

        String unlocalizedName = registryName.getResourceDomain() + ":"
            + item.getUnlocalizedName(itemStack);

        if (unlocalizedName.endsWith(".name")) {
          unlocalizedName = unlocalizedName.substring(0, unlocalizedName.length() - 5);
        }

        langKeys.add(unlocalizedName);
      }

      Collections.sort(langKeys);

      // nuggets

      List<String> nuggetItemStrings = new ArrayList<>();

      for (ItemStack itemStack : nuggetItemStacks) {
        Item item = itemStack.getItem();
        ResourceLocation registryName = item.getRegistryName();

        if (registryName == null) {
          continue;
        }

        String itemString = registryName.getResourceDomain() + ":"
            + registryName.getResourcePath() + ":" + itemStack.getMetadata();
        nuggetItemStrings.add(itemString);
      }

      Collections.sort(nuggetItemStrings);

      // add entry

      OreCompatOreDictEntry oreCompatOreDictEntry = data.oredict.computeIfAbsent(oreKey, s -> new OreCompatOreDictEntry());
      oreCompatOreDictEntry.slagColor = entry.getValue();
      oreCompatOreDictEntry.langKey = langKeys.toArray(new String[0]);
      oreCompatOreDictEntry.output = nuggetItemStrings.toArray(new String[0]);
    }

    return data;
  }

  public static class OreCompatData {

    private String[] __comments = {
        "WARNING: All changes should be made to the file with the name Custom",
        "in the title. Changes made to the Generated file will be overwritten.",
        "",
        "This file defines ore compatibility.",
        "",
        "Slag content will be generated for each oredict entry.",
        "",
        "The first valid key in each langKey list will be chosen; reorder keys",
        "to change which key is selected.",
        "",
        "The first valid nugget in each output list will be chosen; reorder nuggets",
        "to change which nugget is selected.",
        "",
        "Slag color is a hex color code without the #",
        "",
        "Lang keys are in the format: (domain):(key)",
        "",
        "Output item strings are in the format: (domain):(path):(meta)"
    };

    public Map<String, OreCompatOreDictEntry> oredict = new TreeMap<>();
  }

  public static class OreCompatOreDictEntry {

    public String slagColor = "ffffff";
    public String[] langKey = new String[0];
    public String[] output = new String[0];
  }

  private CompatInitializerOre() {
    //
  }

}
