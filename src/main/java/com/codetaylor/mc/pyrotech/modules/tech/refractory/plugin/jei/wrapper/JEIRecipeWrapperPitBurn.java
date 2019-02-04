package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.wrapper;

import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperPitBurn
    extends JEIRecipeWrapperTimed {

  private final List<List<ItemStack>> inputs;
  private final List<List<ItemStack>> outputs;
  private final FluidStack fluidStack;
  private final String failureChance;

  public JEIRecipeWrapperPitBurn(PitBurnRecipe recipe) {

    super(recipe);

    BlockMetaMatcher inputMatcher = recipe.getInputMatcher();
    Block block = inputMatcher.getBlock();

    int burnStages = recipe.getBurnStages();

    this.inputs = Collections.singletonList(Collections.singletonList(new ItemStack(Item.getItemFromBlock(block))));

    this.outputs = new ArrayList<>();
    ItemStack recipeOutput = recipe.getOutput();
    recipeOutput.setCount(burnStages);
    this.outputs.add(Collections.singletonList(recipeOutput));
    this.outputs.add(Arrays.asList(recipe.getFailureItems()));

    FluidStack fluidProduced = recipe.getFluidProduced();

    if (fluidProduced != null) {
      fluidProduced = fluidProduced.copy();
      fluidProduced.amount *= burnStages;
    }

    this.fluidStack = fluidProduced;

    this.failureChance = Util.translateFormatted(
        "gui." + ModuleTechRefractory.MOD_ID + ".jei.failure",
        (int) (recipe.getFailureChance() * 100)
    );
  }

  public FluidStack getFluidStack() {

    return this.fluidStack;
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutputLists(VanillaTypes.ITEM, this.outputs);

    if (this.fluidStack != null) {
      ingredients.setOutput(VanillaTypes.FLUID, this.fluidStack);
    }
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);

    int stringWidth = minecraft.fontRenderer.getStringWidth(this.failureChance);
    minecraft.fontRenderer.drawString(this.failureChance, recipeWidth - stringWidth, 44, Color.DARK_GRAY.getRGB());
  }
}
