package com.codetaylor.mc.pyrotech.modules.plugin.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.ModulePatchouliConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerJoinEventHandler {

  private static final String KEY = "pyrotech.start";

  @SubscribeEvent
  public void on(PlayerEvent.PlayerLoggedInEvent event) {

    if (ModulePatchouliConfig.GIVE_BOOK_ON_START) {
      NBTTagCompound data = event.player.getEntityData();
      NBTTagCompound persistent;

      if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
        data.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistent = new NBTTagCompound()));

      } else {
        persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
      }

      if (!persistent.hasKey(KEY)) {
        persistent.setBoolean(KEY, true);
        event.player.inventory.addItemStackToInventory(new ItemStack(ModuleCore.Items.BOOK));
      }
    }
  }

}
