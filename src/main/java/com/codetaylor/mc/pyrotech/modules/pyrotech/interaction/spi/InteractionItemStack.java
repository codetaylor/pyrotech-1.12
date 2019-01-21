package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionRenderers;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class InteractionItemStack<T extends TileEntity & ITileInteractable>
    extends InteractionBase<T>
    implements IInteractionItemStack<T> {

  private static final Vec3d TEXT_OFFSET = new Vec3d(0, 0.1, 0);

  protected final ItemStackHandler[] stackHandlers;
  protected final int slot;
  protected final Transform transform;

  /**
   * Used to cache the last item checked for validation.
   */
  protected ItemStack lastItemChecked;

  /**
   * Used to cache if the last item checked was valid.
   */
  protected boolean lastItemValid;

  public InteractionItemStack(
      ItemStackHandler[] stackHandlers,
      int slot,
      EnumFacing[] sides,
      AxisAlignedBB bounds,
      Transform transform
  ) {

    super(sides, bounds);
    this.stackHandlers = stackHandlers;
    this.slot = slot;
    this.transform = transform;
  }

  @Override
  public Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack, float partialTicks) {

    return this.transform;
  }

  /**
   * Searches each stack handler in the order they are provided in the
   * constructor. Returns the first non-empty {@link ItemStack} found.
   *
   * @return the first {@link ItemStack} found
   */
  @Override
  public ItemStack getStackInSlot() {

    for (int i = 0; i < this.stackHandlers.length; i++) {
      ItemStack itemStack = this.stackHandlers[i].getStackInSlot(this.slot);

      if (!itemStack.isEmpty()) {
        return itemStack;
      }
    }

    return ItemStack.EMPTY;
  }

  /**
   * Searches each stack handler in the order they are provided in the
   * constructor. Returns the first non-empty {@link ItemStack} extracted.
   *
   * @param amount   the amount to extract
   * @param simulate set false to actually perform the extraction
   * @return the first non-empty {@link ItemStack} extracted
   */
  @Override
  public ItemStack extract(int amount, boolean simulate) {

    for (int i = 0; i < this.stackHandlers.length; i++) {
      ItemStack itemStack = this.stackHandlers[i].getStackInSlot(this.slot);

      if (!itemStack.isEmpty()) {
        return this.stackHandlers[i].extractItem(this.slot, amount, simulate);
      }
    }

    return ItemStack.EMPTY;
  }

  /**
   * Searches each stack handler in the order they are provided in the
   * constructor. Inserts into the first handler that will accept any of the
   * given {@link ItemStack}.
   * <p>
   * Returns any remaining items in an {@link ItemStack}.
   *
   * @param itemStack the {@link ItemStack} to insert
   * @param simulate  set false to actually perform the insertion
   * @return any remaining items in an {@link ItemStack}
   */
  @Override
  public ItemStack insert(ItemStack itemStack, boolean simulate) {

    if (!this.isItemStackValid(itemStack)) {
      return itemStack;
    }

    for (int i = 0; i < this.stackHandlers.length; i++) {
      int count = itemStack.getCount();
      ItemStack result = this.stackHandlers[i].insertItem(this.slot, itemStack, true);

      if (result.getCount() != count) {
        return this.stackHandlers[i].insertItem(this.slot, itemStack, simulate);
      }
    }

    return itemStack;
  }

  /**
   * @return true if all stack handlers are empty
   */
  @Override
  public boolean isEmpty() {

    return this.getStackInSlot().isEmpty();
  }

  @Override
  public boolean isItemStackValid(ItemStack itemStack) {

    if (itemStack.isEmpty()) {
      return false;
    }

    // TODO: Rethink cache invalidation
    // 2019-01-09
    // There is currently no way to invalidate this cache when the
    // underlying handler changes on the client. When the interaction is run
    // on the client, no changes to the stack handler are actually made. They
    // instead ride in over the network, therefore, any cache invalidation
    // that we do inside the interact method occurs before stacks are changed.
    // Item stack validation that relies on interrogating the state of the
    // stacks is then incorrect on the client.
    /*
    if (this.lastItemChecked == null
        || this.lastItemChecked.getItem() != itemStack.getItem()
        || this.lastItemChecked.getMetadata() != itemStack.getMetadata()) {

      // Run the potentially expensive check.
      this.lastItemChecked = itemStack.copy();
      this.lastItemValid = this.doItemStackValidation(itemStack);
    }

    return this.lastItemValid;
    */

    return this.doItemStackValidation(itemStack);
  }

  /**
   * Actually do the itemStack validation here.
   * <p>
   * This method can contain potentially expensive code like a recipe check.
   * The return value will be cached for the last item checked.
   *
   * @param itemStack the stack to perform validation on
   * @return true if the item stack is allowed in this interaction
   */
  protected boolean doItemStackValidation(ItemStack itemStack) {

    return true;
  }

  @Override
  public boolean interact(EnumType type, T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (!this.allowInteractionWithHand(hand)
        || !this.allowInteractionWithType(type)
    ) {
      return false;
    }

    BlockPos tilePos = tile.getPos();
    ItemStack heldItem = player.getHeldItem(hand);

    if ((heldItem.isEmpty() && type == EnumType.MouseClick)
        || type == EnumType.MouseWheelDown) {

      // Remove item with empty hand

      if (!this.isEmpty()) {

        if (this.doExtract(type, world, player, tilePos)) {
          this.onExtract(type, world, player, tilePos);
          return true;

        } else {
          return false;
        }

      }

    } else {

      ItemStack itemStackToInsert = heldItem;
      int insertIndex = this.getInsertionIndex(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ);

      ItemStack alternateItemStack = null;

      if (!this.isEmpty()
          && type == EnumType.MouseWheelUp) {

        // If the interaction already contains an item stack, first check if the
        // current held item can be inserted, then search the player
        // inventory for more of the same item and insert from that stack.

        ItemStack result = this.stackHandlers[insertIndex].insertItem(this.slot, itemStackToInsert, true);

        if (result.getCount() == itemStackToInsert.getCount()) {

          for (ItemStack itemStack : player.inventory.mainInventory) {
            result = this.stackHandlers[insertIndex].insertItem(this.slot, itemStack, true);

            if (result.getCount() != itemStack.getCount()) {
              alternateItemStack = itemStack;
              break;
            }
          }
        }
      }

      if (alternateItemStack != null) {
        itemStackToInsert = alternateItemStack;
      }

      if (!this.isItemStackValid(itemStackToInsert)) {
        return false;
      }

      // Insert item

      int count = itemStackToInsert.getCount();
      ItemStack itemStack = itemStackToInsert.copy();
      int insertItemCount = this.getInsertItemCount(type, itemStack);
      itemStack.setCount(insertItemCount);
      ItemStack result = this.stackHandlers[insertIndex].insertItem(this.slot, itemStack, world.isRemote);

      if (result.getCount() != count) {
        int actualInsertCount = insertItemCount - result.getCount();

        if (!world.isRemote) {
          itemStackToInsert.setCount(itemStackToInsert.getCount() - actualInsertCount);
        }

        itemStack.setCount(actualInsertCount);
        this.onInsert(type, itemStack, world, player, hitPos);
        return true;
      }
    }

    return false;
  }

  /**
   * Override this to restrict how many items can be inserted at once.
   * <p>
   * The default is to insert the entire stack.
   *
   * @param type
   * @param itemStack the stack being inserted
   * @return how many items can be inserted
   */
  protected int getInsertItemCount(EnumType type, ItemStack itemStack) {

    if (type == EnumType.MouseWheelUp) {
      return 1;
    }

    return itemStack.getCount();
  }

  /**
   * Called immediately after a successful insert.
   * <p>
   * <p>
   * The stack passed is a copy of the stack that was inserted. Changing this
   * stack will have no effect.
   *
   * @param type
   * @param itemStack copy of the stack inserted
   * @param world     the world
   * @param player    the interacting player
   * @param pos       the block position interacted with
   */
  protected void onInsert(EnumType type, ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {
    //
  }

  /**
   * Called immediately after a successful extract.
   *
   * @param type
   * @param world  the world
   * @param player the interacting player
   * @param pos    the block position interacted with
   */
  protected void onExtract(EnumType type, World world, EntityPlayer player, BlockPos pos) {

    if (!world.isRemote
        && type == EnumType.MouseClick) {
      world.playSound(
          null,
          pos.getX(),
          pos.getY(),
          pos.getZ(),
          SoundEvents.ENTITY_ITEM_PICKUP,
          SoundCategory.BLOCKS,
          0.25f,
          (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
      );
    }
  }

  /**
   * Override this to change the extraction behavior.
   *
   * @param type
   * @param world
   * @param player
   * @param tilePos
   * @return true to prevent processing subsequent interactions
   */
  protected boolean doExtract(EnumType type, World world, EntityPlayer player, BlockPos tilePos) {

    int extractItemCount;

    if (type == EnumType.MouseWheelDown) {
      extractItemCount = 1;

    } else {
      extractItemCount = Integer.MAX_VALUE;
    }

    ItemStack result = this.extract(extractItemCount, world.isRemote);

    if (!result.isEmpty()) {

      if (!world.isRemote) {
        StackHelper.addToInventoryOrSpawn(world, player, result, tilePos);
      }

      return true;
    }

    return false;
  }

  /**
   * When a player interacts with a non-empty hand this method is called to
   * determine which stack handler we try to insert into.
   * <p>
   * This allows things like separating valid recipe items into the input
   * stack handler and invalid recipe items directly into the output stack
   * handler.
   * <p>
   * Indices are into the {@link #stackHandlers} array.
   *
   * @param tile    the TE
   * @param world   the world
   * @param hitPos  the block position interacted with
   * @param state   the blockState at the hitPos
   * @param player  the interacting player
   * @param hand    the hand used to interact
   * @param hitSide the side of the block hit
   * @param hitX    the x position of the hit, relative to the hitPos
   * @param hitY    the y position of the hit, relative to the hitPos
   * @param hitZ    the z position of the hit, relative to the hitPos
   * @return the stack handler index to insert into
   */
  protected int getInsertionIndex(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    return 0;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

    InteractionRenderers.ITEM_STACK.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void renderSolidPassText(World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

    InteractionRenderers.ITEM_STACK.renderSolidPassText(this, world, fontRenderer, yaw, offset, pos, blockState, partialTicks);
  }

  @Override
  public Vec3d getTextOffset(EnumFacing tileFacing, EnumFacing playerHorizontalFacing, EnumFacing sideHit) {

    return TEXT_OFFSET;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

    return InteractionRenderers.ITEM_STACK.renderAdditivePass(this, world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
  }

  @Override
  public boolean forceRenderAdditivePassWhileSneaking() {

    return true;
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldRenderAdditivePassForHeldItem(ItemStack heldItemMainHand) {

    return !heldItemMainHand.isEmpty();
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldRenderAdditivePassForStackInSlot(boolean sneaking, ItemStack heldItemMainHand) {

    return heldItemMainHand.isEmpty() || sneaking;
  }
}
