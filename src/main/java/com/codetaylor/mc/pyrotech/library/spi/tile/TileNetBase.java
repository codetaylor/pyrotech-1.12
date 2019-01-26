package com.codetaylor.mc.pyrotech.library.spi.tile;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileDataContainerBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This provides a default implementation of the packet update methods.
 * <p>
 * <p>
 * Call {@link TileNetBase#registerTileDataForNetwork(ITileData[])}
 * in the subclass' constructor to register tile data.
 */
public abstract class TileNetBase
    extends TileDataContainerBase {

  protected final ITileDataService tileDataService;

  protected TileNetBase(ITileDataService tileDataService) {

    this.tileDataService = tileDataService;
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  protected void registerTileDataForNetwork(ITileData[] data) {

    this.tileDataService.register(this, data);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onTileDataUpdate() {
    //
  }

  @Nonnull
  @Override
  public NBTTagCompound getUpdateTag() {

    return this.writeToNBT(new NBTTagCompound());
  }

  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {

    this.readFromNBT(packet.getNbtCompound());
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }
}
