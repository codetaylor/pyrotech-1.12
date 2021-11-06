package com.codetaylor.mc.pyrotech.modules.ignition.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnition;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import com.codetaylor.mc.pyrotech.modules.ignition.block.BlockLampOil;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileTankBase;
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
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileLampOil
    extends TileEntityDataBase
    implements ITileInteractable,
    ITickable {

  private final TileDataFluidTank<Tank> tileDataFluidTank;

  private final Tank tank;
  private final IInteraction<?>[] interactions;

  private float millibucketsUsed;

  public TileLampOil() {

    super(ModuleIgnition.TILE_DATA_SERVICE);

    this.tank = new Tank(this, this.getTankCapacity());

    // --- Network ---
    this.tileDataFluidTank = new TileDataFluidTank<>(this.tank);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataFluidTank
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionBucket(this.tank),
        new InteractionUseItemToActivate(Items.FLINT_AND_STEEL, EnumFacing.VALUES)
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public FluidTank getFluidTank() {

    return this.tank;
  }

  public boolean isActive() {

    return this.world.getBlockState(this.pos).getValue(BlockLampOil.LIT);
  }

  public void setActive(boolean active) {

    if (active && !this.isActive() && this.tank.getFluidAmount() > 0) {
      this.world.setBlockState(this.pos, ModuleIgnition.Blocks.LAMP_OIL.getDefaultState().withProperty(BlockLampOil.LIT, true));

    } else if (!active && this.isActive()) {
      this.world.setBlockState(this.pos, ModuleIgnition.Blocks.LAMP_OIL.getDefaultState().withProperty(BlockLampOil.LIT, false));
    }
  }

  protected int getTankCapacity() {

    return ModuleIgnitionConfig.OIL_LAMP.CAPACITY;
  }

  protected boolean allowFluid(FluidStack fluidStack) {

    if (fluidStack == null || fluidStack.getFluid() == null) {
      return false;
    }

    return ModuleIgnitionConfig.OIL_LAMP.ALLOWED_FUEL.containsKey(fluidStack.getFluid().getName());
  }

  protected int getMillibucketsPerMinute(FluidStack fluidStack) {

    if (fluidStack == null || fluidStack.getFluid() == null) {
      return 0;
    }

    return ModuleIgnitionConfig.OIL_LAMP.ALLOWED_FUEL.getOrDefault(fluidStack.getFluid().getName(), 0);
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.world.isRemote || !this.isActive()) {
      return;
    }

    if (this.tank.getFluidAmount() == 0) {
      this.setActive(false);
      this.millibucketsUsed = 0;

      this.world.playSound(
          null,
          this.pos,
          SoundEvents.BLOCK_FIRE_EXTINGUISH,
          SoundCategory.BLOCKS,
          1.0F,
          Util.RANDOM.nextFloat() * 0.4F + 0.8F
      );
      return;
    }

    this.millibucketsUsed += (this.getMillibucketsPerMinute(this.tank.getFluid()) / 1200f);

    int used = (int) Math.floor(this.millibucketsUsed);

    if (used > 0) {
      this.tank.drainInternal(used, true);
      this.millibucketsUsed -= used;
    }
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  protected void setWorldCreate(@Nonnull World world) {

    this.world = world;
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
    compound.setFloat("millibucketsUsed", this.millibucketsUsed);
    return compound;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.tank.readFromNBT(compound.getCompoundTag("tank"));
    this.millibucketsUsed = compound.getFloat("millibucketsUsed");
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction<?>[] getInteractions() {

    return this.interactions;
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleIgnitionConfig.STAGES_OIL_LAMP;
  }

  public static class InteractionBucket
      extends InteractionBucketBase<TileTankBase> {

    /* package */ InteractionBucket(IFluidHandler fluidHandler) {

      super(
          fluidHandler,
          EnumFacing.VALUES,
          InteractionBounds.BLOCK
      );
    }
  }

  public static class InteractionUseItemToActivate
      extends InteractionUseItemBase<TileLampOil> {

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
    protected boolean allowInteraction(TileLampOil tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (player.getHeldItem(hand).getItem() == this.item);
    }

    @Override
    protected boolean doInteraction(TileLampOil tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {

        tile.setActive(true);

        world.playSound(
            null,
            hitPos,
            SoundEvents.ITEM_FLINTANDSTEEL_USE,
            SoundCategory.BLOCKS,
            1.0F,
            Util.RANDOM.nextFloat() * 0.4F + 0.8F
        );
      }

      return true;
    }
  }

  // ---------------------------------------------------------------------------
  // - Tank
  // ---------------------------------------------------------------------------

  private static class Tank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    private final TileLampOil tile;

    /* package */ Tank(TileLampOil tile, int capacity) {

      super(capacity);
      this.tile = tile;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

      if (!this.tile.allowFluid(resource)) {
        return 0;
      }

      return super.fill(resource, doFill);
    }

    // Special serialization to bypass a bug where the ItemBlock merges an
    // empty tank with the full tank, adding the Empty tag to an otherwise
    // full tank.

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

      if (this.fluid != null) {
        this.fluid.writeToNBT(nbt);

      } else {
        nbt.setString("Empty", "");
      }
      return nbt;
    }

    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {

      if (nbt.hasKey("Empty")) {
        // We need to check if it actually is empty, because of the bug
        // mentioned above.

        if (!nbt.hasKey("Amount")
            || nbt.getInteger("Amount") <= 0) {
          this.setFluid(null);
          return this;
        }
      }

      FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
      this.setFluid(fluid);
      return this;
    }
  }
}
