package com.codetaylor.mc.pyrotech.modules.bloomery.plugin.jei;

import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
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
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleBloomery.Items.TONGS_STONE_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleBloomery.Items.TONGS_FLINT_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleBloomery.Items.TONGS_BONE_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleBloomery.Items.TONGS_IRON_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleBloomery.Items.TONGS_DIAMOND_FULL));
  }

}
