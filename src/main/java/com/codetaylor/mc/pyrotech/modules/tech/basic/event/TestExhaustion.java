package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class TestExhaustion {

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

  public TestExhaustion() {

    this.playerExhaustionMap = new Object2FloatOpenHashMap<>();
    this.failedSet = new HashSet<>();
  }

  @SubscribeEvent
  public void on(TickEvent.PlayerTickEvent event) {

    if (event.phase != TickEvent.Phase.START
        || event.side == Side.CLIENT) {
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
        float newExhaustionLevel = currentExhaustionLevel - delta * 0.5f;
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
