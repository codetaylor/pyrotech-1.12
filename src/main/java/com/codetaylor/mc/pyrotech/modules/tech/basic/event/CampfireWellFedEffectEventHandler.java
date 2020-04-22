package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class CampfireWellFedEffectEventHandler {

  private static MethodHandle foodStats$foodExhaustionLevelGetter;
  private static MethodHandle foodStats$foodExhaustionLevelSetter;

  static {

    try {

      foodStats$foodExhaustionLevelGetter = MethodHandles.lookup().unreflectGetter(
          /*
          MC 1.12: net/minecraft/util/FoodStats.foodExhaustionLevel
          Name: c => field_75126_c => foodExhaustionLevel
          Comment: The player's food exhaustion.
          Side: BOTH
          AT: public net.minecraft.util.FoodStats field_75126_c # foodExhaustionLevel
           */
          ObfuscationReflectionHelper.findField(FoodStats.class, "field_75126_c")
      );

      foodStats$foodExhaustionLevelSetter = MethodHandles.lookup().unreflectSetter(
          /*
          MC 1.12: net/minecraft/util/FoodStats.foodExhaustionLevel
          Name: c => field_75126_c => foodExhaustionLevel
          Comment: The player's food exhaustion.
          Side: BOTH
          AT: public net.minecraft.util.FoodStats field_75126_c # foodExhaustionLevel
           */
          ObfuscationReflectionHelper.findField(FoodStats.class, "field_75126_c")
      );

    } catch (IllegalAccessException e) {
      ModuleCore.LOGGER.error("", e);
    }
  }

  private Object2FloatMap<UUID> playerExhaustionMap;
  private Set<UUID> failedSet;

  public CampfireWellFedEffectEventHandler() {

    this.playerExhaustionMap = new Object2FloatOpenHashMap<>();
    this.failedSet = new HashSet<>();
  }

  @SubscribeEvent
  public void on(LivingEntityUseItemEvent.Finish event) {

    EntityLivingBase entityLiving = event.getEntityLiving();

    if (!ModuleTechBasicConfig.CAMPFIRE_EFFECTS.WELL_FED_EFFECT_ENABLED
        || !ModuleTechBasicConfig.CAMPFIRE_EFFECTS.COMFORT_EFFECT_ENABLED
        || entityLiving.world.isRemote
        || !(event.getItem().getItem() instanceof ItemFood)
        || !entityLiving.getActivePotionMap().containsKey(ModuleTechBasic.Potions.COMFORT)) {
      return;
    }

    if (entityLiving instanceof EntityPlayer) {
      FoodStats foodStats = ((EntityPlayer) entityLiving).getFoodStats();
      float saturationLevel = foodStats.getSaturationLevel();
      int duration = Math.max(0, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.WELL_FED_DURATION_TICKS);

      if (duration > 0 && MathHelper.epsilonEquals(saturationLevel, 20)) {
        entityLiving.addPotionEffect(new PotionEffect(ModuleTechBasic.Potions.WELL_FED, duration, 0, false, true));
      }
    }
  }

  @SubscribeEvent
  public void on(TickEvent.PlayerTickEvent event) {

    if (!ModuleTechBasicConfig.CAMPFIRE_EFFECTS.WELL_FED_EFFECT_ENABLED
        || event.phase != TickEvent.Phase.START
        || event.player.world.isRemote) {
      return;
    }

    if (!event.player.getActivePotionMap().containsKey(ModuleTechBasic.Potions.WELL_FED)) {
      return;
    }

    UUID uuid = event.player.getUniqueID();
    boolean failed = false;

    try {

      float currentExhaustionLevel = this.getPlayerExhaustionLevel(event.player);

      if (!this.playerExhaustionMap.containsKey(uuid)) {
        this.playerExhaustionMap.put(uuid, currentExhaustionLevel);
      }

      float previousExhaustionLevel = this.playerExhaustionMap.getFloat(uuid);

      if (previousExhaustionLevel < currentExhaustionLevel) {
        // Player has gained exhaustion, reduce the gain by half
        float delta = currentExhaustionLevel - previousExhaustionLevel;
        float newExhaustionLevel = (float) (currentExhaustionLevel - delta * MathHelper.clamp(ModuleTechBasicConfig.CAMPFIRE_EFFECTS.WELL_FED_EXHAUSTION_MODIFIER, 0, 1));
        this.setPlayerExhaustionLevel(event.player, newExhaustionLevel);
        this.playerExhaustionMap.put(uuid, newExhaustionLevel);

      } else {
        this.playerExhaustionMap.put(uuid, currentExhaustionLevel);
      }

    } catch (Throwable e) {
      failed = true;

      if (!this.failedSet.contains(uuid)) {
        // Only log this if it is a new failure to prevent massive log spam
        this.failedSet.add(uuid);
        ModuleCore.LOGGER.error("", e);
      }

    } finally {

      if (!failed) {
        this.failedSet.remove(uuid);
      }
    }
  }

  private float getPlayerExhaustionLevel(EntityPlayer player) throws Throwable {

    FoodStats foodStats = player.getFoodStats();
    return (float) foodStats$foodExhaustionLevelGetter.invokeExact(foodStats);
  }

  private void setPlayerExhaustionLevel(EntityPlayer player, float value) throws Throwable {

    FoodStats foodStats = player.getFoodStats();
    foodStats$foodExhaustionLevelSetter.invokeExact(foodStats, value);
  }

}
