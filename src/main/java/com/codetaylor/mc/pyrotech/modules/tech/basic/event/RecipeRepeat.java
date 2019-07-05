package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileWorktable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class RecipeRepeat {

  public static class RightClickBlockEventHandler {

    @SubscribeEvent
    public void on(PlayerInteractEvent.RightClickBlock event) {

      // This handler is only registered if the ALLOW_RECIPE_REPEAT config value
      // is set to true.

      World world = event.getWorld();
      BlockPos pos = event.getPos();
      IBlockState blockState = world.getBlockState(pos);
      Block block = blockState.getBlock();

      if (block == ModuleTechBasic.Blocks.WORKTABLE
          || block == ModuleTechBasic.Blocks.WORKTABLE_STONE) {

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileWorktable) {
          event.setUseBlock(Event.Result.ALLOW);
          event.setUseItem(Event.Result.DENY);
        }
      }
    }
  }

  private RecipeRepeat() {
    //
  }
}
