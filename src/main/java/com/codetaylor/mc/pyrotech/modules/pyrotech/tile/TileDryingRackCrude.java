package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.util.QuaternionHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.InteractionHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.util.vector.Quaternion;

import javax.annotation.Nonnull;

public class TileDryingRackCrude
    extends TileDryingRackBase
    implements ITileInteractable {

  private InteractionHandler[] interactionHandlers;

  public TileDryingRackCrude() {

    super();

    this.interactionHandlers = new InteractionHandler[]{
        new TileInteractionHandler(new ItemStackHandler[]{this.stackHandler, this.outputStackHandler}, 0)
    };
  }

  @Override
  protected int getSlotCount() {

    return 1;
  }

  @Override
  protected float getSpeedModified(float speed) {

    return (float) (speed * ModulePyrotechConfig.CRUDE_DRYING_RACK.SPEED_MODIFIER);
  }

  @Override
  protected float getRainSpeed() {

    return 0;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getRenderBoundingBox() {

    return new AxisAlignedBB(this.getPos());
  }

  @Override
  public InteractionHandler[] getInteractionHandlers() {

    return this.interactionHandlers;
  }

  public static class TileInteractionHandler
      extends InteractionHandler {

    private static final Transforms TRANSFORMS_NORTH = new Transforms(
        new Vec3d(0.5, 0.5, 0.15),
        new Quaternion(),
        new Vec3d(0.75, 0.75, 0.75)
    );
    private static final Transforms TRANSFORMS_SOUTH = new Transforms(
        new Vec3d(0.5, 0.5, 0.85),
        QuaternionHelper.setFromAxisAngle(new Quaternion(), 0, 1, 0, (float) Math.PI /* 180 */),
        new Vec3d(0.75, 0.75, 0.75)
    );
    private static final Transforms TRANSFORMS_EAST = new Transforms(
        new Vec3d(0.85, 0.5, 0.5),
        QuaternionHelper.setFromAxisAngle(new Quaternion(), 0, 1, 0, (float) (Math.PI + Math.PI / 2) /* 270 */),
        new Vec3d(0.75, 0.75, 0.75)
    );
    private static final Transforms TRANSFORMS_WEST = new Transforms(
        new Vec3d(0.15, 0.5, 0.5),
        QuaternionHelper.setFromAxisAngle(new Quaternion(), 0, 1, 0, (float) (Math.PI / 2) /* 90 */),
        new Vec3d(0.75, 0.75, 0.75)
    );

    public TileInteractionHandler(ItemStackHandler[] stackHandlers, int slot) {

      super(stackHandlers, slot, EnumFacing.VALUES, INFINITE_EXTENT_AABB, null);
    }

    @Override
    public Transforms getTransforms(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack) {

      EnumFacing facing = blockState.getValue(BlockDryingRack.FACING);

      switch (facing) {
        default:
        case NORTH:
          return TRANSFORMS_NORTH;
        case SOUTH:
          return TRANSFORMS_SOUTH;
        case EAST:
          return TRANSFORMS_EAST;
        case WEST:
          return TRANSFORMS_WEST;
      }

    }
  }

}
