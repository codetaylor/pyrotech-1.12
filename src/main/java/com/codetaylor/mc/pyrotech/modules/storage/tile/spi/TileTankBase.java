package com.codetaylor.mc.pyrotech.modules.storage.tile.spi;

import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataBoolean;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleCombust;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.block.item.ItemBlockTank;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockTankBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TileTankBase
    extends TileEntityDataBase
    implements ITileInteractable,
    ITickable {

  private final TileDataFluidTank<Tank> tileDataFluidTank;
  private final TileDataBoolean tileDataConnectionStateUp;
  private final TileDataBoolean tileDataConnectionStateDown;
  private final Tank tank;
  private final FluidHandler fluidHandler;
  private final IInteraction[] interactions;
  private final List<TileTankBase> tankGroup;

  private boolean firstTickComplete = false;

  public TileTankBase() {

    super(ModuleStorage.TILE_DATA_SERVICE);

    this.tank = new Tank(this, this.getTankCapacity());
    this.tank.addObserver((fluidTank, amount) -> this.markDirty());
    this.fluidHandler = new FluidHandler(this);
    this.tankGroup = new ArrayList<>();

    // --- Network ---

    this.tileDataFluidTank = new TileDataFluidTank<>(this.tank);
    this.tileDataConnectionStateUp = new TileDataBoolean(false);
    this.tileDataConnectionStateDown = new TileDataBoolean(false);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataFluidTank,
        this.tileDataConnectionStateUp,
        this.tileDataConnectionStateDown
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionBucket(this.fluidHandler)
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public FluidTank getFluidTank() {

    return this.tank;
  }

  public BlockTankBase.EnumConnection getConnectionState() {

    if (this.tileDataConnectionStateUp.get() && this.tileDataConnectionStateDown.get()) {
      return BlockTankBase.EnumConnection.BOTH;

    } else if (this.tileDataConnectionStateUp.get()) {
      return BlockTankBase.EnumConnection.UP;

    } else if (this.tileDataConnectionStateDown.get()) {
      return BlockTankBase.EnumConnection.DOWN;

    } else {
      return BlockTankBase.EnumConnection.NONE;
    }
  }

  public boolean isConnectedUp() {

    BlockTankBase.EnumConnection connectionState = this.getConnectionState();
    return connectionState == BlockTankBase.EnumConnection.BOTH || connectionState == BlockTankBase.EnumConnection.UP;
  }

  public boolean isConnectedDown() {

    BlockTankBase.EnumConnection connectionState = this.getConnectionState();
    return connectionState == BlockTankBase.EnumConnection.BOTH || connectionState == BlockTankBase.EnumConnection.DOWN;
  }

  public void setTileDataConnectionStateUp(boolean connected) {

    this.tileDataConnectionStateUp.set(connected);
  }

  public void setTileDataConnectionStateDown(boolean connected) {

    this.tileDataConnectionStateDown.set(connected);
  }

  protected abstract int getTankCapacity();

  protected abstract boolean canHoldHotFluids();

  protected abstract int getHotFluidTemperature();

  // ---------------------------------------------------------------------------
  // - Render
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getRenderBoundingBox() {

    if (!this.isConnectedDown()) {
      return new AxisAlignedBB(
          this.pos.getX(), this.pos.getY(), this.pos.getZ(),
          this.pos.getX() + 1, this.pos.getY() + this.tankGroup.size(), this.pos.getZ() + 1
      );
    }

    return super.getRenderBoundingBox();
  }

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 1) && !this.isConnectedDown() && this.tank.getFluidAmount() > 0;
  }

  // ---------------------------------------------------------------------------
  // - Multiblock
  // ---------------------------------------------------------------------------

  private TileTankBase findLowestConnectedTank() {

    BlockPos.MutableBlockPos tankPos = new BlockPos.MutableBlockPos();
    Block blockToMatch = this.world.getBlockState(this.pos).getBlock();

    for (int y = this.pos.getY(); y >= 0; y--) {
      tankPos.setPos(this.pos.getX(), y, this.pos.getZ());
      TileEntity tileEntity = this.world.getTileEntity(tankPos);
      IBlockState blockState = this.world.getBlockState(tankPos);
      Block block = blockState.getBlock();

      if (tileEntity instanceof TileTankBase && block == blockToMatch) {
        TileTankBase tileTankBase = (TileTankBase) tileEntity;

        if (!tileTankBase.isConnectedDown()) {
          return tileTankBase;
        }
      }
    }

    return this;
  }

  public void updateTankGroups() {

    ArrayList<TileTankBase> tankGroup = new ArrayList<>();

    TileTankBase tank = this.findLowestConnectedTank();

    if (tank != null) {

      do {
        tankGroup.add(tank);
        tank = (TileTankBase) this.world.getTileEntity(tank.pos.up());
      } while (tank != null && tank.isConnectedDown());
    }

    for (TileTankBase tileTankBase : tankGroup) {
      tileTankBase.tankGroup.clear();
      tileTankBase.tankGroup.addAll(tankGroup);
    }
  }

  public List<TileTankBase> getTankGroup() {

    return this.tankGroup;
  }

  public int getActualFluidAmount() {

    int result = 0;
    List<TileTankBase> tankGroup = this.getTankGroup();

    for (TileTankBase tileTankBase : tankGroup) {
      result += tileTankBase.getFluidTank().getFluidAmount();
    }

    return result;
  }

  public int getActualFluidCapacity() {

    List<TileTankBase> tankGroup = this.getTankGroup();
    return tankGroup.size() * this.getTankCapacity();
  }

  public void updateConnectionsForPlacement(@Nullable EnumFacing side) {

    if (this.world.isRemote) {
      return;
    }

    FluidStack fluidToMatch = this.tank.getFluid();
    FluidStack fluidBelow = this.findNearestTankFluidBelow(this.pos);

    if (EnumFacing.DOWN == side) {

      if (fluidToMatch == null) { // we're empty
        this.tryConnectUp();

        // connected tanks below are empty, or
        // fluid below is the same as fluid above
        if (fluidBelow == null
            || fluidBelow.isFluidEqual(this.getTankFluidAbove(this.pos))) {
          this.tryConnectDown();
        }
      } else { // we're not empty

        // our fluid is the same as fluid above
        if (fluidToMatch.isFluidEqual(this.getTankFluidAbove(this.pos))) {
          this.tryConnectUp();
        }

        // connected tanks below are empty, or
        // our fluid is the same as the fluid below
        if (fluidBelow == null
            || fluidToMatch.isFluidEqual(fluidBelow)) {
          this.tryConnectDown();
        }
      }

    } else {

      if (fluidToMatch == null) { // we're empty
        this.tryConnectDown();

        // connected tanks below are empty, or
        // fluid below is the same as fluid above
        if (fluidBelow == null
            || fluidBelow.isFluidEqual(this.getTankFluidAbove(this.pos))) {
          this.tryConnectUp();
        }

      } else { // we're not empty

        // connected tanks below are empty, or
        // our fluid is the same as the fluid below
        if (fluidBelow == null
            || fluidToMatch.isFluidEqual(fluidBelow)) {
          this.tryConnectDown();
        }

        // our fluid is the same as fluid above
        if (fluidToMatch.isFluidEqual(this.getTankFluidAbove(this.pos))) {
          this.tryConnectUp();
        }
      }
    }
  }

  private void tryConnectDown() {

    if (this.world.getBlockState(this.pos) != this.world.getBlockState(this.pos.down())) {
      return;
    }

    TileEntity tileEntity = this.world.getTileEntity(this.pos.down());

    if (tileEntity instanceof TileTankBase) {
      this.setTileDataConnectionStateDown(true);
      ((TileTankBase) tileEntity).setTileDataConnectionStateUp(true);
    }
  }

  private void tryConnectUp() {

    if (this.world.getBlockState(this.pos) != this.world.getBlockState(this.pos.up())) {
      return;
    }

    TileEntity tileEntity = this.world.getTileEntity(this.pos.up());

    if (tileEntity instanceof TileTankBase) {
      this.setTileDataConnectionStateUp(true);
      ((TileTankBase) tileEntity).setTileDataConnectionStateDown(true);
    }
  }

  @Nullable
  private FluidStack getTankFluidAbove(BlockPos pos) {

    FluidStack result = null;
    TileEntity tileEntity = this.world.getTileEntity(pos.up());

    if (tileEntity instanceof TileTankBase) {
      TileTankBase tile = (TileTankBase) tileEntity;
      result = tile.tank.getFluid();
    }

    return result;
  }

  @Nullable
  private FluidStack findNearestTankFluidBelow(BlockPos pos) {

    FluidStack result = null;
    BlockPos nextPos = pos;

    for (int y = pos.getY() - 1; y >= 0; y--) {
      nextPos = nextPos.down();
      TileEntity tileEntity = this.world.getTileEntity(nextPos);

      if (!(tileEntity instanceof TileTankBase)) {
        break;
      }

      TileTankBase tile = (TileTankBase) tileEntity;
      result = tile.tank.getFluid();

      if (result != null) {
        break;
      }

      if (!tile.isConnectedDown()) {
        break;
      }
    }

    return result;
  }

  public void updateConnectionsForNeighborChanged() {

    if (this.world.isRemote) {
      return;
    }

    {
      TileEntity tileEntity = this.world.getTileEntity(this.pos.up());

      if (!(tileEntity instanceof TileTankBase)) {
        this.setTileDataConnectionStateUp(false);
      }
    }

    {
      TileEntity tileEntity = this.world.getTileEntity(this.pos.down());

      if (!(tileEntity instanceof TileTankBase)) {
        this.setTileDataConnectionStateDown(false);
      }
    }
  }

  public void settleFluids() {

    if (this.world.isRemote) {
      return;
    }

    List<TileTankBase> tankGroup = this.getTankGroup();

    // walk down until we find a tank with fluid (A)
    // walk up and attempt to fill tanks below

    for (int drainIndex = tankGroup.size() - 1; drainIndex >= 0; drainIndex--) {
      TileTankBase toDrain = tankGroup.get(drainIndex);
      int fluidAmount = toDrain.tank.getFluidAmount();

      if (fluidAmount == 0) {
        continue;
      }

      for (int fillIndex = 0; fillIndex < drainIndex; fillIndex++) {
        TileTankBase toFill = tankGroup.get(fillIndex);

        if (toFill.tank.getFluidAmount() == toFill.tank.getCapacity()) {
          continue;
        }

        FluidStack drain = toDrain.tank.drain(fluidAmount, false);
        int filled = toFill.tank.fill(drain, false);

        if (filled > 0) {
          toFill.tank.fill(toDrain.tank.drain(filled, true), true);
        }

        if (toDrain.tank.getCapacity() == 0) {
          break;
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (!this.firstTickComplete) {
      this.firstTickComplete = true;
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
      this.updateTankGroups();
    }
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.tileDataFluidTank.isDirty()) {
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    }

    if (this.tileDataConnectionStateUp.isDirty()
        || this.tileDataConnectionStateDown.isDirty()) {
      this.updateTankGroups();
    }
  }

  // ---------------------------------------------------------------------------
  // - Capability
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.fluidHandler;
    }

    return null;
  }

  private static class FluidHandler
      implements IFluidHandler {

    private final TileTankBase tile;

    private FluidHandler(TileTankBase tile) {

      this.tile = tile;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {

      return new IFluidTankProperties[0];
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

      FluidStack resourceCopy = resource.copy();
      List<TileTankBase> tankGroup = this.tile.getTankGroup();

      if (tankGroup.size() > 0) {
        // Prevent filling with two different liquids
        TileTankBase tileTankBase = tankGroup.get(0);
        FluidStack otherFluidStack = tileTankBase.tank.getFluid();

        if (otherFluidStack != null && !resource.isFluidEqual(otherFluidStack)) {
          return 0;
        }
      }

      for (TileTankBase tileTankBase : tankGroup) {
        FluidTank fluidTank = tileTankBase.getFluidTank();
        resourceCopy.amount -= fluidTank.fill(resourceCopy, doFill);

        if (resourceCopy.amount <= 0) {
          break;
        }
      }

      return resource.amount - resourceCopy.amount;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {

      FluidStack result = null;
      FluidStack resourceCopy = resource.copy();

      List<TileTankBase> tankGroup = this.tile.getTankGroup();

      for (int i = tankGroup.size() - 1; i >= 0; i--) {
        TileTankBase tileTankBase = tankGroup.get(i);
        FluidTank fluidTank = tileTankBase.getFluidTank();
        FluidStack drained = fluidTank.drain(resourceCopy, doDrain);

        if (drained != null) {

          if (result == null) {
            result = drained;

          } else {
            result.amount += drained.amount;
          }

          resourceCopy.amount -= drained.amount;
        }

        if (resourceCopy.amount <= 0) {
          break;
        }
      }

      return result;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {

      FluidStack result = null;
      List<TileTankBase> tankGroup = this.tile.getTankGroup();

      for (int i = tankGroup.size() - 1; i >= 0; i--) {
        TileTankBase tileTankBase = tankGroup.get(i);
        FluidTank fluidTank = tileTankBase.getFluidTank();
        FluidStack drained = fluidTank.drain(maxDrain, doDrain);

        if (drained != null) {

          if (result == null) {
            result = drained;

          } else {
            result.amount += drained.amount;
          }

          maxDrain -= drained.amount;
        }

        if (maxDrain <= 0) {
          break;
        }
      }

      return result;
    }
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  protected void setWorldCreate(World world) {

    this.world = world;
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
    compound.setBoolean("connectionUp", this.tileDataConnectionStateUp.get());
    compound.setBoolean("connectionDown", this.tileDataConnectionStateDown.get());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.tank.readFromNBT(compound.getCompoundTag("tank"));
    this.tileDataConnectionStateUp.set(compound.getBoolean("connectionUp"));
    this.tileDataConnectionStateDown.set(compound.getBoolean("connectionDown"));
  }

  public void writeToItem(@Nonnull ItemStack itemStack) {

    ItemBlockTank.writeTank(this.tank, itemStack);
  }

  public void readFromItem(@Nonnull ItemStack itemStack) {

    ItemBlockTank.readTank(this.tank, itemStack);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  public class InteractionBucket
      extends InteractionBucketBase<TileTankBase> {

    /* package */ InteractionBucket(IFluidHandler fluidHandler) {

      super(
          fluidHandler,
          EnumFacing.VALUES,
          InteractionBounds.BLOCK
      );
    }
  }

  // ---------------------------------------------------------------------------
  // - Tank
  // ---------------------------------------------------------------------------

  private class Tank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    private final TileTankBase tile;

    /* package */ Tank(TileTankBase tile, int capacity) {

      super(capacity);
      this.tile = tile;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

      int filled = super.fill(resource, doFill);

      if (!this.tile.canHoldHotFluids()) {
        World world = this.tile.world;
        BlockPos pos = this.tile.pos;

        if (resource != null) {
          Fluid fluid = resource.getFluid();

          if (fluid.getTemperature(resource) >= this.tile.getHotFluidTemperature()) {

            if (!world.isRemote) {
              world.setBlockToAir(pos);
              SoundHelper.playSoundServer(world, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
              FluidUtil.tryPlaceFluid(null, world, pos, this, resource);
              ModuleStorage.PACKET_SERVICE.sendToAllAround(new SCPacketParticleCombust(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.5, 0.5, 0.5), this.tile);
            }
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
