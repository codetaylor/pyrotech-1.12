package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileTripHammer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TripHammerProviderDelegate
    extends ProviderDelegateBase<TripHammerProviderDelegate.ITripHammerSpreaderDisplay, TileTripHammer> {

  public TripHammerProviderDelegate(ITripHammerSpreaderDisplay display) {

    super(display);
  }

  @Override
  public void display(TileTripHammer tile) {

    ItemStackHandler toolStackHandler = tile.getToolStackHandler();
    ItemStack toolItemStack = toolStackHandler.getStackInSlot(0);

    ItemStackHandler cogStackHandler = tile.getCogStackHandler();
    ItemStack cog = cogStackHandler.getStackInSlot(0);

    this.display.setItems(toolItemStack, cog);

    if (!cog.isEmpty()) {
      this.display.setCogName("gui." + ModuleTechMachine.MOD_ID + ".waila.cog", cog);
    }
  }

  public interface ITripHammerSpreaderDisplay {

    void setItems(ItemStack toolItemStack, ItemStack cog);

    void setCogName(String langKey, ItemStack cog);
  }
}
