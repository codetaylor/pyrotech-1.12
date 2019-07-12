package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalMulchSpreader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MechanicalMulchSpreaderProviderDelegate
    extends ProviderDelegateBase<MechanicalMulchSpreaderProviderDelegate.IMechanicalMulchSpreaderDisplay, TileMechanicalMulchSpreader> {

  public MechanicalMulchSpreaderProviderDelegate(IMechanicalMulchSpreaderDisplay display) {

    super(display);
  }

  @Override
  public void display(TileMechanicalMulchSpreader tile) {

    TileMechanicalMulchSpreader.MulchStackHandler mulchStackHandler = tile.getMulchStackHandler();
    ItemStack mulchStack = mulchStackHandler.getStackInSlot(0);

    ItemStackHandler cogStackHandler = tile.getCogStackHandler();
    ItemStack cog = cogStackHandler.getStackInSlot(0);

    this.display.setInput(mulchStack, cog);

    if (!cog.isEmpty()) {
      this.display.setCogName("gui." + ModuleTechMachine.MOD_ID + ".waila.cog", cog);
    }
  }

  public interface IMechanicalMulchSpreaderDisplay {

    void setInput(ItemStack mulchStack, ItemStack cog);

    void setCogName(String langKey, ItemStack cog);
  }
}
