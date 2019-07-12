package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CombustionMachineItemInFluidOutProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInFluidOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCapabilityDelegateMachineTop;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInFluidOutBase;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class CombustionMachineItemInFluidOutProvider
    implements IProbeInfoProvider,
    CombustionMachineItemInFluidOutProviderDelegate.ICombustionMachineItemInFluidOutDisplay {

  private final CombustionMachineItemInFluidOutProviderDelegate delegate;

  private IProbeInfo probeInfo;

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

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCombustionWorkerStoneItemInFluidOutBase
        || tileEntity instanceof TileCapabilityDelegateMachineTop) {

      TileCombustionWorkerStoneItemInFluidOutBase tile = null;

      if (tileEntity instanceof TileCombustionWorkerStoneItemInFluidOutBase) {
        tile = (TileCombustionWorkerStoneItemInFluidOutBase) tileEntity;

      } else {
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileCombustionWorkerStoneItemInFluidOutBase) {
          tile = (TileCombustionWorkerStoneItemInFluidOutBase) candidate;
        }
      }

      if (tile == null) {
        return;
      }

      this.probeInfo = probeInfo;
      this.delegate.display(tile);
      this.probeInfo = null;
    }
  }

  @Override
  public void setBurnTime(@Nullable TextFormatting formatting, String langKey, String burnTimeString) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(formatting, langKey, burnTimeString));
  }

  @Override
  public void setFuel(String langKey, ItemStack fuel, String count) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, fuel, count));
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack fuel, MachineRecipeItemInFluidOutBase recipe, int progress, int maxProgress) {

    if (input.isEmpty() && fuel.isEmpty()) {
      return;
    }

    IProbeInfo horizontal = this.probeInfo.horizontal();
    horizontal.item(input);

    if (!fuel.isEmpty()) {
      horizontal.item(fuel);
    }

    if (recipe != null) {
      FluidStack output = recipe.getOutput();
      ItemStack filledBucket = FluidUtil.getFilledBucket(output);
      horizontal.progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false));
      horizontal.item(filledBucket);
    }
  }

  @Override
  public void setSynchronous(String langKey, String recipeTime, String totalRecipeTime) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, recipeTime, totalRecipeTime));
  }

  @Override
  public void setAsynchronous(String langKey, String recipeTime) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, recipeTime));
  }

  @Override
  public void setFluidTank(String langKey, FluidStack fluid, int amount, int capacity) {

    this.probeInfo.element(new PluginTOP.ElementTankLabel(null, fluid, capacity));
  }

  @Override
  public void setFluidTankEmpty(String langKey, int capacity) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, 0, capacity));
  }
}
