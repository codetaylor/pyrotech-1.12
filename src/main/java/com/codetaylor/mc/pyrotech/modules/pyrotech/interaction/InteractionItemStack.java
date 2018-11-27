package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import net.minecraft.block.state.IBlockState;
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

    if (this.lastItemChecked == null
        || this.lastItemChecked.getItem() != itemStack.getItem()
        || this.lastItemChecked.getMetadata() != itemStack.getMetadata()) {

      // Run the potentially expensive check.
      this.lastItemChecked = itemStack.copy();
      this.lastItemValid = this.doItemStackValidation(itemStack);
    }

    return this.lastItemValid;
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
  public boolean interact(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (!this.allowInteractionWithHand(hand)) {
      return false;
    }

    BlockPos tilePos = tile.getPos();
    ItemStack heldItem = player.getHeldItem(hand);

    if (heldItem.isEmpty()) {

      // Remove item with empty hand

      if (!this.isEmpty()) {

        if (this.doExtract(world, player, tilePos)) {
          this.onExtract(world, player, tilePos);
          return true;

        } else {
          return false;
        }

      }

    } else {

      if (!this.isItemStackValid(heldItem)) {
        return false;
      }

      // Insert item

      int count = heldItem.getCount();
      int insertIndex = this.getInsertionIndex(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ);
      ItemStack itemStack = heldItem.copy();
      ItemStack result = this.stackHandlers[insertIndex].insertItem(this.slot, itemStack, world.isRemote);

      if (result.getCount() != count) {

        if (!world.isRemote) {
          heldItem.setCount(result.getCount());
        }

        itemStack.setCount(itemStack.getCount() - result.getCount());
        this.onInsert(itemStack, world, player, hitPos);
        return true;
      }
    }

    return false;
  }

  /**
   * Called immediately after a successful insert.
   * <p>
   * <p>
   * The stack passed is a copy of the stack that was inserted. Changing this
   * stack will have no effect.
   *
   * @param itemStack copy of the stack inserted
   * @param world     the world
   * @param player    the interacting player
   * @param pos       the block position interacted with
   */
  protected void onInsert(ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {
    //
  }

  /**
   * Called immediately after a successful extract.
   *
   * @param world  the world
   * @param player the interacting player
   * @param pos    the block position interacted with
   */
  protected void onExtract(World world, EntityPlayer player, BlockPos pos) {

    if (!world.isRemote) {
      // Plays for everyone. To switch to player local, play on client with
      // nonnull player parameter.
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
   * @param world
   * @param player
   * @param tilePos
   * @return true to prevent processing subsequent interactions
   */
  protected boolean doExtract(World world, EntityPlayer player, BlockPos tilePos) {

    ItemStack result = this.extract(this.getStackInSlot().getCount(), world.isRemote);

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
  public boolean renderAdditivePass(World world, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

    return InteractionRenderers.ITEM_STACK.renderAdditivePass(this, world, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
  }
}
