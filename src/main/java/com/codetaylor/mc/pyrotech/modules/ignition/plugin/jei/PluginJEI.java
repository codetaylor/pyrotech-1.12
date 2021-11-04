package com.codetaylor.mc.pyrotech.modules.ignition.plugin.jei;

import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnition;
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
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleIgnition.Items.BOW_DRILL_DURABLE_SPINDLE));
  }

}
