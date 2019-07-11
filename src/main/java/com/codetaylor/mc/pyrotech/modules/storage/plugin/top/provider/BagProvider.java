package com.codetaylor.mc.pyrotech.modules.storage.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate.BagProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class BagProvider
    implements IProbeInfoProvider,
    BagProviderDelegate.IBagDisplay {

  private final BagProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public BagProvider() {

    this.delegate = new BagProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileBagBase) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileBagBase) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setContents(ItemStackHandler stackHandler) {

    boolean found = false;

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack stackInSlot = stackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        found = true;
        break;
      }
    }

    if (found) {

      IProbeInfo horizontal = this.probeInfo.horizontal();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        ItemStack stackInSlot = stackHandler.getStackInSlot(i);

        if (!stackInSlot.isEmpty()) {
          horizontal.item(stackInSlot);
        }
      }
    }
  }

  @Override
  public void setItemCount(String langKey, int itemCount, int itemCapacity) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, itemCount, itemCapacity));
  }

  @Override
  public void setExtendedInfoOff(String langKey, TextFormatting startColor, TextFormatting endColor) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, startColor.toString(), endColor.toString()));
  }

  @Override
  public void setExtendedInfoOn(String count, TextFormatting formatting, ItemStack itemStack) {

    this.probeInfo.element(new PluginTOP.ElementItemLabel(count + formatting.toString(), itemStack));
  }
}
