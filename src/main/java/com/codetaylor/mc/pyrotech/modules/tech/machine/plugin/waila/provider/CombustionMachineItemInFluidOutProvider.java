package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CombustionMachineItemInFluidOutProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CombustionMachineProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInFluidOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCapabilityDelegateMachineTop;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInFluidOutBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CombustionMachineItemInFluidOutProvider
    extends BodyProviderAdapter
    implements CombustionMachineItemInFluidOutProviderDelegate.ICombustionMachineItemInFluidOutDisplay {

  private final CombustionMachineProviderDelegateBase<CombustionMachineItemInFluidOutProviderDelegate.ICombustionMachineItemInFluidOutDisplay, TileCombustionWorkerStoneItemInFluidOutBase, MachineRecipeItemInFluidOutBase> delegate;

  private List<String> tooltip;

  public CombustionMachineItemInFluidOutProvider() {

    this.delegate = new CombustionMachineItemInFluidOutProviderDelegate(this) {

      @Override
      public float getModifiedRecipeTimeTicks(float recipeTimeTicks, TileCombustionWorkerStoneItemInFluidOutBase tile, ItemStack input, MachineRecipeItemInFluidOutBase recipe) {

        if (!tile.processAsynchronous()
            && input.getCount() > 1) {

          recipeTimeTicks += recipe.getTimeTicks() * (input.getCount() - 1);
        }

        return recipeTimeTicks;
      }
    };
  }

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileCombustionWorkerStoneItemInFluidOutBase
        || tileEntity instanceof TileCapabilityDelegateMachineTop) {

      TileCombustionWorkerStoneItemInFluidOutBase tile = null;

      if (tileEntity instanceof TileCombustionWorkerStoneItemInFluidOutBase) {
        tile = (TileCombustionWorkerStoneItemInFluidOutBase) tileEntity;

      } else {
        World world = tileEntity.getWorld();
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileCombustionWorkerStoneItemInFluidOutBase) {
          tile = (TileCombustionWorkerStoneItemInFluidOutBase) candidate;
        }
      }

      if (tile == null) {
        return tooltip;
      }

      this.tooltip = tooltip;
      this.delegate.display(tile);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack fuel, MachineRecipeItemInFluidOutBase recipe, int progress, int maxProgress) {

    StringBuilder renderString = new StringBuilder();
    renderString.append(WailaUtil.getStackRenderString(input));

    if (!fuel.isEmpty()) {
      renderString.append(WailaUtil.getStackRenderString(fuel));
    }

    if (recipe != null) {
      renderString.append(WailaUtil.getProgressRenderString(progress, maxProgress));

      FluidStack outputFluid = recipe.getOutput();
      Block fluidBlock = outputFluid.getFluid().getBlock();

      if (fluidBlock instanceof BlockFluidBase) {
        renderString.append(WailaUtil.getStackRenderString(new ItemStack(outputFluid.getFluid().getBlock())));

      } else {
        renderString.append(WailaUtil.getStackRenderString(FluidUtil.getFilledBucket(outputFluid)));
      }
    }

    this.tooltip.add(renderString.toString());
  }

  @Override
  public void setSynchronous(String langKey, String recipeTime, String totalRecipeTime) {

    this.tooltip.add(Util.translateFormatted(langKey, recipeTime, totalRecipeTime));
  }

  @Override
  public void setAsynchronous(String langKey, String recipeTime) {

    this.tooltip.add(Util.translateFormatted(langKey, recipeTime));
  }

  @Override
  public void setFluidTank(String langKey, FluidStack fluid, int amount, int capacity) {

    this.tooltip.add(Util.translateFormatted(langKey, fluid.getLocalizedName(), amount, capacity));
  }

  @Override
  public void setFluidTankEmpty(String langKey, int capacity) {

    this.tooltip.add(Util.translateFormatted(langKey, 0, capacity));
  }

  @Override
  public void setBurnTime(@Nullable TextFormatting formatting, String langKey, String burnTimeString) {

    String formattingString = (formatting != null) ? formatting.toString() : "";
    this.tooltip.add(formattingString + Util.translateFormatted(langKey, burnTimeString));
  }

  @Override
  public void setFuel(String langKey, ItemStack fuel, String count) {

    this.tooltip.add(Util.translateFormatted(langKey, fuel.getDisplayName(), count));
  }
}
