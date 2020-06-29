package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.util.*;

public final class CampfireEffectTracker {

  public static final Map<UUID, Set<BlockPos>> TRACKING_CAMPFIRES = new HashMap<>();

  private int intervalCounter;

  @SubscribeEvent
  public void on(TickEvent.ServerTickEvent event) {

    if (event.phase != TickEvent.Phase.START) {
      return;
    }

    this.intervalCounter += 1;

    if (this.intervalCounter >= 10) {
      this.intervalCounter = 0;

      MinecraftServer minecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
      PlayerList playerList = minecraftServer.getPlayerList();
      List<EntityPlayerMP> players = playerList.getPlayers();

      for (EntityPlayerMP player : players) {
        this.updateTracking(player);
      }
    }
  }

  private void updateTracking(@Nonnull EntityPlayer entity) {

    World world = entity.getEntityWorld();

    if (world.isRemote) {
      return;
    }

    UUID uuid = entity.getUniqueID();
    Set<BlockPos> trackingCampfires = CampfireEffectTracker.TRACKING_CAMPFIRES.getOrDefault(uuid, Collections.emptySet());

    // If there are campfires tracking this player, iterate through them
    // and determine if the player is still within range. If not, remove
    // the tracking info.
    if (!trackingCampfires.isEmpty()) {

      for (Iterator<BlockPos> it = trackingCampfires.iterator(); it.hasNext(); ) {
        BlockPos blockPos = it.next();
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (!(tileEntity instanceof TileCampfire)) {
          it.remove();

        } else {
          TileCampfire tileCampfire = (TileCampfire) tileEntity;

          if (!tileCampfire.workerIsActive()
              || !tileCampfire.isEntityInEffectRange(entity)) {
            it.remove();
          }
        }
      }
    }

    // Check the time.
    long worldTime = world.getWorldTime() % 24000;
    boolean effectsActive = worldTime >= ModuleTechBasicConfig.CAMPFIRE_EFFECTS.EFFECTS_START_TIME
        && worldTime <= ModuleTechBasicConfig.CAMPFIRE_EFFECTS.EFFECTS_STOP_TIME;

    if (trackingCampfires.isEmpty() || !effectsActive) {
      Map<Potion, PotionEffect> activePotionMap = entity.getActivePotionMap();

      if (activePotionMap.containsKey(ModuleTechBasic.Potions.COMFORT)) {
        entity.removePotionEffect(ModuleTechBasic.Potions.COMFORT);

        if (ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {

          if (trackingCampfires.isEmpty()) {
            String message = "Removed comfort effect due to no tracking campfires";
            ModuleCore.LOGGER.debug(message);
            entity.sendMessage(new TextComponentString(message));

          } else if (!effectsActive) {
            String message = "Removed comfort effect due to time";
            ModuleCore.LOGGER.debug(message);
            entity.sendMessage(new TextComponentString(message));
          }
        }
      }

      if (activePotionMap.containsKey(ModuleTechBasic.Potions.RESTING)) {
        entity.removePotionEffect(ModuleTechBasic.Potions.RESTING);

        if (ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {

          if (trackingCampfires.isEmpty()) {
            String message = "Removed resting effect due to no tracking campfires";
            ModuleCore.LOGGER.debug(message);
            entity.sendMessage(new TextComponentString(message));

          } else if (!effectsActive) {
            String message = "Removed resting effect due to time";
            ModuleCore.LOGGER.debug(message);
            entity.sendMessage(new TextComponentString(message));
          }
        }
      }
    }
  }
}
