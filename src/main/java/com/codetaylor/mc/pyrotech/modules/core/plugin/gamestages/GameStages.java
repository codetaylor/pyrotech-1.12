package com.codetaylor.mc.pyrotech.modules.core.plugin.gamestages;

import com.codetaylor.mc.pyrotech.library.Stages;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.Collection;

public final class GameStages {

  public static boolean allowed(EntityPlayer player, @Nullable Stages stages) {

    if (stages == null) {
      return true;
    }

    IStageData playerData = GameStageHelper.getPlayerData(player);
    Collection<String> playerStages = playerData.getStages();
    return stages.allowed(playerStages);
  }

  private GameStages() {
    //
  }
}
