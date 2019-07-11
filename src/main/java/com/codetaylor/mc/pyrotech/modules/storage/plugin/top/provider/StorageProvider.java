package com.codetaylor.mc.pyrotech.modules.storage.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate.StorageProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileCrate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileShelf;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileStash;
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

public class StorageProvider
    implements IProbeInfoProvider,
    StorageProviderDelegate.IStorageDisplay {

  private final StorageProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public StorageProvider() {

    this.delegate = new StorageProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleStorage.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCrate
        || tileEntity instanceof TileShelf
        || tileEntity instanceof TileStash) {
      this.probeInfo = probeInfo;
      this.delegate.display(tileEntity, player);
      this.probeInfo = null;
    }
  }

  @Override
  public void setItem(ItemStack itemStack) {

    this.probeInfo.item(itemStack);
  }

  @Override
  public void setItemLabel(ItemStack itemStack) {

    this.probeInfo.itemLabel(itemStack);
  }
}
