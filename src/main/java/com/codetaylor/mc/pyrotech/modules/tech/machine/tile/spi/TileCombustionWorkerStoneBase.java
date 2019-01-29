package com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockCombustionWorkerStoneBase;
import com.codetaylor.mc.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeBase;
import com.codetaylor.mc.pyrotech.library.spi.tile.ITileContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileCombustionWorkerStoneBase<E extends StoneMachineRecipeBase<E>>
    extends TileCombustionWorkerBase
    implements ITickable,
    ITileInteractable,
    ITileContainer {

  private static final int DORMANT_COUNTER = 50;
  private static final AxisAlignedBB INTERACTION_BOUNDS_TOP = new AxisAlignedBB(1f / 16f, 1, 1f / 16f, 15f / 16f, 24f / 16f, 15f / 16f);

  private FuelStackHandler fuelStackHandler;

  private TileDataInteger remainingRecipeTimeTicks;

  private int dormantCounter;

  private IInteraction[] interactions;

  public TileCombustionWorkerStoneBase() {

    // --- Init ---

    super(ModulePyrotech.TILE_DATA_SERVICE, 1);

    this.resetDormantCounter();

    // --- Stack Handlers ---

    this.fuelStackHandler = new FuelStackHandler(1);
    this.fuelStackHandler.addObserver((handler, slot) -> {
      // force the burn time to update with the fuel change
      this.burnTimeRemaining.forceUpdate();
      this.markDirty();
    });

    // --- Network ---

    this.remainingRecipeTimeTicks = new TileDataInteger(0, 20);

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.fuelStackHandler),
        this.remainingRecipeTimeTicks
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionBucket(),
        new InteractionUseFlintAndSteel(),
        new InteractionFuel(new ItemStackHandler[]{
            TileCombustionWorkerStoneBase.this.fuelStackHandler
        })
    };
  }

  public abstract E getRecipe(ItemStack itemStack);

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    super.onTileDataUpdate();
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public ItemStackHandler getFuelStackHandler() {

    return this.fuelStackHandler;
  }

  public int getRemainingRecipeTimeTicks() {

    return this.remainingRecipeTimeTicks.get();
  }

  public void setRemainingRecipeTimeTicks(int value) {

    this.remainingRecipeTimeTicks.set(value);

    // TODO: Pretty sure this isn't necessary anymore.
    if (this.remainingRecipeTimeTicks.isDirty()) {
      this.markDirty();
    }
  }

  public boolean hasFuel() {

    return this.combustionGetBurnTimeRemaining() > 0
        || !this.fuelStackHandler.getStackInSlot(0).isEmpty();
  }

  protected void resetDormantCounter() {

    this.dormantCounter = DORMANT_COUNTER;
  }

  protected void addInteractions(IInteraction[] interactions) {

    this.interactions = ArrayHelper.combine(this.interactions, interactions);
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (facing != null
        && facing.getAxis().isHorizontal()
        && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

      if (facing != null && facing.getAxis().isHorizontal()) {
        //noinspection unchecked
        return (T) this.fuelStackHandler;
      }
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Combustion Worker
  // ---------------------------------------------------------------------------

  @Override
  protected ItemStack combustionGetFuelItem() {

    return this.getFuelStackHandler().extractItem(0, 1, false);
  }

  // ---------------------------------------------------------------------------
  // - Worker
  // ---------------------------------------------------------------------------

  @Override
  public void workerSetActive(boolean active) {

    IBlockState blockState = this.world.getBlockState(this.pos);

    if (active && !super.workerIsActive()) {

      if (this.hasFuel()) {
        blockState = blockState.withProperty(BlockCombustionWorkerStoneBase.TYPE, BlockCombustionWorkerStoneBase.EnumType.BottomLit);
        this.world.setBlockState(this.pos, blockState, 3);
        super.workerSetActive(true);
      }

    } else if (!active && super.workerIsActive()) {
      blockState = blockState.withProperty(BlockCombustionWorkerStoneBase.TYPE, BlockCombustionWorkerStoneBase.EnumType.Bottom);
      this.world.setBlockState(this.pos, blockState, 3);
      super.workerSetActive(false);
    }
  }

  @Override
  public boolean workerDoWork() {

    if (!super.workerDoWork()) {
      return false;
    }

    if (this.hasFuel()
        && this.hasInput()) {
      this.resetDormantCounter();

    } else if (this.shouldKeepHeat()
        && this.dormantCounter > 0) {

      this.dormantCounter -= 1;
    }

    if (this.dormantCounter == 0) {
      return false;
    }

    if (this.getRemainingRecipeTimeTicks() > 0) {

      this.reduceRecipeTime();

      if (this.getRemainingRecipeTimeTicks() == 0) {
        this.onRecipeComplete();
      }
    }

    return true;
  }

  protected void reduceRecipeTime() {

    this.setRemainingRecipeTimeTicks(this.getRemainingRecipeTimeTicks() - 1);
  }

  public abstract boolean hasInput();

  public abstract boolean allowInsertInput(ItemStack stack, E recipe);

  protected abstract boolean shouldKeepHeat();

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  protected abstract void onRecipeComplete();

  protected void recalculateRemainingTime(ItemStack itemStack) {
    // Recalculate remaining recipe time.

    if (itemStack.isEmpty()) {
      this.setRemainingRecipeTimeTicks(0);

    } else {
      StoneMachineRecipeBase<E> recipe = this.getRecipe(itemStack);

      if (recipe != null) {
        this.setRemainingRecipeTimeTicks(recipe.getTimeTicks());

      } else {
        this.setRemainingRecipeTimeTicks(0);
      }
    }

    this.resetDormantCounter();
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("fuelStackHandler", this.fuelStackHandler.serializeNBT());
    compound.setInteger("remainingRecipeTimeTicks", this.remainingRecipeTimeTicks.get());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.fuelStackHandler.deserializeNBT(compound.getCompoundTag("fuelStackHandler"));
    this.remainingRecipeTimeTicks.set(compound.getInteger("remainingRecipeTimeTicks"));
  }

  @Override
  public void dropContents() {

    ItemStackHandler stackHandler = this.getFuelStackHandler();
    ItemStack itemStack = stackHandler.extractItem(0, stackHandler.getStackInSlot(0).getCount(), false);

    if (!itemStack.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
    }

    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  @Override
  public boolean shouldRefresh(
      World world,
      BlockPos pos,
      @Nonnull IBlockState oldState,
      @Nonnull IBlockState newState
  ) {

    if (oldState.getBlock() == newState.getBlock()) {
      return false;
    }

    return super.shouldRefresh(world, pos, oldState, newState);
  }

  // ---------------------------------------------------------------------------
  // - Render
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getRenderBoundingBox() {

    return super.getRenderBoundingBox().expand(0, 1, 0);
  }

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return blockState.getValue(Properties.FACING_HORIZONTAL);
  }

  @Override
  public boolean isExtendedInteraction(World world, BlockPos pos, IBlockState blockState) {

    BlockPos blockPos = this.getPos();

    return blockPos.getX() == pos.getX()
        && blockPos.getY() + 1 == pos.getY()
        && blockPos.getZ() == pos.getZ();
  }

  protected EnumFacing[] getInputInteractionSides() {

    return new EnumFacing[]{EnumFacing.NORTH};
  }

  protected AxisAlignedBB getInputInteractionBoundsTop() {

    return INTERACTION_BOUNDS_TOP;
  }

  private class InteractionBucket
      extends InteractionBucketBase<TileCombustionWorkerStoneBase> {

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
      }, new EnumFacing[]{EnumFacing.NORTH}, InteractionBounds.BLOCK);
    }

    @Override
    protected boolean doInteraction(TileCombustionWorkerStoneBase tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!tile.workerIsActive()) {
        return false;
      }

      if (super.doInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ)) {
        tile.combustionOnDeactivatedByRain();
        tile.workerSetActive(false);
        return true;
      }

      return false;
    }
  }

  private class InteractionFuel
      extends InteractionItemStack<TileCombustionWorkerStoneBase> {

    /* package */ InteractionFuel(ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          new EnumFacing[]{EnumFacing.NORTH},
          InteractionBounds.BLOCK,
          new Transform(
              Transform.translate(0.5, 0.2, 0.5),
              Transform.rotate(),
              Transform.scale(0.5, 0.5, 0.5)
          )
      );
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return StackHelper.isFuel(itemStack)
          && !itemStack.getItem().hasContainerItem(itemStack);
    }
  }

  private class InteractionUseFlintAndSteel
      extends InteractionUseItemBase<TileCombustionWorkerStoneBase> {

    /* package */ InteractionUseFlintAndSteel() {

      super(new EnumFacing[]{EnumFacing.NORTH}, InteractionBounds.BLOCK);
    }

    @Override
    protected boolean allowInteraction(TileCombustionWorkerStoneBase tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL);
    }

    @Override
    protected boolean doInteraction(TileCombustionWorkerStoneBase tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {
        tile.workerSetActive(true);

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
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class FuelStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ FuelStackHandler(int size) {

      super(size);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return MathHelper.clamp(getFuelSlotSize(), 1, 64);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // Filter out non-fuel items.

      if (!StackHelper.isFuel(stack)
          || stack.getItem().hasContainerItem(stack)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }

  }

  protected abstract int getFuelSlotSize();
}
