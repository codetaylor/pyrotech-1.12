package com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import com.codetaylor.mc.pyrotech.modules.hunting.tile.TileCarcass;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CarcassProviderDelegate
    extends ProviderDelegateBase<CarcassProviderDelegate.ICarcassDisplay, TileCarcass> {

  private List<ItemStack> toolList;

  public CarcassProviderDelegate(ICarcassDisplay display) {

    super(display);
  }

  @Override
  public void display(TileCarcass tile) {

    if (this.toolList == null) {
      this.toolList = new ArrayList<>();

      for (String registryName : ModuleHuntingConfig.CARCASS.ALLOWED_KNIVES) {
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

  public interface ICarcassDisplay {

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);
  }
}
