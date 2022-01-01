package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.inventory.LargeObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileCrate
    extends TileEntityDataBase
    implements ITileInteractable {

  private StackHandler stackHandler;

  private IInteraction[] interactions;

  public TileCrate() {

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
      int z = i / 3;
      interactionList.add(new Interaction(this.stackHandler, i, x, z));
    }

    this.interactions = interactionList.toArray(new IInteraction[0]);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  protected int getMaxStacks() {

    return ModuleStorageConfig.CRATE.MAX_STACKS;
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

    return ModuleStorageConfig.CRATE.ALLOW_AUTOMATION;
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

    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setTag("stackHandler", this.stackHandler.serializeNBT());

    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleStorageConfig.STAGES_CRATE;
  }

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleStorage.Blocks.CRATE
        || blockState.getBlock() == ModuleStorage.Blocks.CRATE_STONE) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return ITileInteractable.super.getTileFacing(world, pos, blockState);
  }

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class Interaction
      extends InteractionItemStack<TileCrate> {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double ONE_SIXTH = 1.0 / 6.0;

    /* package */ Interaction(ItemStackHandler stackHandler, int slot, double x, int z) {

      super(
          new ItemStackHandler[]{stackHandler},
          slot,
          new EnumFacing[]{EnumFacing.UP},
          new AxisAlignedBB(
              x * ONE_THIRD, 13f / 16f, z * ONE_THIRD,
              x * ONE_THIRD + ONE_THIRD, 15f / 16f, z * ONE_THIRD + ONE_THIRD
          ),
          new Transform(
              Transform.translate(x * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025, 15f / 16f, z * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025),
              Transform.rotate(),
              Transform.scale(0.20, 0.20, 0.20)
          )
      );
    }

    @Override
    public Vec3d getTextOffset(EnumFacing tileFacing, EnumFacing playerHorizontalFacing, EnumFacing sideHit) {

      return new Vec3d(0, 0.25, 0);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handler
  // ---------------------------------------------------------------------------

  /**
   * https://github.com/codetaylor/pyrotech-1.12/issues/380
   * <p>
   * This handler has been modified to fool interrogators into thinking that it
   * has ten item slots, one of which is always empty.
   * <p>
   * This is necessary to trick the injected Forge logic found in
   * {@link net.minecraftforge.items.VanillaInventoryCodeHooks#isFull(IItemHandler)}
   * into thinking that this container is not full and can accept input. The
   * hopper is then allowed to attempt an insert.
   * <p>
   * This has been tested against the vanilla hopper and Pyrotech's stone hopper.
   * <p>
   * It is considered to be an experimental fix and may introduce unintended
   * side-effects with other mods.
   */
  @SuppressWarnings("JavadocReference")
  private static class StackHandler
      extends LargeObservableStackHandler
      implements ITileDataItemStackHandler {

    private final int maxStackSize;

    /* package */ StackHandler(int maxStackSize) {

      super(9);
      this.maxStackSize = maxStackSize;
    }

    @Override
    public int getSlots() {

      return 10;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {

      return (slot == 9) ? ItemStack.EMPTY : super.getStackInSlot(slot);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {

      super.setStackInSlot((slot == 9) ? 8 : slot, stack);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      return super.insertItem((slot == 9) ? 8 : slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

      return super.extractItem((slot == 9) ? 8 : slot, amount, simulate);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return stack.getMaxStackSize() * this.maxStackSize;
    }
  }

}
