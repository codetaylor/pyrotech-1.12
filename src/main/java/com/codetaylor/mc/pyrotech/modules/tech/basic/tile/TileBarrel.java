package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.ParticleHelper;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleCombust;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockBarrel;
import com.codetaylor.mc.pyrotech.modules.tech.basic.client.render.BarrelFluidRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.BarrelRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileBarrel
    extends TileEntityDataBase
    implements ITileInteractable,
    ITickable {

  private final InputFluidTank inputFluidTank;
  private final InputStackHandler inputStackHandler;
  private final LidStackHandler lidStackHandler;

  private final TileDataFluidTank<InputFluidTank> tileDataFluidTank;
  private final TileDataItemStackHandler<InputStackHandler> tileDataItemStackHandler;

  private final ItemStack[] inputItems;
  private final TileDataFloat recipeProgress;

  private BarrelRecipe currentRecipe;
  private int waterConversionTickCounter;
  private int waterFillTickCounter;

  private final IInteraction<?>[] interactions;

  public TileBarrel() {

    super(ModuleTechBasic.TILE_DATA_SERVICE);

    this.inputFluidTank = new InputFluidTank(this, Fluid.BUCKET_VOLUME);
    this.inputStackHandler = new InputStackHandler(this, 4);
    this.lidStackHandler = new LidStackHandler();
    this.recipeProgress = new TileDataFloat(0);
    this.inputItems = new ItemStack[4];

    this.inputFluidTank.addObserver((fluidTank, amount) -> {
      this.markDirty();
      this.updateRecipe();
    });

    this.inputStackHandler.addObserver((stackHandler, slotIndex) -> {
      this.markDirty();
      this.updateRecipe();
    });

    this.lidStackHandler.addObserver((stackHandler, slotIndex) -> {
      this.markDirty();

      if (!this.world.isRemote) {
        boolean hasLid = !stackHandler.getStackInSlot(slotIndex).isEmpty();

        if (hasLid) {
          BlockBarrel.setState(true, this.world, this.pos);

        } else {
          this.recipeProgress.set(0);
          BlockBarrel.setState(false, this.world, this.pos);
        }
      }
    });

    // --- Network

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataFluidTank = new TileDataFluidTank<>(this.inputFluidTank),
        this.tileDataItemStackHandler = new TileDataItemStackHandler<>(this.inputStackHandler),
        new TileDataItemStackHandler<>(this.lidStackHandler),
        this.recipeProgress
    });

    // --- Interaction

    List<IInteraction<?>> interactionList = new ArrayList<>();

    // Fluid interaction must be first to avoid item render issues.
    interactionList.add(new InteractionInputFluid(this, this.inputFluidTank));

    interactionList.add(new InteractionLid(this.lidStackHandler));

    for (int slot = 0; slot < 4; slot++) {
      int x = (slot % 2);
      int z = (slot / 2);
      interactionList.add(new InteractionInputItem(this, this.inputStackHandler, slot, x, z));
    }

    this.interactions = interactionList.toArray(new IInteraction[0]);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public BarrelRecipe getCurrentRecipe() {

    return this.currentRecipe;
  }

  public InputFluidTank getInputFluidTank() {

    return this.inputFluidTank;
  }

  public InputStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public LidStackHandler getLidStackHandler() {

    return this.lidStackHandler;
  }

  public float getRecipeProgress() {

    return this.recipeProgress.get();
  }

  protected int getHotFluidTemperature() {

    return 450;
  }

  protected boolean canHoldHotFluids() {

    return false;
  }

  protected boolean isSealed() {

    IBlockState blockState = this.world.getBlockState(this.pos);
    Block block = blockState.getBlock();
    return (block == ModuleTechBasic.Blocks.BARREL_SEALED);
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.tileDataFluidTank.isDirty()
        || this.tileDataItemStackHandler.isDirty()) {
      this.updateRecipe();
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (!this.isSealed()) {

      // If the barrel is not sealed and it is raining on top, periodically
      // check if we can add water to the barrel. Also periodically check if
      // we can transform the contained fluid into water.

      if (this.world.isRainingAt(this.pos.up())) {

        if (ModuleTechBasicConfig.BARREL.RAIN_WATER_FILL_DURATION_TICKS > 0) {
          this.waterFillTickCounter += 1;

          if (this.waterFillTickCounter >= ModuleTechBasicConfig.BARREL.RAIN_WATER_FILL_DURATION_TICKS) {
            this.waterFillTickCounter = 0;

            if (this.inputFluidTank.getFluidAmount() < Fluid.BUCKET_VOLUME) {
              FluidStack fluid = new FluidStack(FluidRegistry.WATER, 5);

              if (this.inputFluidTank.fill(fluid, false) > 0) {
                this.inputFluidTank.fill(fluid, true);
              }
            }
          }
        }

        if (ModuleTechBasicConfig.BARREL.RAIN_WATER_CONVERSION_DURATION_TICKS > 0) {
          this.waterConversionTickCounter += 1;

          if (this.waterConversionTickCounter >= ModuleTechBasicConfig.BARREL.RAIN_WATER_CONVERSION_DURATION_TICKS) {
            this.waterConversionTickCounter = 0;
            FluidStack fluidStack = this.inputFluidTank.getFluid();

            if (fluidStack != null && fluidStack.getFluid() != FluidRegistry.WATER) {
              int fluidAmount = this.inputFluidTank.getFluidAmount();
              this.inputFluidTank.drain(fluidAmount, true);
              this.inputFluidTank.fill(new FluidStack(FluidRegistry.WATER, fluidAmount), true);
            }
          }
        }

      } else {
        this.waterConversionTickCounter = 0;
      }

      return;
    }

    this.waterConversionTickCounter = 0;

    if (this.world.isRemote) {

      if (ModuleCoreConfig.CLIENT.SHOW_RECIPE_PROGRESSION_PARTICLES
          && this.currentRecipe != null
          && this.world.getTotalWorldTime() % 40 == 0) {

        ParticleHelper.spawnProgressParticlesClient(
            1,
            this.pos.getX() + 0.5, this.pos.getY() + 1.25, this.pos.getZ() + 0.5,
            0.5, 0.25, 0.5
        );
      }
      return;
    }

    if (this.currentRecipe != null) {

      int timeTicks = Math.max(1, this.currentRecipe.getTimeTicks());
      float increment = 1.0f / timeTicks;

      this.recipeProgress.add(increment);

      if (this.recipeProgress.get() >= 0.9999) {
        BarrelRecipe currentRecipe = this.currentRecipe;

        for (int i = 0; i < this.inputStackHandler.getSlots(); i++) {
          this.inputStackHandler.extractItem(i, this.inputStackHandler.getSlotLimit(0), false);
        }

        if (!currentRecipe.getOutput().isFluidEqual(this.inputFluidTank.getFluid())) {
          FluidStack outputCopy = currentRecipe.getOutput().copy();
          this.inputFluidTank.drain(this.inputFluidTank.getFluidAmount(), true);
          this.inputFluidTank.fill(outputCopy, true);
        }

        this.recipeProgress.set(0);
        this.updateRecipe();
      }

    } else {
      this.recipeProgress.set(0);
    }
  }

  private void updateRecipe() {

    for (int i = 0; i < this.inputStackHandler.getSlots(); i++) {
      this.inputItems[i] = this.inputStackHandler.getStackInSlot(i);
    }

    this.currentRecipe = BarrelRecipe.getRecipe(this.inputItems, this.inputFluidTank.getFluid());
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {

    return oldState.getBlock() != newState.getBlock();
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    if (!this.allowAutomation()
        || facing != EnumFacing.UP
        || this.isSealed()) {
      return false;
    }

    return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        || (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (this.allowAutomation()
        && facing == EnumFacing.UP
        && !this.isSealed()) {

      if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

        //noinspection unchecked
        return (T) this.inputStackHandler;

      } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {

        //noinspection unchecked
        return (T) this.inputFluidTank;
      }
    }

    return null;
  }

  protected boolean allowAutomation() {

    return ModuleTechBasicConfig.BARREL.ALLOW_AUTOMATION;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("inputFluidTank", this.inputFluidTank.writeToNBT(new NBTTagCompound()));
    compound.setTag("lidStackHandler", this.lidStackHandler.serializeNBT());
    compound.setFloat("recipeProgress", this.recipeProgress.get());
    return compound;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.inputFluidTank.readFromNBT(compound.getCompoundTag("inputFluidTank"));
    this.lidStackHandler.deserializeNBT(compound.getCompoundTag("lidStackHandler"));
    this.recipeProgress.set(compound.getFloat("recipeProgress"));
    this.updateRecipe();
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction<?>[] getInteractions() {

    return this.interactions;
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBasicConfig.STAGES_BARREL;
  }

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  private static class InteractionLid
      extends InteractionItemStack<TileBarrel> {

    /* package */ InteractionLid(ItemStackHandler stackHandler) {

      super(
          new ItemStackHandler[]{stackHandler},
          0,
          new EnumFacing[]{EnumFacing.UP},
          InteractionBounds.BLOCK,
          new Transform(
              Transform.translate(0.5, 14.0 / 16.0 + 0.05, 0.5),
              Transform.rotate(1, 0, 0, 90),
              Transform.scale()
          )
      );
    }

    @Override
    public boolean isItemStackValid(ItemStack itemStack) {

      return (itemStack.getItem() == ModuleTechBasic.Items.BARREL_LID);
    }
  }

  private static class InteractionInputItem
      extends InteractionItemStack<TileBarrel> {

    private final TileBarrel tile;

    /* package */ InteractionInputItem(TileBarrel tile, ItemStackHandler stackHandler, int slot, int x, int z) {

      super(
          new ItemStackHandler[]{stackHandler},
          slot,
          new EnumFacing[]{EnumFacing.UP},
          AABBHelper.create(
              (x == 0) ? 2 : 8, 12, (z == 0) ? 2 : 8,
              (x == 0) ? 8 : 14, 14, (z == 0) ? 8 : 14
          ),
          new Transform(
              Transform.translate((x == 0) ? 0.3125 : 0.6875, 14.0 / 16.0, (z == 0) ? 0.3125 : 0.6875),
              Transform.rotate(),
              Transform.scale(3.0 / 16.0, 3.0 / 16.0, 3.0 / 16.0)
          )
      );
      this.tile = tile;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      FluidStack fluid = this.tile.inputFluidTank.getFluid();

      if (fluid == null) {
        return false;
      }

      if (this.tile.inputStackHandler.insertItem(this.slot, itemStack, true).getCount() == itemStack.getCount()) {
        return false;
      }

      return BarrelRecipe.isValidItem(itemStack, fluid);
    }

    @Override
    public boolean isEmpty() {

      return super.isEmpty();
    }

    @Override
    public boolean isEnabled() {

      return !this.tile.isSealed();
    }

    @Override
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      if (!this.tile.isSealed()) {
        super.renderSolidPass(world, renderItem, pos, blockState, partialTicks);
      }
    }

    @Override
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      if (!this.tile.isSealed()) {
        return super.renderAdditivePass(world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
      }
      return false;
    }
  }

  public static class InteractionInputFluid
      extends InteractionBucketBase<TileBarrel> {

    private final TileBarrel tile;
    private final FluidTank fluidTank;

    /* package */ InteractionInputFluid(TileBarrel tile, FluidTank fluidTank) {

      super(fluidTank, new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK);
      this.tile = tile;
      this.fluidTank = fluidTank;
    }

    public FluidTank getFluidTank() {

      return this.fluidTank;
    }

    @Override
    public boolean isEnabled() {

      return !this.tile.isSealed();
    }

    @Override
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      BarrelFluidRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  public static class LidStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ LidStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }
  }

  public static class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    private final TileBarrel tile;

    public InputStackHandler(TileBarrel tile, int slots) {

      super(slots);
      this.tile = tile;
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      if (this.tile.isSealed()) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

  public static class InputFluidTank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    private final TileBarrel tile;

    /* package */ InputFluidTank(TileBarrel tile, int capacity) {

      super(capacity);
      this.tile = tile;
    }

    @Override
    public int fillInternal(FluidStack resource, boolean doFill) {

      if (this.tile.isSealed()) {
        return 0;
      }

      int filled = super.fillInternal(resource, doFill);

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
