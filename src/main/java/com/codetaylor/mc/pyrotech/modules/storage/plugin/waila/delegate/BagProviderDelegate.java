package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.StringUtils;

public class BagProviderDelegate
    extends ProviderDelegateBase<BagProviderDelegate.IBagDisplay, TileBagBase> {

  private EntityPlayer player;

  public BagProviderDelegate(IBagDisplay display) {

    super(display);
  }

  public void setPlayer(EntityPlayer player) {

    this.player = player;
  }

  @Override
  public void display(TileBagBase tile) {

    TileBagBase.StackHandler stackHandler = tile.getStackHandler();
    int maxDigits = 0;

    if (tile.isOpen()) {

      // count max digits, used for layout

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        ItemStack stackInSlot = stackHandler.getStackInSlot(i);

        if (!stackInSlot.isEmpty()) {
          int count = stackInSlot.getCount();
          int digits = String.valueOf(count).length();

          if (digits > maxDigits) {
            maxDigits = digits;
          }
        }
      }

      this.display.setContents(stackHandler);
    }

    {
      String langKey = "gui." + ModuleTechRefractory.MOD_ID + ".waila.capacity";
      this.display.setItemCount(langKey, tile.getItemCount(), tile.getItemCapacity());
    }

    if (tile.isOpen()) {

      if (this.player.isSneaking()) {

        for (int i = 0; i < stackHandler.getSlots(); i++) {
          ItemStack stackInSlot = stackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            String count = String.valueOf(stackInSlot.getCount());

            if (count.length() < maxDigits) {
              count = TextFormatting.DARK_GRAY + StringUtils.repeat("0", maxDigits - count.length()).concat(TextFormatting.YELLOW + count);

            } else {
              count = TextFormatting.YELLOW + count;
            }

            this.display.setExtendedInfoOn(" " + count + " ", TextFormatting.GOLD, stackInSlot);
          }
        }

      } else {
        String langKey = "gui.pyrotech.tooltip.extended.shift";
        this.display.setExtendedInfoOff(langKey, Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY);
      }
    }
  }

  public interface IBagDisplay {

    void setContents(ItemStackHandler stackHandler);

    void setItemCount(String langKey, int itemCount, int itemCapacity);

    void setExtendedInfoOff(String langKey, TextFormatting startColor, TextFormatting endColor);

    void setExtendedInfoOn(String count, TextFormatting formatting, ItemStack itemStack);
  }
}
