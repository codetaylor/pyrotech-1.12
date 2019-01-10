package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.CompactingBinInteractionInputRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.CompactingBinRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class TileCompactingBin
    extends TileNetBase
    implements ITileInteractable {

  private InputStackHandler inputStackHandler;
  private TileDataFloat recipeProgress;
  private TileDataItemStackHandler tileDataInputStackHandler;
  private CompactingBinRecipe currentRecipe;
  private IInteraction[] interactions;

  public TileCompactingBin() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.inputStackHandler = new InputStackHandler(this);
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.recipeProgress.set(0);
      this.updateRecipe();
      this.markDirty();
    });

    this.recipeProgress = new TileDataFloat(0);

    // --- Network ---

    this.tileDataInputStackHandler = new TileDataItemStackHandler<>(this.inputStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataInputStackHandler,
        this.recipeProgress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionInput(this, this.inputStackHandler),
        new InteractionShovel()
    };
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.tileDataInputStackHandler.isDirty()) {
      this.updateRecipe();
    }
  }

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  private void updateRecipe() {

    ItemStack itemStack = this.inputStackHandler.getFirstNonEmptyItemStack();

    if (!itemStack.isEmpty()) {
      this.currentRecipe = CompactingBinRecipe.getRecipe(itemStack);

    } else {
      this.currentRecipe = null;
    }
  }

  private boolean isItemValidForInsertion(ItemStack itemStack) {

    CompactingBinRecipe recipe = CompactingBinRecipe.getRecipe(itemStack);

    if (recipe == null) {
      return false;
    }

    if (this.currentRecipe == null) {
      return true;
    }

    return (recipe == this.currentRecipe);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public float getRecipeProgress() {

    return this.recipeProgress.get();
  }

  public InputStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public CompactingBinRecipe getCurrentRecipe() {

    return this.currentRecipe;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setFloat("recipeProgress", this.recipeProgress.get());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.recipeProgress.set(compound.getFloat("recipeProgress"));
    this.updateRecipe();
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  public static class InteractionInput
      extends InteractionItemStack<TileCompactingBin> {

    private final TileCompactingBin tile;

    /* package */ InteractionInput(TileCompactingBin tile, ItemStackHandler stackHandler) {

      super(new ItemStackHandler[]{stackHandler}, 0, new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK, new Transform(
          Transform.translate(0.5, 1.0, 0.5),
          Transform.rotate(),
          Transform.scale(0.75, 0.75, 0.75)
      ));
      this.tile = tile;
    }

    public TileCompactingBin getTile() {

      return this.tile;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return this.tile.isItemValidForInsertion(itemStack);
    }

    @Override
    protected void onInsert(ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {

      super.onInsert(itemStack, world, player, pos);

      if (!world.isRemote) {
        world.playSound(
            null,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            SoundEvents.BLOCK_WOOD_PLACE,
            SoundCategory.BLOCKS,
            0.5f,
            (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
        );
      }
    }

    @Override
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      CompactingBinInteractionInputRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }

    @Override
    public void renderSolidPassText(World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

      CompactingBinInteractionInputRenderer.INSTANCE.renderSolidPassText(this, world, fontRenderer, yaw, offset, pos, blockState, partialTicks);
    }

    @Override
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return CompactingBinInteractionInputRenderer.INSTANCE.renderAdditivePass(this, world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
    }
  }

  private class InteractionShovel
      extends InteractionUseItemBase<TileCompactingBin> {

    /* package */ InteractionShovel() {

      super(new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK);
    }

    @Override
    protected boolean allowInteraction(TileCompactingBin tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (tile.currentRecipe == null
          || tile.currentRecipe.getAmount() > tile.getInputStackHandler().getTotalItemCount()) {
        return false;
      }

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item heldItem = heldItemStack.getItem();
      ResourceLocation resourceLocation = heldItem.getRegistryName();

      if (resourceLocation == null) {
        return false;
      }

      String registryName = resourceLocation.toString();

      if (heldItem.getToolClasses(heldItemStack).contains("shovel")) {
        return !ArrayHelper.contains(ModulePyrotechConfig.COMPACTING_BIN.SHOVEL_BLACKLIST, registryName);

      } else {
        return ArrayHelper.contains(ModulePyrotechConfig.COMPACTING_BIN.SHOVEL_WHITELIST, registryName);
      }
    }

    @Override
    protected void applyItemDamage(ItemStack itemStack, EntityPlayer player) {

      // We apply our own item damage on recipe completion.
    }

    @Override
    protected boolean doInteraction(TileCompactingBin tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (!world.isRemote) {

        int harvestLevel = heldItem.getItem().getHarvestLevel(heldItem, "shovel", player, null);
        int[] requiredToolUses = tile.currentRecipe.getRequiredToolUses();
        tile.recipeProgress.add(1f / ArrayHelper.getOrLast(requiredToolUses, harvestLevel));

        if (tile.recipeProgress.get() > 0.9999) {
          // recipe complete
          StackHelper.spawnStackOnTop(world, tile.currentRecipe.getOutput(), hitPos, 1.0);
          world.playSound(null, hitPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);

          // reduce input items
          tile.getInputStackHandler().removeItems(tile.currentRecipe.getAmount());

          // damage tool
          heldItem.damageItem(ModulePyrotechConfig.COMPACTING_BIN.TOOL_DAMAGE_PER_CRAFT, player);
        }
      }

      return true;
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  public class InputStackHandler
      extends DynamicStackHandler {

    private final TileCompactingBin tile;

    /* package */ InputStackHandler(TileCompactingBin tile) {

      super(1);
      this.tile = tile;
    }

    public int getTotalItemCount() {

      int result = 0;

      for (int i = 0; i < this.getSlots(); i++) {
        ItemStack stackInSlot = this.getStackInSlot(i);

        if (!stackInSlot.isEmpty()) {
          result += stackInSlot.getCount();
        }
      }

      return result;
    }

    public ItemStack getFirstNonEmptyItemStack() {

      for (int i = 0; i < this.getSlots(); i++) {
        ItemStack stackInSlot = this.getStackInSlot(0);

        if (!stackInSlot.isEmpty()) {
          return stackInSlot;
        }
      }
      return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      if (!this.tile.isItemValidForInsertion(stack)) {
        return stack; // item is not valid for insertion, fail
      }

      CompactingBinRecipe recipe = CompactingBinRecipe.getRecipe(stack);

      if (recipe == null) {
        // This should never happen because the item's recipe is checked above.
        return stack; // item has no recipe, fail
      }

      int max = ModulePyrotechConfig.COMPACTING_BIN.MAX_CAPACITY * recipe.getAmount();
      int currentTotal = this.tile.getInputStackHandler().getTotalItemCount();

      if (currentTotal == max) {
        return stack; // There's no room for insert, fail

      } else if (currentTotal + stack.getCount() <= max) {
        // There's enough room for all items in the stack
        this.insertItem(stack, simulate);
        return ItemStack.EMPTY;

      } else {
        // Trim the input stack down to size and insert
        ItemStack toInsert = stack.copy();
        int insertCount = max - currentTotal;
        toInsert.setCount(insertCount);
        this.insertItem(toInsert, simulate);
        ItemStack toReturn = stack.copy();
        toReturn.setCount(toReturn.getCount() - insertCount);
        return toReturn;
      }
    }

    public int removeItems(int amount) {

      int remaining = amount;

      for (int i = this.getSlots() - 1; i >= 0; i--) {

        if (!this.getStackInSlot(i).isEmpty()) {
          remaining -= super.extractItem(i, remaining, false).getCount();

          if (remaining == 0) {
            return amount;
          }
        }
      }

      return amount - remaining;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

      for (int i = this.getSlots() - 1; i >= 0; i--) {

        if (!this.getStackInSlot(i).isEmpty()) {
          return super.extractItem(i, amount, simulate);
        }
      }

      return ItemStack.EMPTY;
    }
  }

  private class DynamicStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public DynamicStackHandler(int initialSize) {

      super(1);
      this.stacks = new ItemStackList(initialSize);
    }

    @Override
    public void setSize(int size) {

      this.stacks = new ItemStackList(size);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      while (this.getSlots() - 1 < slot) {
        this.stacks.add(ItemStack.EMPTY);
      }

      return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

      if (slot > this.getSlots() - 1) {
        return ItemStack.EMPTY;
      }

      return super.extractItem(slot, amount, simulate);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {

      while (this.getSlots() - 1 < slot) {
        this.stacks.add(ItemStack.EMPTY);
      }

      super.setStackInSlot(slot, stack);
    }

    public void insertItem(ItemStack itemStack, boolean simulate) {

      ItemStack remaining = itemStack;
      int i = 0;

      while (!remaining.isEmpty()) {

        while (this.getSlots() - 1 < i) {
          this.stacks.add(ItemStack.EMPTY);
        }

        remaining = super.insertItem(i, remaining, simulate);
        i += 1;
      }
    }

    public void clearStacks() {

      for (int i = 0; i < this.getSlots(); i++) {
        this.extractItem(i, this.getSlotLimit(i), false);
      }
    }

    private class ItemStackList
        extends NonNullList<ItemStack> {

      public ItemStackList(int size) {

        super(new ArrayList<>(size), ItemStack.EMPTY);

        for (int i = 0; i < size; i++) {
          this.add(ItemStack.EMPTY);
        }
      }
    }
  }

}
