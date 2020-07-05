package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.CompostBinProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompostBin;
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

public class CompostBinProvider
    implements IProbeInfoProvider,
    CompostBinProviderDelegate.ICompostBinDisplay {

  private final CompostBinProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public CompostBinProvider() {

    this.delegate = new CompostBinProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCompostBin) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileCompostBin) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeProgress(ItemStack output, int progress, int maxProgress) {

    if (progress > 0) {

      IProbeInfo horizontal = this.probeInfo.horizontal();
      ProgressStyle progressStyle = new ProgressStyle().height(18).width(64).showText(false);

      horizontal.item(new ItemStack(ModuleTechBasic.Blocks.COMPOST_BIN));
      horizontal.progress(progress, maxProgress, progressStyle);
      horizontal.item(output);
    }
  }

  @Override
  public void setMoistureLevel(@Nullable TextFormatting formatting, String langKey, int moistureLevel) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(formatting, langKey, moistureLevel));
  }

  @Override
  public void setContents(ItemStackHandler inputStackHandler, ItemStack storedCompostValue, ItemStack outputStack) {

    IProbeInfo horizontal = this.probeInfo.horizontal();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        horizontal.item(stackInSlot);
      }
    }

    if (!storedCompostValue.isEmpty()) {
      horizontal.item(storedCompostValue);
    }

    if (!outputStack.isEmpty()) {
      horizontal.item(outputStack);
    }
  }
}
