package com.codetaylor.mc.pyrotech.patreon;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = ModPyrotech.MOD_ID)
public class PlayerEntityTracker {

  private static final ThreadLocal<List<EntityPlayer>> PLAYER_LIST = ThreadLocal.withInitial(ArrayList::new);
  private static final ThreadLocal<Map<UUID, EntityPlayer>> PLAYER_MAP = ThreadLocal.withInitial(HashMap::new);

  @Nullable
  public static EntityPlayer getEntityForPlayer(UUID uuid) {

    Map<UUID, EntityPlayer> playerMap = PLAYER_MAP.get();
    return playerMap.get(uuid);
  }

  @SubscribeEvent
  public static void on(EntityJoinWorldEvent event) {

    if (!event.getWorld().isRemote) {
      return;
    }

    Entity entity = event.getEntity();

    if (entity instanceof EntityPlayer) {
      List<EntityPlayer> playerList = PLAYER_LIST.get();
      Map<UUID, EntityPlayer> playerMap = PLAYER_MAP.get();
      EntityPlayer entityPlayer = (EntityPlayer) entity;

      playerList.removeIf(p -> p.getUniqueID().equals(entity.getUniqueID()));
      playerMap.remove(entity.getUniqueID());

      playerList.add(entityPlayer);
      playerMap.put(entityPlayer.getUniqueID(), entityPlayer);
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public static void on(TickEvent.ClientTickEvent event) {

    if (event.phase == TickEvent.Phase.START) {
      PlayerEntityTracker.expireEntries();
    }
  }

  @SideOnly(Side.SERVER)
  @SubscribeEvent
  public static void on(TickEvent.ServerTickEvent event) {

    if (event.phase == TickEvent.Phase.START) {
      PlayerEntityTracker.expireEntries();
    }
  }

  private static void expireEntries() {

    Map<UUID, EntityPlayer> playerMap = PLAYER_MAP.get();

    for (Iterator<EntityPlayer> it = PLAYER_LIST.get().iterator(); it.hasNext(); ) {
      EntityPlayer entityPlayer = it.next();

      if (entityPlayer.isDead) {
        it.remove();
        playerMap.remove(entityPlayer.getUniqueID());
      }
    }
  }

}
