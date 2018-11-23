package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITileDataContainer {

  TileDataTracker getTileDataManager();

  @SideOnly(Side.CLIENT)
  void onTileDataUpdate(ITileData data);
}
