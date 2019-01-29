package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackCrudeRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileDryingRackBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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

  private IInteraction[] interactionHandlers;
  private AxisAlignedBB renderBounds;

  public TileDryingRackCrude() {

    super(1);

    this.interactionHandlers = new IInteraction[]{
        new TileDryingRackCrude.Interaction(
            new ItemStackHandler[]{this.inputStackHandler, this.outputStackHandler}
        )
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

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactionHandlers;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return blockState.getValue(BlockDryingRack.FACING);
  }

  private class Interaction
      extends InteractionItemStack<TileDryingRackCrude> {

    public Interaction(ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          EnumFacing.VALUES,
          BlockDryingRack.AABB_NORTH,
          new Transform(
              new Vec3d(0.5, 0.5, 0.15),
              new Quaternion(),
              new Vec3d(0.75, 0.75, 0.75)
          )
      );
    }

    @Override
    protected int getInsertionIndex(TileDryingRackCrude tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!this.stackHandlers[0].getStackInSlot(this.slot).isEmpty()) {
        // This will prevent an item being placed in handler 1 while handler 0 is occupied.
        return 0;

      } else if (!this.stackHandlers[1].getStackInSlot(this.slot).isEmpty()) {
        // This will prevent an item being placed in handler 0 while handler 1 is occupied.
        return 1;
      }

      return (DryingRackCrudeRecipe.getRecipe(player.getHeldItemMainhand()) != null) ? 0 : 1;
    }
  }

}
