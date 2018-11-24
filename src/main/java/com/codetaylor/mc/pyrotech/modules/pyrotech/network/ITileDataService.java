package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import javax.annotation.Nullable;

public interface ITileDataService {

  int getServiceId();

  @Nullable
  TileDataTracker getTracker(TileDataContainerBase tile);

  void register(TileDataContainerBase tile, ITileData[] data);

  void update();
}
