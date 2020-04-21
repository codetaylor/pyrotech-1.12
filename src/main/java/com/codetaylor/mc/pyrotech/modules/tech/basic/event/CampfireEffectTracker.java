package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import net.minecraft.entity.EntityLivingBase;
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

  private void updateTracking(@Nonnull EntityLivingBase entity) {

    World world = entity.getEntityWorld();

    if (world.isRemote) {
      return;
    }

    if (entity instanceof EntityPlayer) {
      UUID uuid = entity.getUniqueID();
      Set<BlockPos> trackingCampfires = CampfireEffectTracker.TRACKING_CAMPFIRES.getOrDefault(uuid, Collections.emptySet());

      for (Iterator<BlockPos> it = trackingCampfires.iterator(); it.hasNext(); ) {
        BlockPos blockPos = it.next();
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (!(tileEntity instanceof TileCampfire)
            || !((TileCampfire) tileEntity).isEntityInEffectRange(entity)) {
          it.remove();
        }
      }

      if (trackingCampfires.isEmpty()) {
        entity.removePotionEffect(ModuleTechBasic.Potions.COMFORT);
        entity.removePotionEffect(ModuleTechBasic.Potions.RESTING);
      }
    }
  }
}
