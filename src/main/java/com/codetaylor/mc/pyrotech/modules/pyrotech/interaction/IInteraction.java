package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IInteraction<T extends TileEntity & ITileInteractable> {

  /**
   * Returns true if this interaction is allowed with the given hand.
   *
   * @param hand the hand to test against
   * @return true if this interaction is allowed with the given hand
   */
  boolean allowInteractionWithHand(EnumHand hand);

  /**
   * Returns true if the handler can be interacted with given the criteria.
   * <p>
   * Note that the given tilePos may be different than the hitPos. For example, if
   * the block above the TE is an interaction extension, the hitPos will
   * be for the block above the given tilePos.
   *
   * @param world          the world
   * @param hitSide        the side hit
   * @param hitPos         the position of the block hit
   * @param hitVec         the location of the hit relative to world origin
   * @param tilePos        the position of the TE, may be different than the hitPos
   * @param tileBlockState the blockState of the TE
   * @param tileFacing     the facing of the TE's block; default: NORTH
   * @return true if the handler can be interacted with given the criteria
   */
  boolean canInteractWith(World world, EnumFacing hitSide, BlockPos hitPos, Vec3d hitVec, BlockPos tilePos, IBlockState tileBlockState, EnumFacing tileFacing);

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
   *
   * @param world            the world
   * @param hitSide          the side hit
   * @param hitPos           the position of the block hit
   * @param hitVec           the location of the hit relative to world origin
   * @param pos              the position of the intersected TE, may be different than the hitPos
   * @param blockState       the blockState of the intersected TE
   * @param heldItemMainHand the item held in the player's main hand
   * @param partialTicks     value passed from the TESR
   */
  @SideOnly(Side.CLIENT)
  default void renderAdditivePass(World world, EnumFacing hitSide, BlockPos hitPos, Vec3d hitVec, BlockPos pos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {
    // default no op
  }
}
