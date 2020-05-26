package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.athenaeum.inventory.LargeObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileNetBase;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockShelfBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileShelf
    extends TileNetBase
    implements ITileInteractable {

  private StackHandler stackHandler;

  private IInteraction[] interactions;

  public TileShelf() {

    super(ModuleStorage.TILE_DATA_SERVICE);

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
      interactionList.add(new ShelfInteraction(this, this.stackHandler, i, x, y));
    }

    this.interactions = interactionList.toArray(new IInteraction[0]);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  protected int getMaxStacks() {

    return ModuleStorageConfig.SHELF.MAX_STACKS;
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return this.allowAutomation()
        && (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (this.allowAutomation()
        && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

      //noinspection unchecked
      return (T) this.stackHandler;
    }

    return null;
  }

  protected boolean allowAutomation() {

    return ModuleStorageConfig.SHELF.ALLOW_AUTOMATION;
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

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleStorageConfig.STAGES_SHELF;
  }

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

    if (blockState.getBlock() == ModuleStorage.Blocks.SHELF
        || blockState.getBlock() == ModuleStorage.Blocks.SHELF_STONE) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return ITileInteractable.super.getTileFacing(world, pos, blockState);
  }

  private class ShelfInteraction
      extends InteractionItemStack<TileShelf> {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double ONE_SIXTH = 1.0 / 6.0;

    private final Transform forwardTransform;
    private final AxisAlignedBB forwardBounds;
    private final TileShelf tile;

    /* package */ ShelfInteraction(TileShelf tile, ItemStackHandler stackHandler, int slot, double x, int y) {

      super(
          new ItemStackHandler[]{stackHandler},
          slot,
          new EnumFacing[]{EnumFacing.NORTH},
          new AxisAlignedBB(x * ONE_THIRD, y * ONE_THIRD, 12.0 / 16.0, x * ONE_THIRD + ONE_THIRD, y * ONE_THIRD + ONE_THIRD, 1),
          new Transform(
              Transform.translate(x * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025, y * (ONE_THIRD - 0.025) + ONE_SIXTH, 2 * ONE_THIRD + ONE_SIXTH - 0.025),
              Transform.rotate(0, 1, 0, 180),
              Transform.scale(0.20, 0.20, 0.20)
          )
      );

      this.tile = tile;

      this.forwardBounds = new AxisAlignedBB(x * ONE_THIRD, y * ONE_THIRD, 2.0 / 16.0, x * ONE_THIRD + ONE_THIRD, y * ONE_THIRD + ONE_THIRD, 6.0 / 16.0);

      this.forwardTransform = new Transform(
          Transform.translate(x * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025, y * (ONE_THIRD - 0.025) + ONE_SIXTH, ONE_SIXTH - 0.025),
          Transform.rotate(0, 1, 0, 180),
          Transform.scale(0.20, 0.20, 0.20)
      );
    }

    @Override
    public Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack, float partialTicks) {

      if (blockState.getBlock() instanceof BlockShelfBase) {
        BlockShelfBase.EnumType type = blockState.getValue(BlockShelfBase.TYPE);

        if (type == BlockShelfBase.EnumType.FORWARD) {
          return this.forwardTransform;
        }
      }

      return super.getTransform(world, pos, blockState, itemStack, partialTicks);
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      if (blockState.getBlock() instanceof BlockShelfBase) {
        BlockShelfBase.EnumType type = blockState.getValue(BlockShelfBase.TYPE);

        if (type == BlockShelfBase.EnumType.FORWARD) {
          return this.forwardBounds;
        }
      }

      return super.getInteractionBounds(world, pos, blockState);
    }

    @Override
    public Vec3d getTextOffset(EnumFacing tileFacing, EnumFacing playerHorizontalFacing, EnumFacing sideHit) {

      return new Vec3d(0, 0.125, 0);
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
