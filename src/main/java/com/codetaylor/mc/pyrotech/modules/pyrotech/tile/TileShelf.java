package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.LargeObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileShelf
    extends TileNetBase
    implements ITileInteractable {

  private StackHandler stackHandler;

  private IInteraction[] interactions;

  public TileShelf() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    // --- Initialize ---

    this.stackHandler = new StackHandler(this.getMaxStacks());
    this.stackHandler.addObserver((handler, slot) -> this.markDirty());

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataLargeItemStackHandler<>(this.stackHandler)
    });

    // --- Interactions ---

    this.interactions = new IInteraction[12];

    List<IInteraction> interactionList = new ArrayList<>();

    for (int i = 0; i < 9; i++) {
      int x = i % 3;
      int y = i / 3;
      interactionList.add(new ShelfInteraction(this.stackHandler, i, x, y));
    }

    this.interactions = interactionList.toArray(new IInteraction[0]);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  protected int getMaxStacks() {

    return ModulePyrotechConfig.SHELF.MAX_STACKS;
  }

  // ---------------------------------------------------------------------------
  // - Container
  // ---------------------------------------------------------------------------

  public void dropContents() {

    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.stackHandler, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.stackHandler.deserializeNBT(compound.getCompoundTag("shelfStackHandler"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setTag("shelfStackHandler", this.stackHandler.serializeNBT());

    return compound;
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

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return blockState.getValue(Properties.FACING_HORIZONTAL);
  }

  private class ShelfInteraction
      extends InteractionItemStack<TileShelf> {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double ONE_SIXTH = 1.0 / 6.0;

    /* package */ ShelfInteraction(ItemStackHandler stackHandler, int slot, double x, int y) {

      super(
          new ItemStackHandler[]{stackHandler},
          slot,
          new EnumFacing[]{EnumFacing.NORTH},
          new AxisAlignedBB(x * ONE_THIRD, y * ONE_THIRD, 10.0 / 16.0, x * ONE_THIRD + ONE_THIRD, y * ONE_THIRD + ONE_THIRD, 1),
          new Transform(
              Transform.translate(x * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025, y * (ONE_THIRD - 0.025) + ONE_SIXTH, 2 * ONE_THIRD + ONE_SIXTH - 0.025),
              Transform.rotate(0, 1, 0, 180),
              Transform.scale(0.20, 0.20, 0.20)
          )
      );
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handler
  // ---------------------------------------------------------------------------

  private class StackHandler
      extends LargeObservableStackHandler
      implements ITileDataItemStackHandler {

    private final int maxStacks;

    /* package */ StackHandler(int maxStacks) {

      super(9);
      this.maxStacks = maxStacks;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return stack.getMaxStackSize() * this.maxStacks;
    }
  }

}
