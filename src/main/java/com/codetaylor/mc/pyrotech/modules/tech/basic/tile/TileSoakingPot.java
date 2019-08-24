package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.inventory.LargeObservableStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.library.util.ParticleHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleCombust;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockSoakingPot;
import com.codetaylor.mc.pyrotech.modules.tech.basic.client.render.SoakingPotFluidRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.SoakingPotRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSoakingPot
    extends TileNetBase
    implements ITileInteractable,
    ITickable {

  private final TileDataFluidTank<InputFluidTank> tileDataFluidTank;
  private final TileDataItemStackHandler<InputStackHandler> tileDataItemStackHandler;
  private InputFluidTank inputFluidTank;
  private InputStackHandler inputStackHandler;
  private OutputStackHandler outputStackHandler;
  private TileDataFloat recipeProgress;

  private SoakingPotRecipe currentRecipe;

  private IInteraction[] interactions;

  private boolean firstLightCheck = false;

  public TileSoakingPot() {

    super(ModuleTechBasic.TILE_DATA_SERVICE);

    this.inputFluidTank = new InputFluidTank(this);
    this.inputFluidTank.addObserver((tank, amount) -> {
      this.markDirty();
      this.updateRecipe();
      this.ejectItemOverfill();
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    });

    this.outputStackHandler = new OutputStackHandler();
    this.outputStackHandler.addObserver((handler, slot) -> this.markDirty());

    this.inputStackHandler = new InputStackHandler(this.inputFluidTank, this.outputStackHandler);
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.markDirty();
      this.updateRecipe();
    });

    this.recipeProgress = new TileDataFloat(0, 20);

    // --- Network

    this.tileDataFluidTank = new TileDataFluidTank<>(this.inputFluidTank);
    this.tileDataItemStackHandler = new TileDataItemStackHandler<>(this.inputStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataFluidTank,
        this.tileDataItemStackHandler,
        new TileDataLargeItemStackHandler<>(this.outputStackHandler),
        this.recipeProgress
    });

    // --- Interactions

    this.interactions = new IInteraction[]{
        new InteractionInputFluid(this.inputFluidTank),
        new InteractionItem(this, new ItemStackHandler[]{this.inputStackHandler, this.outputStackHandler})
    };
  }

  private void ejectItemOverfill() {

    if (this.currentRecipe != null
        && !this.world.isRemote) {
      int requiredFluidPerItem = this.currentRecipe.getInputFluid().amount;
      int existingItemCount = this.getInputStackHandler().getStackInSlot(0).getCount();
      int totalFluidRequired = requiredFluidPerItem * existingItemCount;

      int fluidAmount = this.getInputFluidTank().getFluidAmount();

      if (totalFluidRequired > fluidAmount) {
        int maxItemCount = fluidAmount / requiredFluidPerItem;

        if (maxItemCount < existingItemCount) {
          int toEject = existingItemCount - maxItemCount;
          ItemStack itemStack = this.getInputStackHandler().extractItem(0, toEject, false);
          StackHelper.spawnStackOnTop(this.world, itemStack, this.pos, 0.5);
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public SoakingPotRecipe getCurrentRecipe() {

    return this.currentRecipe;
  }

  public float getRecipeProgress() {

    if (this.currentRecipe == null) {
      return 0;
    }

    return this.recipeProgress.get();
  }

  public InputStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public InputFluidTank getInputFluidTank() {

    return this.inputFluidTank;
  }

  public OutputStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    if (!this.allowAutomation()) {
      return false;
    }

    return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        || (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (this.allowAutomation()) {

      if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

        if (facing == EnumFacing.DOWN) {

          //noinspection unchecked
          return (T) this.outputStackHandler;
        }

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

    return ModuleTechBasicConfig.SOAKING_POT.ALLOW_AUTOMATION;
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
  // - Recipe
  // ---------------------------------------------------------------------------

  private void updateRecipe() {

    // Note: This is called during the call to readFromNBT and the tile doesn't,
    // have access to the world object yet. Don't use the world here.

    ItemStack inputItem = this.inputStackHandler.getStackInSlot(0);
    FluidStack fluid = this.inputFluidTank.getFluid();

    if (inputItem.isEmpty() || fluid == null || fluid.amount == 0) {
      this.currentRecipe = null;

    } else {
      this.currentRecipe = SoakingPotRecipe.getRecipe(inputItem, fluid);
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (!this.firstLightCheck) {
      this.firstLightCheck = true;
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    }

    if (this.world.isRemote) {

      if (ModuleCoreConfig.CLIENT.SHOW_RECIPE_PROGRESSION_PARTICLES
          && this.currentRecipe != null
          && !this.getInputStackHandler().getStackInSlot(0).isEmpty()
          && this.world.getTotalWorldTime() % 40 == 0) {
        ParticleHelper.spawnProgressParticlesClient(
            1,
            this.pos.getX() + 0.5, this.pos.getY() + 0.75, this.pos.getZ() + 0.5,
            0.25, 0.25, 0.25
        );
      }
      return;
    }

    if (this.currentRecipe != null) {

      int timeTicks = Math.max(1, this.currentRecipe.getTimeTicks());
      float increment = 1.0f / timeTicks;

      ItemStack itemStack = this.inputStackHandler.getStackInSlot(0);
      int maxDrain = this.currentRecipe.getInputFluid().amount * itemStack.getCount();
      FluidStack drain = this.inputFluidTank.drain(maxDrain, false);

      if (drain == null || drain.amount != maxDrain) {
        return;
      }

      this.recipeProgress.add(increment);

      if (this.recipeProgress.get() >= 0.9999) {
        SoakingPotRecipe currentRecipe = this.currentRecipe;
        ItemStack inputItem = this.inputStackHandler.extractItem(0, this.inputStackHandler.getSlotLimit(0), false);
        this.inputFluidTank.drain(currentRecipe.getInputFluid().amount * inputItem.getCount(), true);
        ItemStack output = currentRecipe.getOutput();
        output.setCount(inputItem.getCount());
        this.outputStackHandler.insertItem(0, output, false);

        this.recipeProgress.set(0);
        this.updateRecipe();
      }

    } else {
      this.recipeProgress.set(0);
    }
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setTag("inputFluidTank", this.inputFluidTank.writeToNBT(new NBTTagCompound()));
    compound.setFloat("recipeProgress", this.recipeProgress.get());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.inputFluidTank.readFromNBT(compound.getCompoundTag("inputFluidTank"));
    this.recipeProgress.set(compound.getInteger("recipeProgress"));
    this.updateRecipe();
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBasicConfig.STAGES_SOAKING_POT;
  }

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class InteractionItem
      extends InteractionItemStack<TileSoakingPot> {

    private final TileSoakingPot tile;

    /* package */ InteractionItem(TileSoakingPot tile, ItemStackHandler[] stackHandlers) {

      super(stackHandlers, 0, new EnumFacing[]{EnumFacing.UP}, BlockSoakingPot.AABB, new Transform(
          Transform.translate(0.5, 0.5, 0.5),
          Transform.rotate(),
          Transform.scale(6.0 / 16.0, 6.0 / 16.0, 6.0 / 16.0)
      ));
      this.tile = tile;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      FluidStack fluid = this.tile.inputFluidTank.getFluid();

      if (fluid == null) {
        return false;
      }

      if (this.tile.inputStackHandler.insertItem(0, itemStack, true).getCount() == itemStack.getCount()) {
        return false;
      }

      return SoakingPotRecipe.getRecipe(itemStack, fluid) != null;
    }
  }

  public static class InteractionInputFluid
      extends InteractionBucketBase<TileSoakingPot> {

    private final FluidTank fluidTank;

    /* package */ InteractionInputFluid(FluidTank fluidTank) {

      super(fluidTank, new EnumFacing[]{EnumFacing.UP}, BlockSoakingPot.AABB);
      this.fluidTank = fluidTank;
    }

    public FluidTank getFluidTank() {

      return this.fluidTank;
    }

    @Override
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      SoakingPotFluidRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }
  }

  // ---------------------------------------------------------------------------
  // - Handlers
  // ---------------------------------------------------------------------------

  public static class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    private final InputFluidTank inputFluidTank;
    private final OutputStackHandler outputStackHandler;

    public InputStackHandler(InputFluidTank inputFluidTank, OutputStackHandler outputStackHandler) {

      super(1);
      this.inputFluidTank = inputFluidTank;
      this.outputStackHandler = outputStackHandler;
    }

    @Override
    public int getSlotLimit(int slot) {

      return ModuleTechBasicConfig.SOAKING_POT.MAX_STACK_SIZE;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      if (!this.outputStackHandler.getStackInSlot(0).isEmpty()) {
        return stack;
      }

      FluidStack fluid = this.inputFluidTank.getFluid();

      if (fluid == null
          || fluid.amount == 0) {
        return stack;
      }

      SoakingPotRecipe recipe = SoakingPotRecipe.getRecipe(stack, fluid);

      if (recipe == null) {
        return stack;
      }

      // Don't accept more items than there is fluid required to process them.

      int requiredFluidPerItem = recipe.getInputFluid().amount;
      int existingItemCount = this.getStackInSlot(0).getCount();

      if (fluid.amount < requiredFluidPerItem * existingItemCount) {
        return stack;
      }

      ItemStack remainingItems = super.insertItem(slot, stack, true);

      if (remainingItems.getCount() == stack.getCount()) {
        return stack;
      }

      int maxInsertQuantity = stack.getCount() - remainingItems.getCount();
      int actualInsertQuantity = 0;

      for (int i = 0; i < maxInsertQuantity; i++) {

        if (fluid.amount >= requiredFluidPerItem * (existingItemCount + i + 1)) {
          actualInsertQuantity += 1;

        } else {
          break;
        }
      }

      if (actualInsertQuantity == 0) {
        return stack;

      } else {
        ItemStack toInsert = stack.copy();
        toInsert.setCount(actualInsertQuantity);
        super.insertItem(slot, toInsert, simulate);

        ItemStack result = stack.copy();
        result.setCount(stack.getCount() - actualInsertQuantity);
        return result;
      }
    }
  }

  public static class OutputStackHandler
      extends LargeObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler() {

      super(1);
    }
  }

  public static class InputFluidTank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    private final TileSoakingPot tile;

    /* package */ InputFluidTank(TileSoakingPot tile) {

      super(ModuleTechBasicConfig.SOAKING_POT.MAX_FLUID_CAPACITY);
      this.tile = tile;
    }

    @Override
    public int fillInternal(FluidStack resource, boolean doFill) {

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
              ModuleStorage.PACKET_SERVICE.sendToAllAround(new SCPacketParticleCombust(pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, 0.2, 0.2, 0.2), this.tile);
            }
            world.checkLightFor(EnumSkyBlock.BLOCK, pos);
          }
        }
      }

      return filled;
    }
  }

  private int getHotFluidTemperature() {

    return ModuleTechBasicConfig.SOAKING_POT.HOT_TEMPERATURE;
  }

  private boolean canHoldHotFluids() {

    return ModuleTechBasicConfig.SOAKING_POT.HOLDS_HOT_FLUIDS;
  }
}
