package com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.tile.TileCarcass;
import net.minecraft.item.ItemStack;

public class CarcassProviderDelegate
    extends ProviderDelegateBase<CarcassProviderDelegate.ICarcassDisplay, TileCarcass> {

  private ItemStack itemStackInput;

  public CarcassProviderDelegate(ICarcassDisplay display) {

    super(display);
  }

  @Override
  public void display(TileCarcass tile) {

    if (this.itemStackInput == null) {
      this.itemStackInput = new ItemStack(ModuleHunting.Items.IRON_HUNTING_KNIFE);
    }

    float progress = tile.getCurrentProgress();
    this.display.setRecipeProgress(this.itemStackInput, tile.getNextItem(), (int) (100 * progress), 100);
  }

  public interface ICarcassDisplay {

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);
  }
}
