package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.util.QuaternionHelper;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.util.vector.Quaternion;

public class TileDryingRack
    extends TileDryingRackBase
    implements ITileInteractable {

  private IInteraction[] interactions;

  public TileDryingRack() {

    super();

    this.interactions = new IInteraction[4];

    for (int slot = 0; slot < 4; slot++) {

      final int qX = (slot & 1);
      final int qZ = ((slot >> 1) & 1);

      this.interactions[slot] = new InteractionItemStack<TileDryingRack>(
          new ItemStackHandler[]{
              this.stackHandler,
              this.outputStackHandler
          },
          slot,
          new EnumFacing[]{EnumFacing.UP},
          new InteractionBounds(qX * 0.5, qZ * 0.5, qX * 0.5 + 0.5, qZ * 0.5 + 0.5),
          new Transform(
              new Vec3d(
                  qX * 0.375 + 0.25 + 0.0625,
                  0.75 + 0.03125,
                  qZ * 0.375 + 0.25 + 0.0625
              ),
              QuaternionHelper.setFromAxisAngle(new Quaternion(), 1, 0, 0, (float) (Math.PI / 2) /* 90 */),
              new Vec3d(0.25, 0.25, 0.25)
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
      );

    }

  }

  @Override
  protected int getSlotCount() {

    return 4;
  }

  @Override
  protected float getSpeedModified(float speed) {

    return (float) (speed * ModulePyrotechConfig.DRYING_RACK.SPEED_MODIFIER);
  }

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return blockState.getValue(BlockDryingRack.FACING);
  }
}
