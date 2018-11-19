package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackCrudeRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import net.minecraft.block.state.IBlockState;
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

  private IInteraction[] interactionHandlers;
  private AxisAlignedBB renderBounds;

  public TileDryingRackCrude() {

    super();

    this.interactionHandlers = new IInteraction[]{
        new InteractionItemStack<TileDryingRackCrude>(
            new ItemStackHandler[]{this.stackHandler, this.outputStackHandler},
            0,
            EnumFacing.VALUES,
            InteractionBounds.INFINITE,
            new Transform(
                new Vec3d(0.5, 0.5, 0.15),
                new Quaternion(),
                new Vec3d(0.75, 0.75, 0.75)
            ),
            (tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ) -> {

              Object recipe = null;

              if (state.getValue(BlockDryingRack.VARIANT) == BlockDryingRack.EnumType.CRUDE) {
                recipe = DryingRackCrudeRecipe.getRecipe(player.getHeldItemMainhand());

              } else if (state.getValue(BlockDryingRack.VARIANT) == BlockDryingRack.EnumType.NORMAL) {
                recipe = DryingRackRecipe.getRecipe(player.getHeldItemMainhand());
              }

              return (recipe == null) ? 1 : 0;
            }
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

}
