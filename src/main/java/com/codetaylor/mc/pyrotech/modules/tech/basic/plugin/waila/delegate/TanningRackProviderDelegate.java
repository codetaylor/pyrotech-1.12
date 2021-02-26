package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.TanningRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileTanningRack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TanningRackProviderDelegate
    extends ProviderDelegateBase<TanningRackProviderDelegate.ITanningRackDisplay, TileTanningRack> {

  public TanningRackProviderDelegate(ITanningRackDisplay display) {

    super(display);
  }

  @Override
  public void display(TileTanningRack tile) {

    ItemStackHandler stackHandler = tile.getInputStackHandler();
    ItemStackHandler outputStackHandler = tile.getOutputStackHandler();

    for (int i = 0; i < stackHandler.getSlots(); i++) {

      ItemStack inputStack = stackHandler.getStackInSlot(i);
      float progress = tile.getRecipeProgress();

      if (!inputStack.isEmpty()) {

        // Display input item and recipe output.

        TanningRackRecipe recipe = tile.getCurrentRecipe();

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutput();
          recipeOutput.setCount(inputStack.getCount());
          this.display.setRecipeProgress(inputStack, recipeOutput, (int) (100 * progress), 100);

        } else {
          this.display.setRecipeProgress(inputStack, ItemStack.EMPTY, 0, 100);
        }
      }
    }

    this.display.setOutputItems(outputStackHandler);
  }

  public interface ITanningRackDisplay {

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);

    void setOutputItems(ItemStackHandler outputStackHandler);
  }
}
