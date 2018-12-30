package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class InteractionBucketBase<T extends TileEntity & ITileInteractable>
    extends InteractionBase<T> {

  private final IFluidHandler fluidHandler;

  public InteractionBucketBase(IFluidHandler fluidHandler, EnumFacing[] sides, AxisAlignedBB bounds) {

    super(sides, bounds);
    this.fluidHandler = fluidHandler;
  }

  @Override
  public boolean interact(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (this.allowInteractionWithHand(hand)
        && this.allowInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ)) {

      return this.doInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ);
    }

    return false;
  }

  protected boolean allowInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    return (FluidUtil.getFluidHandler(player.getHeldItemMainhand()) != null);
  }

  protected boolean doInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    return FluidUtil.interactWithFluidHandler(player, hand, this.fluidHandler);
  }

}
