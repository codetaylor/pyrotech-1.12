package com.codetaylor.mc.pyrotech.library.util;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public final class ExperienceHelper {

  public static void spawnXp(World world, int multiple, float xp, BlockPos pos) {

    int i = multiple;

    if (xp == 0) {
      i = 0;

    } else if (xp < 1) {
      float totalXp = i * xp;
      int floorXp = MathHelper.floor(totalXp);
      int ceilXp = MathHelper.ceil(totalXp);

      if (floorXp < ceilXp && Math.random() < (totalXp - floorXp)) {
        ++floorXp;
      }

      i = floorXp;
    }

    while (i > 0) {
      int k = EntityXPOrb.getXPSplit(i);
      i -= k;
      world.spawnEntity(new EntityXPOrb(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, k));
    }
  }
}
