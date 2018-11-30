package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

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
import java.util.List;

/**
 * This provides a default implementation of the packet update methods.
 * <p>
 * <p>
 * Call {@link TileNetworkedBase#registerTileData(ITileData[])}
 * in the subclass' constructor to register tile data.
 */
public abstract class TileNetworkedBase
    extends TileDataContainerBase {

  protected final ITileDataService tileDataService;

  protected TileNetworkedBase(ITileDataService tileDataService) {

    this.tileDataService = tileDataService;
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  protected void registerTileData(ITileData[] data) {

    this.tileDataService.register(this, data);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onTileDataUpdate(List<ITileData> data) {
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
