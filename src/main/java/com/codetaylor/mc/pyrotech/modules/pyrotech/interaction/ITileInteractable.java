package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface ITileInteractable {

  IInteraction[] EMPTY_INTERACTION_HANDLERS = new IInteraction[0];

  default IInteraction[] getInteractions() {

    return EMPTY_INTERACTION_HANDLERS;
  }

  /**
   * Return true if the given position is a valid interaction position.
   * <p>
   * Use this to extend a tile entity's interaction beyond one block.
   * <p>
   * This is called every frame, optimize accordingly.
   *
   * @param world      the world
   * @param pos        the pos to check
   * @param blockState the blockState at the given pos
   * @return true if the given position is a valid interaction position
   */
  default boolean isExtendedInteraction(World world, BlockPos pos, IBlockState blockState) {

    return false;
  }

  default EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return EnumFacing.NORTH;
  }

  default <T extends TileEntity & ITileInteractable> void interact(T tile, World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    Vec3d posVec = new Vec3d(player.posX, player.posY + player.eyeHeight, player.posZ);
    int interactionDistance = 5;
    RayTraceResult rayTraceResult = world.rayTraceBlocks(posVec, posVec.add(player.getLookVec().scale(interactionDistance)), false);

    if (rayTraceResult != null
        && rayTraceResult.hitInfo instanceof RayTraceResult[]) {

      RayTraceResult[] results = (RayTraceResult[]) rayTraceResult.hitInfo;

      for (int i = 0; i < results.length; i++) {

        if (results[i].hitInfo instanceof IInteraction) {
          IInteraction interaction = (IInteraction) results[i].hitInfo;

          //noinspection unchecked
          if (interaction.allowInteractionWithHand(hand)
              && interaction.allowInteractionWithSide(results[i].sideHit)
              && interaction.interact(tile, world, pos, state, player, hand, facing, hitX, hitY, hitZ)) {
            break;
          }
        }
      }
    }
  }

  default <T extends TileEntity & ITileInteractable> T asTileInteractable() {

    //noinspection unchecked
    return (T) this;
  }
}
