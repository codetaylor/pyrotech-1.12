package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.util.QuaternionHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.InteractionHandlerItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
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

  private InteractionHandlerItemStack[] interactionHandlers;
  private AxisAlignedBB renderBounds;

  public TileDryingRackCrude() {

    super();

    this.interactionHandlers = new InteractionHandlerItemStack[]{
        new TileDryingRackCrude.InteractionHandler(new ItemStackHandler[]{this.stackHandler, this.outputStackHandler}, 0)
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

    if (this.renderBounds == null) {
      this.renderBounds = new AxisAlignedBB(this.getPos());
    }

    return this.renderBounds;
  }

  @Override
  public InteractionHandlerItemStack[] getInteractionHandlers() {

    return this.interactionHandlers;
  }

  public static class InteractionHandler
      extends InteractionHandlerItemStack {

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

}
