package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMillBlade;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.OvenStoneRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileCombustionWorkerStoneBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileMillStone
    extends TileCombustionWorkerStoneBase<OvenStoneRecipe> {

  private BladeStackHandler bladeStackHandler;

  public TileMillStone() {

    this.bladeStackHandler = new BladeStackHandler();
    this.bladeStackHandler.addObserver((handler, slot) -> {
      this.markDirty();
    });

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.bladeStackHandler)
    });

    // --- Interactions ---

    this.addInteractions(new IInteraction[]{
        new InteractionBlade(new ItemStackHandler[]{this.bladeStackHandler})
    });
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.bladeStackHandler.deserializeNBT(compound.getCompoundTag("bladeStackHandler"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("bladeStackHandler", this.bladeStackHandler.serializeNBT());
    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Tile Combustion Worker Stone
  // ---------------------------------------------------------------------------

  @Override
  public void dropContents() {

    super.dropContents();
    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.bladeStackHandler, this.pos);
  }

  @Override
  public OvenStoneRecipe getRecipe(ItemStack itemStack) {

    return OvenStoneRecipe.getRecipe(itemStack);
  }

  @Override
  protected List<ItemStack> getRecipeOutput(OvenStoneRecipe recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks) {

    ItemStack output = recipe.getOutput();
    ItemStack copy = output.copy();
    copy.setCount(input.getCount());
    outputItemStacks.add(copy);
    return outputItemStacks;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModulePyrotechConfig.STONE_MILL.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModulePyrotechConfig.STONE_MILL.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModulePyrotechConfig.STONE_MILL.FUEL_SLOT_SIZE;
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  protected EnumFacing[] getInputInteractionSides() {

    return new EnumFacing[]{EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.UP};
  }

  private class InteractionBlade
      extends InteractionItemStack<TileMillStone> {

    /* package */ InteractionBlade(ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          new EnumFacing[]{EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.UP},
          new AxisAlignedBB(1f / 16f, 1, 1f / 16f, 15f / 16f, 24f / 16f, 15f / 16f),
          new Transform(
              Transform.translate(0.5, 1, 0.5),
              Transform.rotate(0, 1, 0, 90),
              Transform.scale(0.75, 0.75, 0.75)
          )
      );
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (itemStack.getItem() instanceof ItemMillBlade);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class BladeStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ BladeStackHandler() {

      super(1);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // Filter out non-blade items.

      if (!(stack.getItem() instanceof ItemMillBlade)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

}
