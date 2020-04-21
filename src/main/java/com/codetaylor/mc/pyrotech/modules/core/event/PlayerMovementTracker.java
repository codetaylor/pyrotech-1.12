package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ModuleCore.MOD_ID)
public final class PlayerMovementTracker {

  private static final Object2IntMap<UUID> TICKS_SINCE_LAST_MOVE;
  private static final Map<UUID, BlockPos> LAST_KNOWN_POSITION;

  static {
    TICKS_SINCE_LAST_MOVE = new Object2IntArrayMap<>();
    TICKS_SINCE_LAST_MOVE.defaultReturnValue(0);
    LAST_KNOWN_POSITION = new HashMap<>();
  }

  @SubscribeEvent
  public static void on(TickEvent.PlayerTickEvent event) {

    if (event.player.world.isRemote || event.phase != TickEvent.Phase.START) {
      return;
    }

    UUID uuid = event.player.getUniqueID();
    BlockPos lastPos = LAST_KNOWN_POSITION.get(uuid);
    BlockPos position = event.player.getPosition();

    if (position.equals(lastPos)) {
      TICKS_SINCE_LAST_MOVE.put(uuid, TICKS_SINCE_LAST_MOVE.getInt(uuid) + 1);

    } else {
      TICKS_SINCE_LAST_MOVE.put(uuid, 0);
    }

    LAST_KNOWN_POSITION.put(uuid, position);
  }

  public static int getTicksSinceLastMove(EntityPlayer player) {

    return TICKS_SINCE_LAST_MOVE.get(player.getUniqueID());
  }
}
