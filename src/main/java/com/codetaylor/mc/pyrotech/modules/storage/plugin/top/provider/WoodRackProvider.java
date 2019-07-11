package com.codetaylor.mc.pyrotech.modules.storage.plugin.top.provider;

import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate.WoodRackProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileWoodRack;
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
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class WoodRackProvider
    implements IProbeInfoProvider,
    WoodRackProviderDelegate.IWoodRackDisplay {

  private final WoodRackProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public WoodRackProvider() {

    this.delegate = new WoodRackProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileWoodRack) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileWoodRack) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setContents(ItemStackHandler stackHandler) {

    boolean found = false;

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack itemStack = stackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        found = true;
        break;
      }
    }

    if (found) {

      IProbeInfo horizontal = this.probeInfo.horizontal();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        ItemStack itemStack = stackHandler.getStackInSlot(i);

        if (!itemStack.isEmpty()) {
          horizontal.item(itemStack);
        }
      }
    }
  }
}
