package com.codetaylor.mc.pyrotech.api;

import com.codetaylor.mc.pyrotech.internal.IPyrotechAPI;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class PyrotechAPI_Internal
    implements IPyrotechAPI {

  public final List<String> HAMMERS = new ArrayList<>(1);

  @Override
  public void registerHammer(Item item, int harvestLevel) {

    HAMMERS.add(item.getRegistryName() + ";" + harvestLevel);
  }
}
