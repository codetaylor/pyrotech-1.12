package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CombustionMachineItemInItemOutProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInItemOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCapabilityDelegateMachineTop;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileSawmillBase;
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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class SawmillProvider
    implements IProbeInfoProvider,
    CombustionMachineItemInItemOutProviderDelegate.ICombustionMachineItemInItemOutDisplay {

  private final CombustionMachineItemInItemOutProviderDelegate delegate;

  private TileSawmillBase tile;
  private IProbeInfo probeInfo;

  public SawmillProvider() {

    this.delegate = new CombustionMachineItemInItemOutProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileSawmillBase
        || tileEntity instanceof TileCapabilityDelegateMachineTop) {

      TileSawmillBase tile = null;

      if (tileEntity instanceof TileSawmillBase) {
        tile = (TileSawmillBase) tileEntity;

      } else {
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileSawmillBase) {
          tile = (TileSawmillBase) candidate;
        }
      }

      if (tile == null) {
        return;
      }

      this.tile = tile;
      this.probeInfo = probeInfo;
      this.delegate.display(tile);
      this.tile = null;
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack fuel, MachineRecipeItemInItemOutBase recipe, int progress, int maxProgress) {

    ItemStackHandler bladeStackHandler = this.tile.getBladeStackHandler();
    ItemStack blade = bladeStackHandler.getStackInSlot(0);

    if (input.isEmpty() && fuel.isEmpty() && blade.isEmpty()) {
      return;
    }

    IProbeInfo horizontal = this.probeInfo.horizontal();

    horizontal.item(input);

    if (!blade.isEmpty()) {
      horizontal.item(blade);
    }

    if (!fuel.isEmpty()) {
      horizontal.item(fuel);
    }

    if (recipe != null) {
      ItemStack recipeOutput = recipe.getOutput();
      recipeOutput.setCount(input.getCount());
      horizontal.progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false));
      horizontal.item(recipeOutput);
    }
  }

  @Override
  public void setRecipeDuration(String langKey, String duration) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, duration));
  }

  @Override
  public void setOutputItems(ItemStackHandler outputStackHandler) {

    ItemStackHandler bladeStackHandler = this.tile.getBladeStackHandler();
    ItemStack blade = bladeStackHandler.getStackInSlot(0);

    IProbeInfo horizontal = this.probeInfo.horizontal();

    if (!blade.isEmpty()) {
      horizontal.item(blade);
    }

    for (int i = 0; i < outputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        horizontal.item(stackInSlot);
      }
    }
  }

  @Override
  public void optionalNoInputNoOutput() {

    ItemStackHandler bladeStackHandler = this.tile.getBladeStackHandler();
    ItemStack blade = bladeStackHandler.getStackInSlot(0);

    if (!blade.isEmpty()) {
      this.probeInfo.item(blade);
    }
  }

  @Override
  public void optionalPreBurnTimeInfo() {

    ItemStackHandler bladeStackHandler = this.tile.getBladeStackHandler();
    ItemStack blade = bladeStackHandler.getStackInSlot(0);

    if (!blade.isEmpty()) {
      this.probeInfo.element(new PluginTOP.ElementTextLocalized(
          "gui." + ModuleTechMachine.MOD_ID + ".waila.sawmill.blade",
          blade
      ));
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
}
