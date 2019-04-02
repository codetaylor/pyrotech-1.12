package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.IPyrotechRecipeWrapper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CampfireRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperCampfire
    implements IPyrotechRecipeWrapper {

  private static final String TIME_STRING = StringHelper.ticksToHMS(ModuleTechBasicConfig.CAMPFIRE.COOK_TIME_TICKS);

  private final ResourceLocation registryName;
  private final List<List<ItemStack>> inputs;
  private final ItemStack output;

  public JEIRecipeWrapperCampfire(CampfireRecipe recipe) {

    this(recipe.getInput(), recipe.getOutput(), recipe.getRegistryName());
  }

  public JEIRecipeWrapperCampfire(Ingredient input, ItemStack output) {

    this(input, output, null);
  }

  private JEIRecipeWrapperCampfire(Ingredient input, ItemStack output, ResourceLocation registryName) {

    this.inputs = Collections.singletonList(Arrays.asList(input.getMatchingStacks()));
    this.output = output;
    this.registryName = registryName;
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutput(VanillaTypes.ITEM, this.output);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    int stringWidth = minecraft.fontRenderer.getStringWidth(TIME_STRING);
    minecraft.fontRenderer.drawString(TIME_STRING, 36 - stringWidth / 2, 22, Color.DARK_GRAY.getRGB());
  }

  @Override
  public ResourceLocation getRegistryName() {

    return this.registryName;
  }
}
