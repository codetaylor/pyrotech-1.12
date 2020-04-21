package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.util.*;

public final class CampfireEffectTracker {

  public static final Map<UUID, Set<BlockPos>> TRACKING_CAMPFIRES = new HashMap<>();

  private int intervalCounter;

  @SubscribeEvent
  public void on(TickEvent.PlayerTickEvent event) {

    if (event.phase == TickEvent.Phase.END && ++this.intervalCounter >= 20) {
      this.intervalCounter = 0;
      this.updateTracking(event.player);
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

        if (!(tileEntity instanceof TileCampfire)
            || !((TileCampfire) tileEntity).isEntityInEffectRange(entity)) {
          it.remove();
        }
      }
    }

    // Check the time.
    boolean effectsActive = world.getWorldTime() >= ModuleTechBasicConfig.CAMPFIRE_EFFECTS.EFFECTS_START_TIME
        && world.getWorldTime() <= ModuleTechBasicConfig.CAMPFIRE_EFFECTS.EFFECTS_STOP_TIME;

    if (trackingCampfires.isEmpty() || !effectsActive) {
      entity.removePotionEffect(ModuleTechBasic.Potions.COMFORT);
      entity.removePotionEffect(ModuleTechBasic.Potions.RESTING);
    }
  }
}
