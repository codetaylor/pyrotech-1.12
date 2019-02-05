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
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockSoakingPot;
import com.codetaylor.mc.pyrotech.modules.tech.basic.client.render.SoakingPotFluidRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.SoakingPotRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
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

  public TileSoakingPot() {

    super(ModuleTechBasic.TILE_DATA_SERVICE);

    this.inputFluidTank = new InputFluidTank();
    this.inputFluidTank.addObserver((tank, amount) -> {
      this.markDirty();
      this.updateRecipe();
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

    if (this.currentRecipe != null) {
      int maxDrain = this.currentRecipe.getInputFluid().amount * inputItem.getCount();
      FluidStack drain = this.inputFluidTank.drain(maxDrain, false);

      if (drain == null || drain.amount != maxDrain) {
        this.currentRecipe = null;
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    if (this.currentRecipe != null) {

      float increment = 1.0f / this.currentRecipe.getTimeTicks();
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

      if (!this.outputStackHandler.getStackInSlot(0).isEmpty()
          || this.inputFluidTank.getFluid() == null
          || this.inputFluidTank.getFluid().amount == 0) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
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

    /* package */ InputFluidTank() {

      super(ModuleTechBasicConfig.SOAKING_POT.MAX_FLUID_CAPACITY);
    }
  }
}
