package com.codetaylor.mc.pyrotech.interaction.spi;

import com.codetaylor.mc.pyrotech.interaction.util.InteractionRayTraceData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Interactable tiles must implement this interface and override the
 * {@link #getInteractions()} method to return an array of the tile's
 * interactions.
 */
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

  /**
   * Override this for tiles with facing and return the tile's facing.
   * <p>
   * NOTE: The system currently works with only horizontal facings.
   * <p>
   * The default facing is NORTH.
   *
   * @param world      the world
   * @param pos        the position of the tile
   * @param blockState the blockState at the tile's position
   * @return this tile's facing
   */
  default EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return EnumFacing.NORTH;
  }

  /**
   * Call this method from the block's {@link Block#onBlockActivated(World, BlockPos, IBlockState, EntityPlayer, EnumHand, EnumFacing, float, float, float)}
   * method and pass in the block's tile entity.
   *
   * @param <T>    the type of tile being interacted with
   * @param type   the source of the interaction
   * @param tile   the tile being interacted with
   * @param world  the world
   * @param pos    the position of the block/tile being interacted with
   * @param state  the blockState of the block being interacted with
   * @param player the player doing the interaction
   * @param hand   the hand the player is using
   * @param facing the facing of the side of the block's AABB being interacted with
   * @param hitX   the vec.x of the interaction
   * @param hitY   the vec.y of the interaction
   * @param hitZ   the vec.z of the interaction
   */
  default <T extends TileEntity & ITileInteractable> void interact(IInteraction.EnumType type, T tile, World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    Vec3d posVec = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
    int interactionDistance = 5;
    RayTraceResult rayTraceResult = world.rayTraceBlocks(posVec, posVec.add(player.getLookVec().scale(interactionDistance)), false);

    if (rayTraceResult != null
        && rayTraceResult.hitInfo instanceof InteractionRayTraceData.List) {

      InteractionRayTraceData.List results = (InteractionRayTraceData.List) rayTraceResult.hitInfo;

      for (int i = 0; i < results.size(); i++) {

        InteractionRayTraceData data = results.get(i);
        IInteraction interaction = data.getInteraction();

        //noinspection unchecked
        if (interaction.allowInteractionWithHand(hand)
            && interaction.allowInteractionWithSide(data.getRayTraceResult().sideHit)
            && interaction.interact(type, tile, world, pos, state, player, hand, facing, hitX, hitY, hitZ)) {
          break;
        }
      }
    }
  }

  default <T extends TileEntity & ITileInteractable> T asTileInteractable() {

    //noinspection unchecked
    return (T) this;
  }
}
