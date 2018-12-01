package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IInteraction<T extends TileEntity & ITileInteractable> {

  default boolean isEnabled() {

    return true;
  }

  /**
   * Returns the bounds for this interaction.
   * Bounds are relative to block space.
   *
   * @param world      the world
   * @param pos        the position of the intersected block
   * @param blockState the blockState of the intersected block
   * @return the bounds for this interaction
   */
  AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState);

  /**
   * Returns true if this interaction should trigger with the given hand.
   * Override this to change the hand that this interaction works with.
   * <p>
   * By default it will trigger only on the main hand.
   *
   * @param hand the hand to filter this interaction by
   * @return true if this interaction should trigger with the given hand
   */
  default boolean allowInteractionWithHand(EnumHand hand) {

    return (hand == EnumHand.MAIN_HAND);
  }

  /**
   * Returns true if this interaction can be interacted with from the given
   * side.
   *
   * @param facing the block space facing
   * @return true if this interaction can be interacted with from the given side
   */
  boolean allowInteractionWithSide(EnumFacing facing);

  /**
   * Should be called from {@link net.minecraft.block.Block#onBlockActivated(World, BlockPos, IBlockState, EntityPlayer, EnumHand, EnumFacing, float, float, float)}.
   *
   * @param world   the world
   * @param hitPos  the blockPos of the block hit
   * @param state   the blockState of the block it
   * @param player  the player
   * @param hand    the player's hand used
   * @param hitSide the side of the block hit
   * @param hitX    the x position of the hit, relative to the hitPos
   * @param hitY    the y position of the hit, relative to the hitPos
   * @param hitZ    the z position of the hit, relative to the hitPos
   * @return true to prevent processing subsequent interactions
   */
  boolean interact(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ);

  /**
   * Render the solid pass.
   *
   * @param world        the world
   * @param renderItem   the renderItem instance
   * @param pos          the position of the TE
   * @param blockState   the blockState of the TE
   * @param partialTicks value passed from the TESR
   */
  @SideOnly(Side.CLIENT)
  default void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {
    // default no op
  }

  /**
   * Render the additive pass.
   *  @param world            the world
   * @param hitSide          the side hit
   * @param hitVec           the location of the hit relative to world origin
   * @param hitPos           the position of the block hit
   * @param blockState       the blockState of the intersected TE
   * @param heldItemMainHand the item held in the player's main hand
   * @param partialTicks     value passed from the TESR
   */
  @SideOnly(Side.CLIENT)
  default boolean renderAdditivePass(World world, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {
    // default no op
    return false;
  }
}
