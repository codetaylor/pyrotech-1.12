package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloomery;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BloomeryProviderDelegate
    extends ProviderDelegateBase<BloomeryProviderDelegate.IBloomeryDisplay, TileBloomery> {

  public BloomeryProviderDelegate(IBloomeryDisplay display) {

    super(display);
  }

  @Override
  public void display(TileBloomery tile) {

    float progress = tile.getRecipeProgress();

    ItemStackHandler stackHandler = tile.getInputStackHandler();
    ItemStackHandler outputStackHandler = tile.getOutputStackHandler();
    TileBloomery.FuelStackHandler fuelStackHandler = tile.getFuelStackHandler();

    ItemStack input = stackHandler.getStackInSlot(0);
    boolean hasOutput = !outputStackHandler.getStackInSlot(0).isEmpty();
    int fuelCount = fuelStackHandler.getTotalItemCount();

    if (!input.isEmpty()) {

      // Display input item and recipe output.

      BloomeryRecipeBase<?> recipe = tile.getCurrentRecipe();

      if (recipe != null) {
        ItemStack recipeOutput = recipe.getOutputBloom();
        this.display.setRecipeProgress(input, recipeOutput, (int) (100 * progress), 100);

      } else {
        this.display.setInput(input);
      }

    } else if (hasOutput) {

      // Display output items.

      this.display.setOutputItems(outputStackHandler);
    }

    if (fuelCount > 0) {
      this.display.setFuelItems(fuelStackHandler);
    }

    this.display.setSpeed(
        "gui." + ModuleTechBloomery.MOD_ID + ".waila.speed",
        (int) (tile.getSpeed() * 100)
    );

    this.display.setAirflow(
        "gui." + ModuleTechBloomery.MOD_ID + ".waila.bloomery.airflow",
        (int) (tile.getAirflow() * 100)
    );

    this.display.setFuelCount(
        "gui." + ModuleTechBloomery.MOD_ID + ".waila.bloomery.fuel",
        tile.getFuelCount(),
        tile.getMaxFuelCount()
    );

  }

  public interface IBloomeryDisplay {

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);

    void setInput(ItemStack input);

    void setOutputItems(ItemStackHandler stackHandler);

    void setFuelItems(ItemStackHandler stackHandler);

    void setSpeed(String langKey, int speed);

    void setAirflow(String langKey, int airflow);

    void setFuelCount(String langKey, int fuelCount, int maxFuelCount);
  }

}
