package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockTorchFiber;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetBase;
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

import javax.annotation.Nonnull;

public class TileTorch
    extends TileNetBase
    implements ITileInteractable {

  private TileDataInteger type;

  private int duration;
  private long lastTimeStamp;
  private IInteraction[] interactions;

  public TileTorch() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.type = new TileDataInteger(BlockTorchFiber.EnumType.UNLIT.getMeta());

    this.duration = (int) (ModulePyrotechConfig.TORCH.DURATION + (Math.random() * 2 - 1) * ModulePyrotechConfig.TORCH.DURATION_VARIANT);

    this.registerTileDataForNetwork(new ITileData[]{
        this.type
    });

    this.interactions = new IInteraction[]{
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

    return ModulePyrotechConfig.TORCH.EXTINGUISHED_BY_RAIN
        && this.world.isRainingAt(this.pos.up());
  }

  private boolean shouldBurnUp() {

    return ModulePyrotechConfig.TORCH.BURNS_UP;
  }

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

  public class InteractionUseItemToActivate
      extends InteractionUseItemBase<TileTorch> {

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
    protected boolean allowInteraction(TileTorch tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (player.getHeldItem(hand).getItem() == this.item);
    }

    @Override
    protected boolean doInteraction(TileTorch tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

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
