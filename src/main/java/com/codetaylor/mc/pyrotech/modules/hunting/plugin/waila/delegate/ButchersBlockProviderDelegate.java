package com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import com.codetaylor.mc.pyrotech.modules.hunting.tile.TileButchersBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ButchersBlockProviderDelegate
    extends ProviderDelegateBase<ButchersBlockProviderDelegate.IButchersBlockDisplay, TileButchersBlock> {

  private List<ItemStack> toolList;

  public ButchersBlockProviderDelegate(IButchersBlockDisplay display) {

    super(display);
  }

  @Override
  public void display(TileButchersBlock tile) {

    if (tile.getInputStackHandler().getStackInSlot(0).isEmpty()) {
      return;
    }

    if (this.toolList == null) {
      this.toolList = new ArrayList<>();

      for (String registryName : ModuleHuntingConfig.BUTCHERS_BLOCK.ALLOWED_KNIVES) {
        Item item = Item.getByNameOrId(registryName);

        if (item != null) {
          this.toolList.add(new ItemStack(item));
        }
      }
    }

    float progress = tile.getCurrentProgress();

    if (this.toolList.isEmpty()) {
      this.display.setRecipeProgress(ItemStack.EMPTY, tile.getNextItem(), (int) (100 * progress), 100);

    } else {
      int index = (int) ((tile.getWorld().getTotalWorldTime() / 29) % this.toolList.size());
      this.display.setRecipeProgress(this.toolList.get(index), tile.getNextItem(), (int) (100 * progress), 100);
    }
  }

  public interface IButchersBlockDisplay {

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);
  }
}
