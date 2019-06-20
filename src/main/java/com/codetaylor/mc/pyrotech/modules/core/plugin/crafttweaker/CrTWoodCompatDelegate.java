package com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker;

import com.codetaylor.mc.pyrotech.modules.core.init.WoodCompatInitializer;
import com.codetaylor.mc.pyrotech.modules.core.init.recipe.VanillaCraftingRecipesRemove;
import com.codetaylor.mc.pyrotech.modules.core.init.recipe.VanillaFurnaceRecipesRemove;
import crafttweaker.mc1120.events.ActionApplyEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.nio.file.Path;

public class CrTWoodCompatDelegate {

  private final Path configurationPath;

  private boolean isInitialized;

  public CrTWoodCompatDelegate(Path configurationPath) {

    this.configurationPath = configurationPath;
  }

  @SubscribeEvent
  public void on(ActionApplyEvent event) {

    if (!this.isInitialized) {
      this.isInitialized = true;
      WoodCompatInitializer.create(this.configurationPath);
      VanillaCraftingRecipesRemove.apply(ForgeRegistries.RECIPES);
      VanillaFurnaceRecipesRemove.apply();
    }
  }
}
