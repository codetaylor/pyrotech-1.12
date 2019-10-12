package com.codetaylor.mc.pyrotech.modules.plugin.patchouli;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliHelper {

  public static void openBook(World world, EntityPlayer player, ResourceLocation resourceLocation) {

    PatchouliAPI.instance.openBookGUI((EntityPlayerMP) player, resourceLocation);
    SoundEvent sfx = SoundEvent.REGISTRY.getObject(new ResourceLocation("patchouli", "book_open"));

    if (sfx != null) {
      world.playSound(null, player.posX, player.posY, player.posZ, sfx, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
    }
  }
}
