package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.IPyrotechRecipeWrapper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CampfireRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
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

  private final ResourceLocation registryName;
  private final String timeString;
  private final List<List<ItemStack>> inputs;
  private final ItemStack output;

  public JEIRecipeWrapperCampfire(CampfireRecipe recipe) {

    this(recipe.getInput(), recipe.getOutput(), recipe.getRegistryName(), recipe.getTicks());
  }

  public JEIRecipeWrapperCampfire(Ingredient input, ItemStack output, int ticks) {

    this(input, output, null, ticks);
  }

  private JEIRecipeWrapperCampfire(Ingredient input, ItemStack output, ResourceLocation registryName, int ticks) {

    this.inputs = Collections.singletonList(Arrays.asList(input.getMatchingStacks()));
    this.output = output;
    this.registryName = registryName;
    this.timeString = StringHelper.ticksToHMS(ticks);
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutput(VanillaTypes.ITEM, this.output);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    int stringWidth = minecraft.fontRenderer.getStringWidth(this.timeString);
    minecraft.fontRenderer.drawString(this.timeString, 36 - stringWidth / 2, 22, Color.DARK_GRAY.getRGB());
  }

  @Override
  public ResourceLocation getRegistryName() {

    return this.registryName;
  }
}
