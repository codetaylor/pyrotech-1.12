package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInItemOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneSawmill;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneSawmillTop;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class SawmillProvider
    extends BodyProviderAdapter {

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileStoneSawmill
        || tileEntity instanceof TileStoneSawmillTop) {

      TileStoneSawmill tile = null;

      if (tileEntity instanceof TileStoneSawmill) {
        tile = (TileStoneSawmill) tileEntity;

      } else {
        World world = tileEntity.getWorld();
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileStoneSawmill) {
          tile = (TileStoneSawmill) candidate;
        }
      }

      if (tile == null) {
        return tooltip;
      }

      float progress = tile.workerGetProgress(0);

      ItemStackHandler stackHandler = tile.getInputStackHandler();
      ItemStackHandler outputStackHandler = tile.getOutputStackHandler();
      ItemStackHandler fuelStackHandler = tile.getFuelStackHandler();
      ItemStackHandler bladeStackHandler = tile.getBladeStackHandler();

      ItemStack input = stackHandler.getStackInSlot(0);
      boolean hasOutput = !outputStackHandler.getStackInSlot(0).isEmpty();
      ItemStack fuel = fuelStackHandler.getStackInSlot(0);
      ItemStack blade = bladeStackHandler.getStackInSlot(0);

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        StringBuilder renderString = new StringBuilder();
        renderString.append(WailaUtil.getStackRenderString(input));

        if (!blade.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(blade));
        }

        if (!fuel.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(fuel));
        }

        StoneMachineRecipeItemInItemOutBase recipe = tile.getRecipe(input);

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutput();
          recipeOutput.setCount(recipeOutput.getCount() * input.getCount());
          renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
          renderString.append(WailaUtil.getStackRenderString(recipeOutput));
        }

        tooltip.add(renderString.toString());

      } else if (hasOutput) {

        // Display output items.

        StringBuilder renderString = new StringBuilder();

        if (!blade.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(blade));
        }

        for (int i = 0; i < outputStackHandler.getSlots(); i++) {
          ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            renderString.append(WailaUtil.getStackRenderString(stackInSlot));
          }
        }

        tooltip.add(renderString.toString());

      } else {

        if (!blade.isEmpty()) {
          tooltip.add(WailaUtil.getStackRenderString(blade));
        }
      }

      {
        if (!blade.isEmpty()) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModulePyrotech.MOD_ID + ".waila.mill.stone.blade",
              blade.getItem().getItemStackDisplayName(blade)
          ));
        }

        if (tile.combustionGetBurnTimeRemaining() > 0) {
          ItemStack fuelStack = tile.getFuelStackHandler().getStackInSlot(0);
          tooltip.add(Util.translateFormatted(
              "gui." + ModulePyrotech.MOD_ID + ".waila.burn.time",
              StringHelper.ticksToHMS(tile.combustionGetBurnTimeRemaining() + fuelStack.getCount() * StackHelper.getItemBurnTime(fuelStack))
          ));
        }

        if (!fuel.isEmpty()) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModulePyrotech.MOD_ID + ".waila.kiln.brick.fuel",
              fuel.getItem().getItemStackDisplayName(fuel) + " * " + fuel.getCount()
          ));
        }
      }

    }

    return tooltip;
  }
}
