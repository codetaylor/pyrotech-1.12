package com.codetaylor.mc.pyrotech.modules.ignition.tile.spi;

import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.ignition.block.BlockTorchFiber;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;

public abstract class TileTorchBase
    extends TileNetBase
    implements ITileInteractable {

  private TileDataInteger type;

  private int duration;
  private long lastTimeStamp;
  private IInteraction[] interactions;

  public TileTorchBase() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.type = new TileDataInteger(BlockTorchFiber.EnumType.UNLIT.getMeta());

    this.duration = (int) (this.getDuration() + (Math.random() * 2 - 1) * this.getDurationVariant());

    this.registerTileDataForNetwork(new ITileData[]{
        this.type
    });

    this.interactions = new IInteraction[]{
        new InteractionBucket(),
        new InteractionUseItemToActivate(Items.FLINT_AND_STEEL, EnumFacing.VALUES)
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public BlockTorchFiber.EnumType getType() {

    return BlockTorchFiber.EnumType.fromMeta(this.type.get());
  }

  public void activate() {

    IBlockState blockState = this.world.getBlockState(this.pos);

    if (blockState.getValue(BlockTorchFiber.TYPE) != BlockTorchFiber.EnumType.LIT) {

      if (!this.shouldDouse()) {
        this.type.set(BlockTorchFiber.EnumType.LIT.getMeta());
      }
    }
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
  public void onTileDataUpdate() {

    if (this.type.isDirty()) {
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
      this.world.checkLight(this.pos);
    }
  }

  public void update() {

    // This update is called from the block's random update.
    // The idea is to reduce the tick time consumed by the torches since
    // it seems reasonable that many might be placed.

    if (this.type.get() == BlockTorchFiber.EnumType.LIT.getMeta()) {

      if (this.shouldDouse()) {
        this.type.set(BlockTorchFiber.EnumType.DOUSED.getMeta());

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
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setInteger("type", this.type.get());
    compound.setInteger("duration", this.duration);
    compound.setLong("lastTimeStamp", this.lastTimeStamp);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.type.set(compound.getInteger("type"));
    this.duration = compound.getInteger("duration");
    this.lastTimeStamp = compound.getLong("lastTimeStamp");
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class InteractionBucket
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

      if (tile.type.get() != BlockTorchFiber.EnumType.LIT.getMeta()) {
        return false;
      }

      if (super.doInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ)) {
        tile.type.set(BlockTorchFiber.EnumType.DOUSED.getMeta());

        // This causes the last timestamp to be reset the next time the torch is lit.
        tile.lastTimeStamp = 0;
        return true;
      }

      return false;
    }
  }

  public class InteractionUseItemToActivate
      extends InteractionUseItemBase<TileTorchBase> {

    private final Item item;

    /* package */ InteractionUseItemToActivate(Item item, EnumFacing[] sides) {

      super(sides, InteractionBounds.BLOCK);
      this.item = item;
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

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
