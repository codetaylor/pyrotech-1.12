package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CampfireRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CampfireProviderDelegate
    extends BodyProviderAdapter {

  private final ICampfireDisplay display;

  public CampfireProviderDelegate(ICampfireDisplay display) {

    this.display = display;
  }

  public void display(TileCampfire tileCampfire) {

    float progress = tileCampfire.workerGetProgress(0);

    ItemStackHandler stackHandler = tileCampfire.getInputStackHandler();
    ItemStackHandler outputStackHandler = tileCampfire.getOutputStackHandler();

    ItemStack input = stackHandler.getStackInSlot(0);
    boolean hasOutput = !outputStackHandler.getStackInSlot(0).isEmpty();

    if (!input.isEmpty()) {

      // Display input item and recipe output.

      CampfireRecipe recipe = CampfireRecipe.getRecipe(input);

      if (recipe == null) {
        this.display.setRecipeInput(input);

      } else {
        ItemStack recipeOutput = recipe.getOutput();
        recipeOutput.setCount(input.getCount());
        this.display.setRecipeProgress(input, recipeOutput, (int) (100 * progress), 100);
      }

    } else if (hasOutput) {

      // Display output items.

      ItemStack output = outputStackHandler.getStackInSlot(0);
      this.display.setRecipeOutput(output);
    }

    {
      int fuelRemaining = tileCampfire.getFuelRemaining();

      if (tileCampfire.workerIsActive()
          && tileCampfire.combustionGetBurnTimeRemaining() > 0) {
        String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.burn.time";
        String burnTime = StringHelper.ticksToHMS(tileCampfire.combustionGetBurnTimeRemaining() + tileCampfire.getFuelRemaining() * ModuleTechBasicConfig.CAMPFIRE.BURN_TIME_TICKS_PER_LOG);
        this.display.setBurnTime(langKey, burnTime);
      }

      if (fuelRemaining > 0) {
        String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.campfire.fuel";
        this.display.setFuelRemaining(langKey, fuelRemaining, 8);
      }

      if (tileCampfire.getAshLevel() > 0) {
        String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.ash";
        int ashLevel = tileCampfire.getAshLevel();
        this.display.setAshLevel(langKey, ashLevel, 8);
      }
    }
  }

  public interface ICampfireDisplay {

    void setRecipeInput(ItemStack input);

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);

    void setRecipeOutput(ItemStack output);

    void setBurnTime(String langKey, String burnTime);

    void setFuelRemaining(String langKey, int fuelRemaining, int maxFuel);

    void setAshLevel(String langKey, int ashLevel, int maxAshLevel);
  }
}
