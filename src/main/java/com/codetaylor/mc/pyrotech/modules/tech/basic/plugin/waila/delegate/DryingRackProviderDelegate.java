package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileDryingRackBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class DryingRackProviderDelegate
    extends ProviderDelegateBase<DryingRackProviderDelegate.IDryingRackDisplay, TileDryingRackBase> {

  public DryingRackProviderDelegate(IDryingRackDisplay display) {

    super(display);
  }

  @Override
  public void display(TileDryingRackBase tile) {

    {
      String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.speed";
      int speed = (int) (tile.getSpeed() * 100);
      this.display.setSpeed(langKey, speed);
    }

    ItemStackHandler stackHandler = tile.getInputStackHandler();
    ItemStackHandler outputStackHandler = tile.getOutputStackHandler();

    for (int i = 0; i < stackHandler.getSlots(); i++) {

      ItemStack inputStack = stackHandler.getStackInSlot(i);
      float progress = tile.workerGetProgress(i);

      if (!inputStack.isEmpty()) {

        // Display input item and recipe output.

        DryingRackRecipe recipe = DryingRackRecipe.getRecipe(inputStack);

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutput();
          recipeOutput.setCount(inputStack.getCount());
          this.display.setRecipeProgress(inputStack, recipeOutput, (int) (100 * progress), 100);
        }
      }
    }

    this.display.setOutputItems(outputStackHandler);
  }

  public interface IDryingRackDisplay {

    void setSpeed(String langKey, int speed);

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);

    void setOutputItems(ItemStackHandler outputStackHandler);
  }
}
