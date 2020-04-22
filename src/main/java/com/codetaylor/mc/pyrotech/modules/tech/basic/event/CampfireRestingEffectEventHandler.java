package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.event.PlayerMovementTracker;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.PotionResting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class CampfireRestingEffectEventHandler {

  @SubscribeEvent
  public void on(TickEvent.PlayerTickEvent event) {

    if (event.player.world.isRemote || event.phase != TickEvent.Phase.END) {
      return;
    }

    if (PlayerMovementTracker.getTicksSinceLastMove(event.player) == 0) {

      if (this.resetRestingEffect(event.player) && ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {
        String message = "Reset resting effect due to movement";
        ModuleCore.LOGGER.debug(message);
        event.player.sendMessage(new TextComponentString(message));
      }
    }
  }

  @SubscribeEvent
  public void on(LivingDamageEvent event) {

    // When a player with any level of the resting effect takes damage,
    // bust that effect back down to level I.

    Entity entity = event.getEntity();

    if (!entity.world.isRemote) {

      if (this.resetRestingEffect(entity) && ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {
        String message = "Reset resting effect due to damage taken";
        ModuleCore.LOGGER.debug(message);
        entity.sendMessage(new TextComponentString(message));
      }

      if (entity instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) entity;

        if (player.getAbsorptionAmount() == 0
            && player.getActivePotionMap().containsKey(ModuleTechBasic.Potions.WELL_RESTED)) {
          event.getEntityLiving().removePotionEffect(ModuleTechBasic.Potions.WELL_RESTED);

          if (ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {
            String message = "Removed Well Rested effect due to loss of absorption hearts";
            ModuleCore.LOGGER.debug(message);
            entity.sendMessage(new TextComponentString(message));
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void on(AttackEntityEvent event) {

    // When a player with any level of the resting effect attacks,
    // bust that effect back down to level I.

    Entity entity = event.getEntity();

    if (!entity.world.isRemote && this.resetRestingEffect(entity) && ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {
      String message = "Reset resting effect due to attacking";
      ModuleCore.LOGGER.debug(message);
      entity.sendMessage(new TextComponentString(message));
    }
  }

  @SubscribeEvent
  public void on(LivingHurtEvent event) {

    // When a player with any level of the resting effect hurts an entity,
    // bust that effect back down to level I.

    Entity trueSource = event.getSource().getTrueSource();

    if (trueSource != null && !trueSource.world.isRemote && this.resetRestingEffect(trueSource) && ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {
      String message = "Reset resting effect due to attacking";
      ModuleCore.LOGGER.debug(message);
      trueSource.sendMessage(new TextComponentString(message));
    }
  }

  private boolean resetRestingEffect(Entity entity) {

    if (entity instanceof EntityPlayer) {

      EntityPlayer player = (EntityPlayer) entity;
      PotionEffect potionEffect = player.getActivePotionEffect(ModuleTechBasic.Potions.RESTING);

      if (potionEffect != null && potionEffect.getAmplifier() > 0) {
        player.removePotionEffect(ModuleTechBasic.Potions.RESTING);
        PotionResting.addEffect(player);
        return true;
      }
    }

    return false;
  }
}
