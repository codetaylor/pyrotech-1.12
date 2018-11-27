package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InteractionBlockDelegate {

  @Nullable
  public static <T extends TileEntity & ITileInteractable> RayTraceResult collisionRayTrace(RayTraceResult result, T tile, IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, Vec3d end) {

    if (result == null) {
      return null;
    }

    Vec3d blockStart = new Vec3d(start.x, start.y, start.z);
    Vec3d blockEnd = new Vec3d(end.x, end.y, end.z);

    // Translate trace to origin
    blockStart = blockStart.subtract(pos.getX(), pos.getY(), pos.getZ());
    blockEnd = blockEnd.subtract(pos.getX(), pos.getY(), pos.getZ());

    // Rotate
    EnumFacing tileFacing = tile.getTileFacing(world, pos, blockState);
    blockStart = InteractionBlockDelegate.rotate(blockStart, tileFacing);
    blockEnd = InteractionBlockDelegate.rotate(blockEnd, tileFacing);

    IInteraction[] interactions = tile.getInteractions();

    if (interactions.length == 0) {
      return result;
    }

    List<ResultData> resultDataList = new ArrayList<>();

    for (int i = 0; i < interactions.length; i++) {

      if (!interactions[i].isEnabled()) {
        continue;
      }

      RayTraceResult candidateTrace = interactions[i].getInteractionBounds(world, pos, blockState).calculateIntercept(blockStart, blockEnd);

      if (candidateTrace != null
          && interactions[i].allowInteractionWithSide(candidateTrace.sideHit)) {

        double distanceSq = blockStart.squareDistanceTo(candidateTrace.hitVec);
        resultDataList.add(new ResultData(distanceSq, candidateTrace, interactions[i]));
      }
    }

    if (resultDataList.isEmpty()) {
      return result;
    }

    resultDataList.size();
    Collections.sort(resultDataList);
    RayTraceResult[] results = new RayTraceResult[resultDataList.size()];

    for (int i = 0; i < resultDataList.size(); i++) {
      ResultData data = resultDataList.get(i);
      results[i] = data.rayTraceResult;
      results[i].hitInfo = data.interaction;
    }

    result.hitInfo = results;
    return result;
  }

  private static Vec3d rotate(Vec3d vec, EnumFacing tileFacing) {

    float angle;

    switch (tileFacing) {

      case NORTH:
        angle = 0;
        break;

      case EAST:
        angle = (float) -(Math.PI / 2);
        break;

      case SOUTH:
        angle = (float) -Math.PI;
        break;

      case WEST:
        angle = (float) -(Math.PI + Math.PI / 2);
        break;

      case UP:
      case DOWN:
      default:
        throw new IllegalArgumentException("Unsupported facing: " + tileFacing);
    }

    float cos = MathHelper.cos(angle);
    float sin = MathHelper.sin(angle);
    double rx = 0.5 + (vec.x - 0.5) * cos - (vec.z - 0.5) * sin;
    double rz = 0.5 + (vec.x - 0.5) * sin + (vec.z - 0.5) * cos;

    return new Vec3d(rx, vec.y, rz);
  }

  private InteractionBlockDelegate() {
    //
  }

  private static class ResultData
      implements Comparable<ResultData> {

    private final double distanceSq;
    final RayTraceResult rayTraceResult;
    final IInteraction interaction;

    private ResultData(double distanceSq, RayTraceResult rayTraceResult, IInteraction interaction) {

      this.distanceSq = distanceSq;
      this.rayTraceResult = rayTraceResult;
      this.interaction = interaction;
    }

    @Override
    public int compareTo(@Nonnull ResultData o) {

      return Double.compare(this.distanceSq, o.distanceSq);
    }
  }

}
