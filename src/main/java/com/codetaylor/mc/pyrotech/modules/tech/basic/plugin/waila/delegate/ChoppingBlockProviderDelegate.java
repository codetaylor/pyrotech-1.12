package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.ChoppingBlockRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileChoppingBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ChoppingBlockProviderDelegate
    extends ProviderDelegateBase<ChoppingBlockProviderDelegate.IChoppingBlockDisplay, TileChoppingBlock> {

  public ChoppingBlockProviderDelegate(IChoppingBlockDisplay display) {

    super(display);
  }

  @Override
  public void display(TileChoppingBlock tile) {

    float progress = tile.getRecipeProgress();
    ItemStackHandler stackHandler = tile.getStackHandler();
    ItemStack input = stackHandler.getStackInSlot(0);

    if (!input.isEmpty()) {

      // Display input item and recipe output.

      ChoppingBlockRecipe recipe = ChoppingBlockRecipe.getRecipe(input);

      if (recipe == null) {
        this.display.setRecipeInput(input);

      } else {
        ItemStack recipeOutput = recipe.getOutput();

        if (!recipeOutput.isEmpty()) {
          recipeOutput.setCount(input.getCount());
          this.display.setRecipeProgress(input, recipeOutput, (int) (100 * progress), 100);
        }
      }
    }
  }

  public interface IChoppingBlockDisplay {

    void setRecipeInput(ItemStack input);

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);
  }
}
