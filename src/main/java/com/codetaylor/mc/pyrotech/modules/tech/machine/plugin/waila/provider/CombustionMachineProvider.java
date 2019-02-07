package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CombustionMachineProvider<T extends TileCombustionWorkerStoneBase, R extends StoneMachineRecipeBase>
    extends BodyProviderAdapter {

  protected void addBurnTimeInfo(List<String> tooltip, T tile, float progress, ItemStack input, ItemStack fuel, @Nullable R recipe) {

    ItemStack fuelStack = tile.getFuelStackHandler().getStackInSlot(0);
    int ticks = tile.combustionGetBurnTimeRemaining();

    if (!fuelStack.isEmpty()) {
      ticks += fuelStack.getCount() * StackHelper.getItemBurnTime(fuelStack);
    }

    if (recipe != null) {
      float recipeTimeTicks = recipe.getTimeTicks() * (1 - progress);

      recipeTimeTicks = this.getModifiedRecipeTimeTicks(recipeTimeTicks, tile, input, recipe);

      if (ticks < recipeTimeTicks) {
        tooltip.add(TextFormatting.RED + Util.translateFormatted(
            "gui." + ModuleTechMachine.MOD_ID + ".waila.burn.time",
            StringHelper.ticksToHMS(ticks)
        ));

      } else {
        tooltip.add(TextFormatting.GREEN + Util.translateFormatted(
            "gui." + ModuleTechMachine.MOD_ID + ".waila.burn.time",
            StringHelper.ticksToHMS(ticks)
        ));
      }

    } else {
      tooltip.add(Util.translateFormatted(
          "gui." + ModuleTechMachine.MOD_ID + ".waila.burn.time",
          StringHelper.ticksToHMS(ticks)
      ));
    }

    if (!fuel.isEmpty()) {
      tooltip.add(Util.translateFormatted(
          "gui." + ModuleTechMachine.MOD_ID + ".waila.fuel",
          fuel.getItem().getItemStackDisplayName(fuel) + " * " + fuel.getCount()
      ));
    }
  }

  protected float getModifiedRecipeTimeTicks(float recipeTimeTicks, T tile, ItemStack input, R recipe) {

    return recipeTimeTicks;
  }
}
