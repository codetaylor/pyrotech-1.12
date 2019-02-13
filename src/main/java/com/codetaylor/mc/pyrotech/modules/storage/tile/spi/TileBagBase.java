package com.codetaylor.mc.pyrotech.modules.storage.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.LargeDynamicItemLimitedStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionBase;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockBagBase;
import com.codetaylor.mc.pyrotech.modules.storage.client.render.BagInteractionInputRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class TileBagBase
    extends TileNetBase
    implements ITileInteractable {

  protected StackHandler stackHandler;
  protected IInteraction[] interactions;

  protected TileBagBase() {

    super(ModuleStorage.TILE_DATA_SERVICE);

    this.stackHandler = new StackHandler(this.getItemCapacity(), this::isItemValidForInsertion);
    this.stackHandler.addObserver((handler, slot) -> this.markDirty());

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataLargeItemStackHandler<>(this.stackHandler)
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionInput(this, this.stackHandler),
        new InteractionToggleOpen()
    };
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (this.allowAutomation())
        && (facing == EnumFacing.DOWN)
        && (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (this.allowAutomation()
        && facing == EnumFacing.DOWN
        && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.stackHandler;
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public abstract boolean allowAutomation();

  public abstract int getItemCapacity();

  public void setOpen(boolean open) {

    if (open && !this.isOpen()) {
      IBlockState blockState = this.world.getBlockState(this.pos);
      this.world.setBlockState(this.pos, blockState.withProperty(BlockBagBase.TYPE, BlockBagBase.EnumType.OPEN), 1 | 2);

    } else if (!open && this.isOpen()) {
      IBlockState blockState = this.world.getBlockState(this.pos);
      this.world.setBlockState(this.pos, blockState.withProperty(BlockBagBase.TYPE, BlockBagBase.EnumType.CLOSED), 1 | 2);
    }
  }

  public StackHandler getStackHandler() {

    return this.stackHandler;
  }

  public int getItemCount() {

    return this.stackHandler.getTotalItemCount();
  }

  protected boolean isItemValidForInsertion(ItemStack itemStack) {

    IBlockState blockState = this.world.getBlockState(this.pos);
    Block block = blockState.getBlock();

    if (block instanceof BlockBagBase) {
      return ((BlockBagBase) block).isItemValidForInsertion(itemStack);
    }

    return false;
  }

  protected boolean isOpen() {

    IBlockState blockState = this.world.getBlockState(this.pos);
    Block block = blockState.getBlock();

    if (block instanceof BlockBagBase) {
      return ((BlockBagBase) block).isOpen(blockState);
    }

    return false;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() instanceof BlockBagBase) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return ITileInteractable.super.getTileFacing(world, pos, blockState);
  }

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  public static class InteractionToggleOpen
      extends InteractionBase<TileBagBase> {

    /* package */ InteractionToggleOpen() {

      super(EnumFacing.VALUES, BlockBagBase.AABB_NORTH);
    }

    @Override
    public boolean interact(EnumType type, TileBagBase tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (type == EnumType.MouseClick) {
        tile.setOpen(!tile.isOpen());
        return true;
      }
      return false;
    }
  }

  public static class InteractionInput
      extends InteractionItemStack<TileBagBase> {

    private final TileBagBase tile;

    /* package */ InteractionInput(TileBagBase tile, ItemStackHandler stackHandler) {

      super(new ItemStackHandler[]{stackHandler}, 0, new EnumFacing[]{EnumFacing.UP}, BlockBagBase.AABB_NORTH, new Transform(
          Transform.translate(0.5, 10.0 / 16.0, 0.5),
          Transform.rotate(),
          Transform.scale(0.25, 0.25, 0.25)
      ));
      this.tile = tile;
    }

    public TileBagBase getTile() {

      return this.tile;
    }

    @Override
    public boolean isEnabled() {

      return this.tile.isOpen();
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return this.tile.isItemValidForInsertion(itemStack);
    }

    @Override
    protected void onInsert(EnumType type, ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {

      super.onInsert(type, itemStack, world, player, pos);

      if (!world.isRemote
          && type == EnumType.MouseClick) {
        world.playSound(
            null,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            SoundEvents.BLOCK_CLOTH_PLACE,
            SoundCategory.BLOCKS,
            0.5f,
            (float) (1 + RandomHelper.random().nextGaussian() * 0.4f)
        );
      }
    }

    @Override
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      BagInteractionInputRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }

    @Override
    public void renderSolidPassText(World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

      BagInteractionInputRenderer.INSTANCE.renderSolidPassText(this, world, fontRenderer, yaw, offset, pos, blockState, partialTicks);
    }

    @Override
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return BagInteractionInputRenderer.INSTANCE.renderAdditivePass(this, world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handler
  // ---------------------------------------------------------------------------

  /**
   * This is public because it is shared with the item's capability provider.
   */
  public static class StackHandler
      extends LargeDynamicItemLimitedStackHandler
      implements ITileDataItemStackHandler {

    private final Predicate<ItemStack> filter;

    public StackHandler(int itemCapacity, Predicate<ItemStack> filter) {

      super(10, itemCapacity);
      this.filter = filter;
    }

    @Override
    public ItemStack insertItem(ItemStack itemStack, boolean simulate) {

      if (this.filter.test(itemStack)) {
        return super.insertItem(itemStack, simulate);
      }

      return itemStack;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      if (this.filter.test(stack)) {
        return super.insertItem(stack, simulate);
      }

      return stack;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return Integer.MAX_VALUE;
    }
  }
}
