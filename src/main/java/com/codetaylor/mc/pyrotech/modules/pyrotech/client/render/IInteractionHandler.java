package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IInteractionHandler {

  /**
   * Returns true if the handler can be interacted with given the criteria.
   * <p>
   * Note that the given {@link BlockPos} may be different than the position
   * in the ray trace result. For example, if the block above the TE is an
   * interaction handler extension, the ray trace result will contain results
   * for the block above the given blockPos.
   *
   * @param world      the world
   * @param hitSide    the side hit
   * @param hitPos     the position of the block hit
   * @param hitVec     the location of the hit relative to world origin
   * @param pos        the position of the intersected TE, may be different than the hitBlockPos
   * @param blockState the blockState of the intersected TE
   * @return true if the given {@link RayTraceResult} intersects this handler
   */
  boolean canInteractWith(World world, EnumFacing hitSide, BlockPos hitPos, Vec3d hitVec, BlockPos pos, IBlockState blockState);

}
