package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.inventory.LargeObservableStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileSoakingPot
    extends TileNetBase
    implements ITileInteractable,
    ITickable {

  private final TileDataFluidTank<InputFluidTank> tileDataFluidTank;
  private final TileDataItemStackHandler<InputStackHandler> tileDataItemStackHandler;
  private InputFluidTank inputFluidTank;
  private InputStackHandler inputStackHandler;
  private OutputStackHandler outputStackHandler;
  private TileDataInteger remainingRecipeTimeTicks;

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

    this.remainingRecipeTimeTicks = new TileDataInteger(0, 20);

    // --- Network

    this.tileDataFluidTank = new TileDataFluidTank<>(this.inputFluidTank);
    this.tileDataItemStackHandler = new TileDataItemStackHandler<>(this.inputStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataFluidTank,
        this.tileDataItemStackHandler,
        new TileDataLargeItemStackHandler<>(this.outputStackHandler),
        this.remainingRecipeTimeTicks
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

    return 1 - this.remainingRecipeTimeTicks.get() / (float) this.currentRecipe.getTimeTicks();
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

    ItemStack inputItem = this.inputStackHandler.getStackInSlot(0);
    FluidStack fluid = this.inputFluidTank.getFluid();

    if (inputItem.isEmpty() || fluid == null || fluid.amount == 0) {
      this.currentRecipe = null;

    } else {
      this.currentRecipe = SoakingPotRecipe.getRecipe(inputItem, fluid);
    }

    if (this.currentRecipe != null) {

      int maxDrain = currentRecipe.getInputFluid().amount * inputItem.getCount();
      FluidStack drain = this.inputFluidTank.drain(maxDrain, false);

      if (drain != null && drain.amount == maxDrain) {
        this.remainingRecipeTimeTicks.set(this.currentRecipe.getTimeTicks());

      } else {
        this.currentRecipe = null;
        this.remainingRecipeTimeTicks.set(0);
      }

    } else {
      this.remainingRecipeTimeTicks.set(0);
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.currentRecipe != null) {

      if (this.remainingRecipeTimeTicks.get() > 0) {
        this.remainingRecipeTimeTicks.add(-1);

        if (this.remainingRecipeTimeTicks.get() == 0) {
          SoakingPotRecipe currentRecipe = this.currentRecipe;
          ItemStack inputItem = this.inputStackHandler.extractItem(0, this.inputStackHandler.getSlotLimit(0), false);
          this.inputFluidTank.drain(currentRecipe.getInputFluid().amount * inputItem.getCount(), true);
          this.outputStackHandler.insertItem(0, currentRecipe.getOutput(), false);
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
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setTag("inputFluidTank", this.inputFluidTank.writeToNBT(new NBTTagCompound()));
    compound.setInteger("remainingRecipeTimeTicks", this.remainingRecipeTimeTicks.get());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.inputFluidTank.readFromNBT(compound.getCompoundTag("inputFluidTank"));
    this.remainingRecipeTimeTicks.set(compound.getInteger("remainingRecipeTimeTicks"));
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
