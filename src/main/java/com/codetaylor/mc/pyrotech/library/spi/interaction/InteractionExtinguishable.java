package com.codetaylor.mc.pyrotech.library.spi.interaction;

import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockCampfire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class InteractionExtinguishable<T extends TileEntity & ITileInteractable>
    extends InteractionBucketBase<T> {

  public interface Allow<T> {

    boolean apply(T tile);
  }

  public interface Action<T> {

    void apply(T tile);
  }

  private final Allow<T> allow;
  private final Action<T> action;
  private final boolean playSound;

  public InteractionExtinguishable(Allow<T> allow, Action<T> action) {

    this(allow, action, true);
  }

  public InteractionExtinguishable(Allow<T> allow, Action<T> action, boolean playSound) {

    super(new FluidTank(1000) {

      @Override
      public boolean canFillFluidType(FluidStack fluid) {

        return fluid != null
            && ArrayHelper.contains(ModuleCoreConfig.VALID_DOUSING_FLUIDS, fluid.getFluid().getName());
      }

      @Override
      public int fillInternal(FluidStack resource, boolean doFill) {

        int filled = super.fillInternal(resource, doFill);
        this.setFluid(null);
        return filled;
      }
    }, EnumFacing.VALUES, BlockCampfire.AABB_FULL);

    this.allow = allow;
    this.action = action;
    this.playSound = playSound;
  }

  @Override
  protected boolean allowInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    return super.allowInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ)
        && this.allow.apply(tile);
  }

  @Override
  protected boolean doInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (super.doInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ)) {
      this.action.apply(tile);

      if (this.playSound && !world.isRemote) {
        SoundHelper.playSoundServer(world, tile.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS);
      }

      return true;
    }

    return false;
  }
}