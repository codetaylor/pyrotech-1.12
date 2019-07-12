package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CogWorkerProviderDelegate
    extends ProviderDelegateBase<CogWorkerProviderDelegate.ICogWorkerDisplay, TileCogWorkerBase> {

  public CogWorkerProviderDelegate(ICogWorkerDisplay display) {

    super(display);
  }

  @Override
  public void display(TileCogWorkerBase tile) {

    ItemStackHandler cogStackHandler = tile.getCogStackHandler();
    ItemStack cog = cogStackHandler.getStackInSlot(0);

    if (!cog.isEmpty()) {
      this.display.setCog(cog);
      String langKey = "gui." + ModuleTechMachine.MOD_ID + ".waila.cog";
      this.display.setCogName(langKey, cog);
    }
  }

  public interface ICogWorkerDisplay {

    void setCog(ItemStack cog);

    void setCogName(String langKey, ItemStack cog);
  }
}
