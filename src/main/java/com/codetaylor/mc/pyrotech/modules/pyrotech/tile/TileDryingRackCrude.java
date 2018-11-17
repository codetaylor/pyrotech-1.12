package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
<<<<<<< HEAD
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.ITileInteractionHandler_ItemStack_Provider;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.InteractionHandler_ItemStack_Base;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
=======
>>>>>>> parent of 03250e2... Switch drying rack, crude drying rack and campfire to interactable
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

public class TileDryingRackCrude
<<<<<<< HEAD
    extends TileDryingRackBase
    implements ITileInteractionHandler_ItemStack_Provider {

  private InteractionHandler_ItemStack_Base[] interactionHandlers;
  private AxisAlignedBB renderBounds;
=======
    extends TileDryingRackBase {
>>>>>>> parent of 03250e2... Switch drying rack, crude drying rack and campfire to interactable

  public TileDryingRackCrude() {

    super();
<<<<<<< HEAD

    this.interactionHandlers = new InteractionHandler_ItemStack_Base[]{
        new TileDryingRackCrude.InteractionHandler(new ItemStackHandler[]{this.stackHandler, this.outputStackHandler}, 0)
    };
=======
>>>>>>> parent of 03250e2... Switch drying rack, crude drying rack and campfire to interactable
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

    if (this.renderBounds == null) {
      this.renderBounds = new AxisAlignedBB(this.getPos());
    }

    return this.renderBounds;
  }
<<<<<<< HEAD

  @Override
  public InteractionHandler_ItemStack_Base[] getInteractionHandlers() {

    return this.interactionHandlers;
  }

  public static class InteractionHandler
      extends InteractionHandler_ItemStack_Base {

    private static final Transform TRANSFORM_NORTH = new Transform(
        new Vec3d(0.5, 0.5, 0.15),
        new Quaternion(),
        new Vec3d(0.75, 0.75, 0.75)
    );
    private static final Transform TRANSFORM_SOUTH = new Transform(
        new Vec3d(0.5, 0.5, 0.85),
        QuaternionHelper.setFromAxisAngle(new Quaternion(), 0, 1, 0, (float) Math.PI /* 180 */),
        new Vec3d(0.75, 0.75, 0.75)
    );
    private static final Transform TRANSFORM_EAST = new Transform(
        new Vec3d(0.85, 0.5, 0.5),
        QuaternionHelper.setFromAxisAngle(new Quaternion(), 0, 1, 0, (float) (Math.PI + Math.PI / 2) /* 270 */),
        new Vec3d(0.75, 0.75, 0.75)
    );
    private static final Transform TRANSFORM_WEST = new Transform(
        new Vec3d(0.15, 0.5, 0.5),
        QuaternionHelper.setFromAxisAngle(new Quaternion(), 0, 1, 0, (float) (Math.PI / 2) /* 90 */),
        new Vec3d(0.75, 0.75, 0.75)
    );

    /* package */ InteractionHandler(ItemStackHandler[] stackHandlers, int slot) {

      super(stackHandlers, slot);
    }

    @Override
    public Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack) {

      EnumFacing facing = blockState.getValue(BlockDryingRack.FACING);

      switch (facing) {
        default:
        case NORTH:
          return TRANSFORM_NORTH;
        case SOUTH:
          return TRANSFORM_SOUTH;
        case EAST:
          return TRANSFORM_EAST;
        case WEST:
          return TRANSFORM_WEST;
      }

    }
  }

=======
>>>>>>> parent of 03250e2... Switch drying rack, crude drying rack and campfire to interactable
}
