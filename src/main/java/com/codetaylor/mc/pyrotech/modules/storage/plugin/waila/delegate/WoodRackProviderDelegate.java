package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileWoodRack;
import net.minecraftforge.items.ItemStackHandler;

public class WoodRackProviderDelegate
    extends ProviderDelegateBase<WoodRackProviderDelegate.IWoodRackDisplay, TileWoodRack> {

  public WoodRackProviderDelegate(IWoodRackDisplay display) {

    super(display);
  }

  @Override
  public void display(TileWoodRack tile) {

    ItemStackHandler stackHandler = tile.getStackHandler();
    this.display.setContents(stackHandler);
  }

  public interface IWoodRackDisplay {

    void setContents(ItemStackHandler stackHandler);
  }
}
