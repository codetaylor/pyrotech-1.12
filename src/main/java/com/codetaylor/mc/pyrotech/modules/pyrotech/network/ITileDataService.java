package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ITileDataService {

  int getServiceId();

  @Nullable
  TileDataTracker getTracker(TileDataContainerBase tile);

  void register(TileDataContainerBase tile);

  abstract void update();
}
