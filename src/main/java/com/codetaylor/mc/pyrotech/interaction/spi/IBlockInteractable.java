package com.codetaylor.mc.pyrotech.interaction.spi;

import com.codetaylor.mc.pyrotech.interaction.util.InteractionRayTracer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IBlockInteractable {

  default boolean interact(
      IInteraction.EnumType type,
      World world,
      BlockPos pos,
      IBlockState state,
      EntityPlayer player,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof ITileInteractable) {
      ITileInteractable tile = (ITileInteractable) tileEntity;

      if (tile.getInteractionCooldown() <= 0) {
        tile.interact(type, tile.asTileInteractable(), world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        return true;
      }
    }

    return false;
  }

  default RayTraceResult interactionRayTrace(RayTraceResult result, IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof ITileInteractable) {
      return InteractionRayTracer.collisionRayTrace(result, ((ITileInteractable) tileEntity).asTileInteractable(), blockState, world, pos, start, end);
    }

    return result;
  }

}
