package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
      InteractionBounds bounds,
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

    EnumFacing tileFacing = tile.getTileFacing(world, hitPos, state);
    BlockPos tilePos = tile.getPos();
    IBlockState tileBlockState = world.getBlockState(tilePos);

    if (!this.canInteractWith(world, hitSide, hitPos, new Vec3d(hitX + tilePos.getX(), hitY + tilePos.getY(), hitZ + tilePos.getZ()), tilePos, tileBlockState, tileFacing)) {
      return false;
    }

    ItemStack heldItemMainHand = player.getHeldItemMainhand();

    if (heldItemMainHand.isEmpty()) {

      // Remove item with empty hand

      if (!this.isEmpty()) {

        ItemStack result = this.extract(this.getStackInSlot().getCount(), world.isRemote);

        if (!result.isEmpty()) {

          if (!world.isRemote) {
            StackHelper.addToInventoryOrSpawn(world, player, result, tilePos);
          }

          return true;
        }
      }

    } else {

      if (!this.isItemStackValid(heldItemMainHand)) {
        return false;
      }

      // Insert item

      int count = heldItemMainHand.getCount();
      int insertIndex = this.getInsertionIndex(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ);
      ItemStack itemStack = new ItemStack(heldItemMainHand.getItem(), count, heldItemMainHand.getMetadata());
      ItemStack result = this.stackHandlers[insertIndex].insertItem(this.slot, itemStack, world.isRemote);

      if (result.getCount() != count) {

        if (!world.isRemote) {
          heldItemMainHand.setCount(result.getCount());
        }

        return true;
      }
    }

    return false;
  }

  /**
   * When a player interacts with a non-empty hand this method is called to
   * determine which stack handler we try to insert into.
   * <p>
   * <p>
   * Indices are into the {@link #stackHandlers} array.
   *
   * @param tile    the TE
   * @param world   the world
   * @param hitPos  the block position interacted with
   * @param state   the blockState at the hitPos
   * @param player  the player
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
  public void renderAdditivePass(World world, EnumFacing hitSide, BlockPos hitPos, Vec3d hitVec, BlockPos pos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

    InteractionRenderers.ITEM_STACK.renderAdditivePass(this, world, hitSide, hitPos, hitVec, pos, blockState, heldItemMainHand, partialTicks);
  }
}
