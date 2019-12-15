package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.CompactingBinRecipeBase;
import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBin;
import net.minecraftforge.items.ItemStackHandler;

public class MechanicalCompactingBinProviderDelegate
    extends ProviderDelegateBase<MechanicalCompactingBinProviderDelegate.IMechanicalCompactingBinDisplay, TileMechanicalCompactingBin> {

  public MechanicalCompactingBinProviderDelegate(IMechanicalCompactingBinDisplay display) {

    super(display);
  }

  @Override
  public void display(TileMechanicalCompactingBin tile) {

    CompactingBinRecipeBase currentRecipe = tile.getCurrentRecipe();

    if (currentRecipe == null) {
      return;
    }

    float progress = tile.getRecipeProgress();
    TileMechanicalCompactingBin.InputStackHandler inputStackHandler = tile.getInputStackHandler();
    int totalItemCount = tile.getInputStackHandler().getTotalItemCount();
    int completeRecipeCount = totalItemCount / currentRecipe.getAmount();

    if (totalItemCount > 0) {
      this.display.setRecipeProgress(inputStackHandler, currentRecipe, completeRecipeCount, (int) (100 * progress), 100);
    }
  }

  public interface IMechanicalCompactingBinDisplay {

    void setRecipeProgress(ItemStackHandler inputStackHandler, CompactingBinRecipeBase currentRecipe, int completeRecipeCount, int progress, int maxProgress);
  }
}
