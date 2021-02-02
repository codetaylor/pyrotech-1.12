package com.codetaylor.mc.pyrotech.modules.hunting.plugin.jei;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.minecraft.item.ItemStack;

public class PluginJEI
    implements IModPlugin {

  @Override
  public void register(IModRegistry registry) {

    // --- Blacklist Ingredients

    IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleHunting.Blocks.CARCASS));
  }
}
