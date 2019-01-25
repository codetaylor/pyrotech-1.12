package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.pyrotech.spi.recipe.IRecipeTimed;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class StoneMachineRecipeBase<T extends IForgeRegistryEntry<T>>
    extends IForgeRegistryEntry.Impl<T>
    implements IRecipeTimed {

  protected final int timeTicks;

  public StoneMachineRecipeBase(int timeTicks) {

    this.timeTicks = timeTicks;
  }

  @Override
  public int getTimeTicks() {

    return this.timeTicks;
  }
}
