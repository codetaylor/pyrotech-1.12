package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.IPyrotechRecipeWrapper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInItemOutBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperOven
    implements IPyrotechRecipeWrapper {

  private final ResourceLocation registryName;
  private final List<List<ItemStack>> inputs;
  private final ItemStack output;
  private final String timeString;

  public JEIRecipeWrapperOven(MachineRecipeItemInItemOutBase recipe) {

    this(recipe.getInput(), recipe.getOutput(), recipe.getTimeTicks(), recipe.getRegistryName());
  }

  public JEIRecipeWrapperOven(Ingredient input, ItemStack output, int timeTicks) {

    this(input, output, timeTicks, null);
  }

  private JEIRecipeWrapperOven(Ingredient input, ItemStack output, int timeTicks, ResourceLocation registryName) {

    this.registryName = registryName;
    this.inputs = Collections.singletonList(Arrays.asList(input.getMatchingStacks()));
    this.output = output;
    this.timeString = StringHelper.ticksToHMS(timeTicks);
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

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {

    return this.registryName;
  }
}
