package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperKilnPit
    extends JEIRecipeWrapperTimed {

  private final List<List<ItemStack>> inputs;
  private final List<List<ItemStack>> outputs;
  private final String failureChance;

  public JEIRecipeWrapperKilnPit(KilnPitRecipe recipe) {

    super(recipe);

    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));

    this.outputs = new ArrayList<>();
    this.outputs.add(Collections.singletonList(recipe.getOutput()));
    this.outputs.add(Arrays.asList(recipe.getFailureItems()));

    this.failureChance = Util.translateFormatted(
        "gui." + ModulePyrotech.MOD_ID + ".jei.failure",
        (int) (recipe.getFailureChance() * 100)
    );
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutputLists(VanillaTypes.ITEM, this.outputs);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);

    int stringWidth = minecraft.fontRenderer.getStringWidth(this.failureChance);
    minecraft.fontRenderer.drawString(this.failureChance, recipeWidth - stringWidth, 44, Color.DARK_GRAY.getRGB());
  }
}
