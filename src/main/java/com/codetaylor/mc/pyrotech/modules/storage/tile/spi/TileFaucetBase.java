package com.codetaylor.mc.pyrotech.modules.storage.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataBoolean;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleCombust;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockFaucetBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class TileFaucetBase
    extends TileNetBase
    implements ITileInteractable,
    ITickable {

  private final Tank tank;
  private TileDataBoolean active;
  private int filled;

  private IInteraction[] interactions;

  protected TileFaucetBase() {

    super(ModuleStorage.TILE_DATA_SERVICE);

    this.tank = new Tank(this, 1000);
    this.tank.addObserver((fluidTank, amount) -> this.markDirty());

    this.active = new TileDataBoolean(false);

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataFluidTank<>(this.tank),
        this.active
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new Interaction()
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public void toggleActive() {

    this.setActive(!this.active.get());
  }

  public void setActive(boolean active) {

    this.filled = 0;
    this.active.set(active);
  }

  public boolean isActive() {

    return this.active.get();
  }

  public FluidStack getFluid() {

    return this.tank.getFluid();
  }

  protected abstract boolean canTransferHotFluids();

  protected abstract int getHotFluidTemperature();

  protected abstract int getFluidTransferMBPerTick();

  protected abstract int getFluidTransferLimit();

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.world.isRemote || !this.active.get()) {
      return;
    }

    if (this.filled == this.getFluidTransferLimit()) {
      this.setActive(false);
      return;
    }

    IBlockState blockState = this.world.getBlockState(this.pos);

    if (!(blockState.getBlock() instanceof BlockFaucetBase)) {
      this.active.set(false);
      return;
    }

    EnumFacing facing = blockState.getValue(Properties.FACING_HORIZONTAL);

    TileEntity sourceTile = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));

    if (sourceTile == null) {
      this.active.set(false);
      return;
    }

    IFluidHandler source = sourceTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);

    if (source == null) {
      this.active.set(false);
      return;
    }

    TileEntity targetTile = this.world.getTileEntity(this.pos.down());

    if (targetTile == null) {
      this.active.set(false);
      return;
    }

    IFluidHandler target = targetTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);

    if (target == null) {
      this.active.set(false);
      return;
    }

    int fluidTransferMBPerTick = this.getFluidTransferMBPerTick();

    FluidStack drained = source.drain(fluidTransferMBPerTick, false);

    if (drained == null || drained.amount == 0) {
      this.active.set(false);
      return;
    }

    int filled = target.fill(drained, false);

    if (filled == 0) {
      this.active.set(false);
      return;
    }

    this.tank.drain(Integer.MAX_VALUE, true);
    drained.amount = 1000;
    this.tank.fill(drained, true);
    this.filled += filled;

    target.fill(source.drain(filled, true), true);
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class Interaction
      implements IInteraction {

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      return InteractionBounds.BLOCK;
    }

    @Override
    public boolean allowInteractionWithSide(EnumFacing facing) {

      return true;
    }

    @Override
    public boolean interact(EnumType type, TileEntity tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote
          && tile instanceof TileFaucetBase) {
        ((TileFaucetBase) tile).toggleActive();
      }

      return true;
    }
  }

  // ---------------------------------------------------------------------------
  // - Tank
  // ---------------------------------------------------------------------------

  private class Tank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    private final TileFaucetBase tile;

    /* package */ Tank(TileFaucetBase tile, int capacity) {

      super(capacity);
      this.tile = tile;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

      int filled = super.fill(resource, doFill);

      if (!this.tile.canTransferHotFluids()) {
        World world = this.tile.world;
        BlockPos pos = this.tile.pos;

        if (resource != null) {
          Fluid fluid = resource.getFluid();

          if (fluid.getTemperature(resource) >= this.tile.getHotFluidTemperature()) {
            world.setBlockToAir(pos);
            SoundHelper.playSoundServer(world, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
            ModuleStorage.PACKET_SERVICE.sendToAllAround(new SCPacketParticleCombust(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.2, 0.2, 0.2), this.tile);
            world.checkLightFor(EnumSkyBlock.BLOCK, pos);
          }
        }
      }

      return filled;
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
