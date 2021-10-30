package com.codetaylor.mc.pyrotech.modules.ignition.tile.spi;

import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.athenaeum.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.ignition.block.spi.BlockTorchBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;

public abstract class TileTorchBase
    extends TileEntityBase
    implements ITileInteractable,
    ITickable {

  private int duration;
  private long lastTimeStamp;
  private final IInteraction<?>[] interactions;

  private boolean firstLightCheck;

  public TileTorchBase() {

    this.duration = (int) (this.getDuration() + (Math.random() * 2 - 1) * this.getDurationVariant());

    this.interactions = new IInteraction[]{
        new InteractionBucket(),
        new InteractionUseItemToActivate(Items.FLINT_AND_STEEL, EnumFacing.VALUES)
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public void activate() {

    if (!this.isLit()) {

      if (!this.shouldDouse()) {
        this.setLit();
      }
    }
  }

  private void setLit() {

    IBlockState blockState = this.world.getBlockState(this.pos);
    this.world.setBlockState(this.pos, blockState.withProperty(BlockTorchBase.TYPE, BlockTorchBase.EnumType.LIT));
  }

  private boolean isLit() {

    IBlockState blockState = this.world.getBlockState(this.pos);
    return blockState.getValue(BlockTorchBase.TYPE) == BlockTorchBase.EnumType.LIT;
  }

  private void setDoused() {

    IBlockState blockState = this.world.getBlockState(this.pos);
    this.world.setBlockState(this.pos, blockState.withProperty(BlockTorchBase.TYPE, BlockTorchBase.EnumType.DOUSED));
  }

  private boolean shouldDouse() {

    return this.isExtinguishedByRain()
        && this.world.isRainingAt(this.pos.up());
  }

  protected abstract boolean isExtinguishedByRain();

  protected abstract boolean shouldBurnUp();

  protected abstract int getDuration();

  protected abstract int getDurationVariant();

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (!this.firstLightCheck) {
      this.firstLightCheck = true;
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    }
  }

  public void randomUpdate() {

    // This update is called from the block's random update.
    // The idea is to reduce the tick time consumed by the torches since
    // it seems reasonable that many might be placed.

    if (this.isLit()) {

      if (this.shouldDouse()) {
        this.setDoused();

      } else if (this.shouldBurnUp()) {

        if (this.lastTimeStamp == 0) {
          this.lastTimeStamp = this.world.getTotalWorldTime();

        } else {
          long totalWorldTime = this.world.getTotalWorldTime();
          this.duration -= (totalWorldTime - this.lastTimeStamp);
          this.lastTimeStamp = totalWorldTime;
        }

        if (this.duration <= 0) {
          this.world.setBlockToAir(this.pos);
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setInteger("duration", this.duration);
    compound.setLong("lastTimeStamp", this.lastTimeStamp);
    return compound;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.duration = compound.getInteger("duration");
    this.lastTimeStamp = compound.getLong("lastTimeStamp");
    this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
  }

  @Override
  protected void setWorldCreate(@Nonnull World world) {

    // This is required to prevent NPE during readFromNBT.
    this.setWorld(world);
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction<?>[] getInteractions() {

    return this.interactions;
  }

  private static class InteractionBucket
      extends InteractionBucketBase<TileTorchBase> {

    /* package */ InteractionBucket() {

      super(new FluidTank(1000) {

        @Override
        public boolean canFillFluidType(FluidStack fluid) {

          return (fluid != null) && (fluid.getFluid() == FluidRegistry.WATER);
        }

        @Override
        public int fillInternal(FluidStack resource, boolean doFill) {

          int filled = super.fillInternal(resource, doFill);
          this.setFluid(null);
          return filled;
        }
      }, EnumFacing.VALUES, InteractionBounds.BLOCK);
    }

    @Override
    protected boolean doInteraction(TileTorchBase tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!tile.isLit()) {
        return false;
      }

      if (super.doInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ)) {
        tile.setDoused();

        if (!world.isRemote) {
          SoundHelper.playSoundServer(world, tile.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS);
        }

        // This causes the last timestamp to be reset the next time the torch is lit.
        tile.lastTimeStamp = 0;
        return true;
      }

      return false;
    }
  }

  public static class InteractionUseItemToActivate
      extends InteractionUseItemBase<TileTorchBase> {

    private final Item item;

    /* package */ InteractionUseItemToActivate(Item item, EnumFacing[] sides) {

      super(sides, InteractionBounds.BLOCK);
      this.item = item;
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      //noinspection deprecation
      return blockState.getBlock().getBoundingBox(blockState, world, pos);
    }

    @Override
    protected boolean allowInteraction(TileTorchBase tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (player.getHeldItem(hand).getItem() == this.item);
    }

    @Override
    protected boolean doInteraction(TileTorchBase tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {

        if (!tile.shouldDouse()) {
          tile.activate();

          world.playSound(
              null,
              hitPos,
              SoundEvents.ITEM_FLINTANDSTEEL_USE,
              SoundCategory.BLOCKS,
              1.0F,
              Util.RANDOM.nextFloat() * 0.4F + 0.8F
          );
        }
      }

      return true;
    }
  }
}
