package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.TanningRackRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileTanningRack
    extends TileEntityDataBase
    implements ITileInteractable,
    ITickable {

  private final InputStackHandler inputStackHandler;
  private final OutputStackHandler outputStackHandler;

  private final TileDataItemStackHandler<InputStackHandler> tileDataInputStackHandler;
  private final TileDataFloat recipeProgress;

  private final IInteraction<?>[] interactions;

  private TanningRackRecipe currentRecipe;

  public TileTanningRack() {

    super(ModuleTechBasic.TILE_DATA_SERVICE);

    this.inputStackHandler = new InputStackHandler();
    this.outputStackHandler = new OutputStackHandler();

    this.inputStackHandler.addObserver((stackHandler, slotIndex) -> {
      this.updateRecipe();
      this.markDirty();
    });
    this.outputStackHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());

    // --- Network

    this.tileDataInputStackHandler = new TileDataItemStackHandler<>(this.inputStackHandler);
    this.recipeProgress = new TileDataFloat(0);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataInputStackHandler,
        new TileDataItemStackHandler<>(this.outputStackHandler)
    });

    // --- Interactions

    this.interactions = new IInteraction[]{
        new InputInteraction(this.inputStackHandler),
        new OutputInteraction(this.outputStackHandler)
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public InputStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public OutputStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    // TODO
  }

  private void updateRecipe() {

    // Note: This is called during the call to readFromNBT and the tile doesn't,
    // have access to the world object yet. Don't use the world here.

    ItemStack itemStack = this.inputStackHandler.getStackInSlot(0);
    this.currentRecipe = TanningRackRecipe.getRecipe(itemStack);
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.tileDataInputStackHandler.isDirty()) {
      this.updateRecipe();
    }
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.recipeProgress.set(compound.getFloat("recipeProgress"));
    this.updateRecipe();
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setFloat("recipeProgress", this.recipeProgress.get());
    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBasicConfig.STAGES_TANNING_RACK;
  }

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  @Override
  public IInteraction<?>[] getInteractions() {

    return this.interactions;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return blockState.getValue(Properties.FACING_HORIZONTAL);
  }

  private static class InputInteraction
      extends InteractionItemStack<TileTanningRack> {

    public InputInteraction(ItemStackHandler stackHandler) {

      super(
          new ItemStackHandler[]{stackHandler},
          0,
          new EnumFacing[]{EnumFacing.NORTH},
          InteractionBounds.BLOCK,
          new Transform(
              Transform.translate(0.5, 0.525, 0.475),
              Transform.rotate(1, 0, 0, 22.5),
              Transform.scale(0.75, 0.75, 0.75)
          )
      );
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      return ModuleTechBasic.Blocks.TANNING_RACK.getBoundingBox(blockState, world, pos);
    }

    @Override
    public boolean isItemStackValid(ItemStack itemStack) {

      return (TanningRackRecipe.getRecipe(itemStack) != null);
    }
  }

  private static class OutputInteraction
      extends InteractionItemStack<TileTanningRack> {

    public OutputInteraction(ItemStackHandler stackHandler) {

      super(
          new ItemStackHandler[]{stackHandler},
          0,
          new EnumFacing[]{EnumFacing.NORTH},
          InteractionBounds.BLOCK,
          new Transform(
              Transform.translate(0.5, 0.525, 0.475),
              Transform.rotate(1, 0, 0, 22.5),
              Transform.scale(0.75, 0.75, 0.75)
          )
      );
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      return ModuleTechBasic.Blocks.TANNING_RACK.getBoundingBox(blockState, world, pos);
    }

    @Override
    public boolean isItemStackValid(ItemStack itemStack) {

      return false;
    }
  }

  // ---------------------------------------------------------------------------
  // - Handlers
  // ---------------------------------------------------------------------------

  public static class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public InputStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

      return (TanningRackRecipe.getRecipe(stack) != null);
    }
  }

  public static class OutputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public OutputStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }
  }

}
