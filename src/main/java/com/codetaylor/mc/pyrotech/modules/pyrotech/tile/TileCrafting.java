package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
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

public class TileCrafting
    extends TileNetBase
    implements ITileInteractable {

  private InputStackHandler inputStackHandler;
  private ShelfStackHandler shelfStackHandler;

  private IInteraction[] interactions;

  public TileCrafting() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    // --- Initialize ---

    this.inputStackHandler = new InputStackHandler();
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.markDirty();
    });

    this.shelfStackHandler = new ShelfStackHandler();
    this.shelfStackHandler.addObserver((handler, slot) -> {
      this.markDirty();
    });

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.inputStackHandler),
        new TileDataItemStackHandler<>(this.shelfStackHandler)
    });

    // --- Interactions ---

    this.interactions = new IInteraction[12];

    for (int i = 0; i < 9; i++) {
      int x = i % 3;
      int z = i / 3;
      this.interactions[i] = new InputInteraction(this.inputStackHandler, i, x, z);
    }

    for (int i = 0; i < 3; i++) {
      int x = i % 3;
      this.interactions[9 + i] = new ShelfInteraction(this.shelfStackHandler, i, x);
    }
  }

  // ---------------------------------------------------------------------------
  // - Container
  // ---------------------------------------------------------------------------

  public void dropContents() {

    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.inputStackHandler, this.pos);
    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.shelfStackHandler, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.shelfStackHandler.deserializeNBT(compound.getCompoundTag("shelfStackHandler"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("shelfStackHandler", this.shelfStackHandler.serializeNBT());

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

  private class InputInteraction
      extends InteractionItemStack<TileCrafting> {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double ONE_SIXTH = 1.0 / 6.0;

    /* package */ InputInteraction(ItemStackHandler stackHandler, int slot, double x, double z) {

      super(
          new ItemStackHandler[]{stackHandler},
          slot,
          new EnumFacing[]{EnumFacing.UP},
          new AxisAlignedBB(x * ONE_THIRD, 14f / 16f, z * ONE_THIRD, x * ONE_THIRD + ONE_THIRD, 15f / 16f, z * ONE_THIRD + ONE_THIRD),
          new Transform(
              Transform.translate(x * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025, 15f / 16f, z * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025),
              Transform.rotate(1, 0, 0, 90),
              Transform.scale(0.20, 0.20, 0.20)
          )
      );
    }

    @Override
    protected int getInsertItemCount(ItemStack itemStack) {

      return 1;
    }
  }

  private class ShelfInteraction
      extends InteractionItemStack<TileCrafting> {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double ONE_SIXTH = 1.0 / 6.0;

    /* package */ ShelfInteraction(ItemStackHandler stackHandler, int slot, double x) {

      super(
          new ItemStackHandler[]{stackHandler},
          slot,
          new EnumFacing[]{EnumFacing.UP},
          new AxisAlignedBB(x * ONE_THIRD, 0, 0, x * ONE_THIRD + ONE_THIRD, 5f / 16f, ONE_THIRD),
          new Transform(
              Transform.translate(x * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025, 5f / 16f, ONE_SIXTH + 0.025),
              Transform.rotate(1, 0, 0, 90),
              Transform.scale(0.20, 0.20, 0.20)
          )
      );
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handler
  // ---------------------------------------------------------------------------

  private class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ InputStackHandler() {

      super(9);
    }
  }

  private class ShelfStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ ShelfStackHandler() {

      super(3);
    }
  }

}
