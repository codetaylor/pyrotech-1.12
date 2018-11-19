package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public abstract class InteractionBase<T extends TileEntity & ITileInteractable>
    implements IInteraction<T> {

  protected final int sides;
  protected final InteractionBounds bounds;

  public InteractionBase(EnumFacing[] sides, InteractionBounds bounds) {

    this.bounds = bounds;

    int sidesEncoded = 0;

    for (EnumFacing side : sides) {
      sidesEncoded |= side.getIndex();
    }

    this.sides = sidesEncoded;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean canInteractWith(World world, EnumFacing hitSide, BlockPos hitPos, Vec3d hitVec, BlockPos tilePos, IBlockState tileBlockState, EnumFacing tileFacing) {

    double hitX = hitVec.x - tilePos.getX();
    double hitY = hitVec.y - tilePos.getY();
    double hitZ = hitVec.z - tilePos.getZ();

    double x = 0;
    double y = 0;

    // TODO: test this is right
    switch (hitSide) {
      case UP:
        x = hitX;
        y = hitZ;

        float angle = 0;

        switch (tileFacing) {
          case EAST:
            angle = (float) -(Math.PI / 2);
            break;
          case SOUTH:
            angle = (float) -Math.PI;
            break;
          case WEST:
            angle = (float) -(Math.PI + Math.PI / 2);
            break;
        }

        double nx = 0.5 + (x - 0.5) * MathHelper.cos(angle) - (y - 0.5) * MathHelper.sin(angle);
        double ny = 0.5 + (x - 0.5) * MathHelper.sin(angle) + (y - 0.5) * MathHelper.cos(angle);

        x = nx;
        y = ny;

        break;
      case DOWN:
        x = 1.0 - hitX;
        y = hitZ;
        break;
      case NORTH: // toward -Z
        x = 1.0 - hitX;
        y = hitY;
        break;
      case SOUTH: // toward +Z
        x = hitX;
        y = hitY;
        break;
      case EAST: // toward +X
        x = hitZ;
        y = hitY;
        break;
      case WEST: // toward -X
        x = 1.0 - hitZ;
        y = hitY;
        break;
    }

    EnumFacing actualSideHit = this.getActualSideHit(world, hitSide, x, y, tilePos, tileBlockState, tileFacing);
    return this.canInteractWithActualSide(world, actualSideHit, x, y, tilePos, tileBlockState);
  }

  protected EnumFacing getActualSideHit(World world, EnumFacing hitSide, double hitX, double hitY, BlockPos tilePos, IBlockState tileBlockState, EnumFacing tileFacing) {

    if (tileFacing == EnumFacing.EAST) {

      switch (hitSide) {
        case NORTH:
          return EnumFacing.WEST;
        case SOUTH:
          return EnumFacing.EAST;
        case EAST:
          return EnumFacing.NORTH;
        case WEST:
          return EnumFacing.SOUTH;
      }

    } else if (tileFacing == EnumFacing.SOUTH) {

      switch (hitSide) {
        case NORTH:
          return EnumFacing.SOUTH;
        case SOUTH:
          return EnumFacing.NORTH;
        case EAST:
          return EnumFacing.WEST;
        case WEST:
          return EnumFacing.EAST;
      }

    } else if (tileFacing == EnumFacing.WEST) {

      switch (hitSide) {
        case NORTH:
          return EnumFacing.EAST;
        case SOUTH:
          return EnumFacing.WEST;
        case EAST:
          return EnumFacing.SOUTH;
        case WEST:
          return EnumFacing.NORTH;
      }

    }

    return hitSide;
  }

  protected boolean canInteractWithActualSide(World world, EnumFacing actualSide, double hitX, double hitY, BlockPos pos, IBlockState blockState) {

    return ((this.sides & actualSide.getIndex()) == actualSide.getIndex())
        && this.bounds.contains(hitX, hitY);
  }
}
