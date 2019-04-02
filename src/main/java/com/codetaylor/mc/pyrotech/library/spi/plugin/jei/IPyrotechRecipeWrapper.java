package com.codetaylor.mc.pyrotech.library.spi.plugin.jei;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public interface IPyrotechRecipeWrapper
    extends IRecipeWrapper {

  @Nullable
  ResourceLocation getRegistryName();

}
