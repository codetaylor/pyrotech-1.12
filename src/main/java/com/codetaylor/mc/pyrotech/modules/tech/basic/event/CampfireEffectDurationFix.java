package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class CampfireEffectDurationFix {

  @SubscribeEvent
  public void on(TickEvent.PlayerTickEvent event) {

    if (event.player.world.isRemote) {

      {
        PotionEffect effect = event.player.getActivePotionEffect(ModuleTechBasic.Potions.FOCUSED);

        if (effect != null) {
          effect.setPotionDurationMax(true);
        }
      }

      {
        PotionEffect effect = event.player.getActivePotionEffect(ModuleTechBasic.Potions.RESTING);

        if (effect != null) {
          effect.setPotionDurationMax(true);
        }
      }

      {
        PotionEffect effect = event.player.getActivePotionEffect(ModuleTechBasic.Potions.COMFORT);

        if (effect != null) {
          effect.setPotionDurationMax(true);
        }
      }
    }
  }
}
