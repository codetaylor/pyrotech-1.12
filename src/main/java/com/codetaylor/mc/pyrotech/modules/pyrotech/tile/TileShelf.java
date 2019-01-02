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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.util.vector.Quaternion;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileShelf
    extends TileNetBase
    implements ITileInteractable {

  private ShelfStackHandler shelfStackHandler;

  private IInteraction[] interactions;

  public TileShelf() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    // --- Initialize ---

    this.shelfStackHandler = new ShelfStackHandler();
    this.shelfStackHandler.addObserver((handler, slot) -> this.markDirty());

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.shelfStackHandler)
    });

    // --- Interactions ---

    this.interactions = new IInteraction[12];

    List<IInteraction> interactionList = new ArrayList<>();

    for (int i = 0; i < 9; i++) {
      int x = i % 3;
      int y = i / 3;
      interactionList.add(new ShelfInteraction(this.shelfStackHandler, i, x, y));
    }

    this.interactions = interactionList.toArray(new IInteraction[0]);
  }

  // ---------------------------------------------------------------------------
  // - Container
  // ---------------------------------------------------------------------------

  public void dropContents() {

    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.shelfStackHandler, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.shelfStackHandler.deserializeNBT(compound.getCompoundTag("shelfStackHandler"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

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

  private class ShelfStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ ShelfStackHandler() {

      super(9);
    }
  }

}
