package com.codetaylor.mc.pyrotech.interaction.spi;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public abstract class InteractionBase<T extends TileEntity & ITileInteractable>
    implements IInteraction<T> {

  protected final int sides;
  protected final AxisAlignedBB bounds;

  public InteractionBase(EnumFacing[] sides, AxisAlignedBB bounds) {

    this.bounds = bounds;

    int sidesEncoded = 0;

    for (EnumFacing side : sides) {
      sidesEncoded |= (1 << side.getIndex());
    }

    this.sides = sidesEncoded;
  }

  @Override
  public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

    return this.bounds;
  }

  @Override
  public boolean allowInteractionWithSide(EnumFacing facing) {

    return ((this.sides & (1 << facing.getIndex())) == (1 << facing.getIndex()));
  }

  /**
   * Override for more control over the additive render pass.
   *
   * @param sneaking         is the player sneaking
   * @param heldItemMainHand the player's main hand item
   * @return true if the interaction's item should be rendered in the interaction
   */
  @SideOnly(Side.CLIENT)
  public boolean shouldRenderAdditivePassForStackInSlot(boolean sneaking, ItemStack heldItemMainHand) {

    return heldItemMainHand.isEmpty();
  }
}
