package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeBaseKiln;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.wrapper.JEIRecipeWrapperTimed;
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

public class JEIRecipeWrapperStoneKiln
    extends JEIRecipeWrapperTimed {

  private final List<List<ItemStack>> inputs;
  private final List<List<ItemStack>> outputs;
  private final String failureChance;

  public JEIRecipeWrapperStoneKiln(MachineRecipeBaseKiln recipe) {

    super(recipe);

    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));

    this.outputs = new ArrayList<>();
    this.outputs.add(Collections.singletonList(recipe.getOutput()));
    this.outputs.add(Arrays.asList(recipe.getFailureItems()));

    this.failureChance = Util.translateFormatted(
        "gui." + ModuleTechMachine.MOD_ID + ".jei.failure",
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
    minecraft.fontRenderer.drawString(this.failureChance, recipeWidth - stringWidth, 36, Color.DARK_GRAY.getRGB());
  }

  @Override
  protected int getTimeDisplayY() {

    return 24;
  }
}
