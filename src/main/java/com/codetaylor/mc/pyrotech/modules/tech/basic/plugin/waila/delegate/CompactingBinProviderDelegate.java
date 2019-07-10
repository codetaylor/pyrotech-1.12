package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.CompactingBinRecipeBase;
import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompactingBin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CompactingBinProviderDelegate
    extends ProviderDelegateBase<CompactingBinProviderDelegate.ICompactingBinDisplay, TileCompactingBin> {

  public CompactingBinProviderDelegate(ICompactingBinDisplay display) {

    super(display);
  }

  @Override
  public void display(TileCompactingBin tile) {

    CompactingBinRecipeBase currentRecipe = tile.getCurrentRecipe();

    if (currentRecipe == null) {
      return;
    }

    float progress = tile.getRecipeProgress();
    TileCompactingBin.InputStackHandler inputStackHandler = tile.getInputStackHandler();
    int totalItemCount = tile.getInputStackHandler().getTotalItemCount();
    int completeRecipeCount = totalItemCount / currentRecipe.getAmount();

    if (totalItemCount > 0) {

      if (completeRecipeCount == 0) {
        this.display.setRecipeInput(inputStackHandler);

      } else {
        ItemStack output = currentRecipe.getOutput();
        output.setCount(completeRecipeCount);
        this.display.setRecipeProgress(inputStackHandler, output, (int) (100 * progress), 100);
      }
    }
  }

  public interface ICompactingBinDisplay {

    void setRecipeInput(ItemStackHandler inputStackHandler);

    void setRecipeProgress(ItemStackHandler inputStackHandler, ItemStack output, int progress, int maxProgress);
  }
}
