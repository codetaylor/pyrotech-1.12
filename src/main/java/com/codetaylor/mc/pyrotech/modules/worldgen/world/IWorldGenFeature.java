package com.codetaylor.mc.pyrotech.modules.worldgen.world;

import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import net.minecraftforge.fml.common.IWorldGenerator;

public interface IWorldGenFeature
    extends IWorldGenerator {

  boolean isAllowed(int dimensionId);

  default boolean isAllowedDimension(int dimensionId, int[] whitelist, int[] blacklist) {

    if (whitelist.length > 0) {
      return ArrayHelper.containsInt(whitelist, dimensionId);

    } else if (blacklist.length > 0) {
      return !ArrayHelper.containsInt(blacklist, dimensionId);
    }

    return true;
  }
}
