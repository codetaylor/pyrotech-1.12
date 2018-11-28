package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.IRecipeTimed;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import java.awt.*;

public abstract class JEIRecipeWrapperTimed
    implements IRecipeWrapper {

  protected final String timeString;

  public JEIRecipeWrapperTimed(IRecipeTimed recipe) {

    this.timeString = StringHelper.ticksToHMS(recipe.getTimeTicks());
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    int stringWidth = minecraft.fontRenderer.getStringWidth(this.timeString);
    minecraft.fontRenderer.drawString(this.timeString, recipeWidth - stringWidth, 0, Color.GRAY.getRGB());
  }
}
