package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBinWorker;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MechanicalCompactingBinWorkerProviderDelegate
    extends ProviderDelegateBase<MechanicalCompactingBinWorkerProviderDelegate.IMechanicalCompactingBinWorkerDisplay, TileMechanicalCompactingBinWorker> {

  public MechanicalCompactingBinWorkerProviderDelegate(IMechanicalCompactingBinWorkerDisplay display) {

    super(display);
  }

  @Override
  public void display(TileMechanicalCompactingBinWorker tile) {

    ItemStackHandler cogStackHandler = tile.getCogStackHandler();
    ItemStack cog = cogStackHandler.getStackInSlot(0);

    TileMechanicalCompactingBinWorker.OutputStackHandler outputStackHandler = tile.getOutputStackHandler();
    ItemStack stackInSlot = outputStackHandler.getStackInSlot(0);

    this.display.setOutput(cog, stackInSlot);

    if (!cog.isEmpty()) {
      this.display.setCogName("gui." + ModuleTechMachine.MOD_ID + ".waila.cog", cog);
    }
  }

  public interface IMechanicalCompactingBinWorkerDisplay {

    void setOutput(ItemStack cog, ItemStack output);

    void setCogName(String langKey, ItemStack cog);
  }
}
