package com.codetaylor.mc.pyrotech.interaction.util;

import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

public final class InteractionRayTracer {

  /**
   * Traces through the block and assigns a list of intersected interactions
   * to the given result's hit info.
   * <p>
   * Call this from {@link net.minecraft.block.Block#collisionRayTrace(IBlockState, World, BlockPos, Vec3d, Vec3d)}.
   *
   * @param result     the original block ray trace result
   * @param tile       the tile at the block pos being traced
   * @param blockState the block state at the block pos being traced
   * @param world      the world
   * @param pos        the pos of the block being traced
   * @param start      the start vec of the trace
   * @param end        the end vec of the trace
   * @param <T>        the interactable tile type
   * @return the original ray trace result maybe with added hit info
   */
  @Nullable
  public static <T extends TileEntity & ITileInteractable> RayTraceResult collisionRayTrace(RayTraceResult result, T tile, IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, Vec3d end) {

    if (result == null) {
      return null;
    }

    // Translate
    Vec3d blockStart = new Vec3d(start.x - pos.getX(), start.y - pos.getY(), start.z - pos.getZ());
    Vec3d blockEnd = new Vec3d(end.x - pos.getX(), end.y - pos.getY(), end.z - pos.getZ());

    // Rotate
    EnumFacing tileFacing = tile.getTileFacing(world, pos, blockState);
    blockStart = InteractionRayTracer.rotate(blockStart, tileFacing);
    blockEnd = InteractionRayTracer.rotate(blockEnd, tileFacing);

    // We have to stretch the start and end of the ray before tracing because
    // sometimes the cast starts inside the block. We extend the cast by two
    // in each direction to be safe.

    Vec3d grow = blockEnd.subtract(blockStart).normalize().scale(2);

    blockStart = blockStart.subtract(grow);
    blockEnd = blockEnd.add(grow);

    IInteraction[] interactions = tile.getInteractions();

    if (interactions.length == 0) {
      return result;
    }

    // Gather results

    InteractionRayTraceData.List resultDataList = new InteractionRayTraceData.List();

    for (int i = 0; i < interactions.length; i++) {

      if (!interactions[i].isEnabled()) {
        continue;
      }

      RayTraceResult candidateTrace = interactions[i].getInteractionBounds(world, pos, blockState).calculateIntercept(blockStart, blockEnd);

      if (candidateTrace != null
          && interactions[i].allowInteractionWithSide(candidateTrace.sideHit)) {

        double distanceSq = blockStart.squareDistanceTo(candidateTrace.hitVec);
        resultDataList.add(new InteractionRayTraceData(distanceSq, candidateTrace, interactions[i]));
      }
    }

    if (resultDataList.isEmpty()) {
      return result;
    }

    // Sort and assign the results

    Collections.sort(resultDataList);
    result.hitInfo = resultDataList;
    return result;
  }

  private static final double NORTH_SIN = Math.sin(0);
  private static final double NORTH_COS = Math.cos(0);

  private static final double EAST_SIN = Math.sin(Math.PI + Math.PI / 2.0);
  private static final double EAST_COS = Math.cos(Math.PI + Math.PI / 2.0);

  private static final double SOUTH_SIN = Math.sin(Math.PI);
  private static final double SOUTH_COS = Math.cos(Math.PI);

  private static final double WEST_SIN = Math.sin(Math.PI / 2.0);
  private static final double WEST_COS = Math.cos(Math.PI / 2.0);

  private static Vec3d rotate(Vec3d vec, EnumFacing tileFacing) {

    switch (tileFacing) {

      case NORTH:
        return new Vec3d(0.5 + (vec.x - 0.5) * NORTH_COS - (vec.z - 0.5) * NORTH_SIN, vec.y, 0.5 + (vec.x - 0.5) * NORTH_SIN + (vec.z - 0.5) * NORTH_COS);

      case EAST:
        return new Vec3d(0.5 + (vec.x - 0.5) * EAST_COS - (vec.z - 0.5) * EAST_SIN, vec.y, 0.5 + (vec.x - 0.5) * EAST_SIN + (vec.z - 0.5) * EAST_COS);

      case SOUTH:
        return new Vec3d(0.5 + (vec.x - 0.5) * SOUTH_COS - (vec.z - 0.5) * SOUTH_SIN, vec.y, 0.5 + (vec.x - 0.5) * SOUTH_SIN + (vec.z - 0.5) * SOUTH_COS);

      case WEST:
        return new Vec3d(0.5 + (vec.x - 0.5) * WEST_COS - (vec.z - 0.5) * WEST_SIN, vec.y, 0.5 + (vec.x - 0.5) * WEST_SIN + (vec.z - 0.5) * WEST_COS);

      case UP:
      case DOWN:
      default:
        throw new IllegalArgumentException("Unsupported facing: " + tileFacing);
    }
  }

  private InteractionRayTracer() {
    //
  }

}
